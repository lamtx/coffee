package erika.app.coffee.service;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import erika.app.coffee.model.ClientInfo;
import erika.app.coffee.model.ResponseHandler;
import erika.app.coffee.service.communication.request.Request;
import erika.app.coffee.service.communication.response.Response;
import erika.core.communication.MissingFieldException;
import erika.core.communication.Reader;
import erika.core.threading.ContinuationOptions;
import erika.core.threading.Task;
import erika.core.threading.TaskCompletionSource;

public class Client {
    public enum ConnectResult {
        CREDENTIALS_MISSING,
        HOST_NOT_FOUND,
        CANNOT_CONNECT,
        CONNECTION_TIMEOUT,
        INVALID_AUTHENTICATION,
        PARSE_ERROR,
        OK,
    }

    public enum Status {
        DISCONNECTED, CONNECTING, CONNECTED,
    }

    public static class ClassMissMatchException extends RuntimeException {
        private final Class<?> expectedClass;
        private final Class<?> actualClass;
        private final Response actualResponse;

        public ClassMissMatchException(Class<?> expectedClass, Class<?> actualClass, Response actualResponse) {
            this.expectedClass = expectedClass;
            this.actualClass = actualClass;
            this.actualResponse = actualResponse;
        }
    }

    public static class UnableToSendRequestException extends RuntimeException {
    }

    private interface UncheckedTaskCompletionSource {
        void setResult(Response response);

        void setException(Exception exception);

        void setCancellation();
    }

    private static class CheckedTaskCompletionSource<T extends Response> implements UncheckedTaskCompletionSource {
        final TaskCompletionSource<T> taskCompletionSource = new TaskCompletionSource<>();
        final Class<T> expectedClass;

        CheckedTaskCompletionSource(Class<T> expectedClass) {
            this.expectedClass = expectedClass;
        }

        @Override
        public void setResult(Response response) {
            if (expectedClass.isAssignableFrom(response.getClass())) {
                @SuppressWarnings({"unchecked"})
                T value = (T) response;
                taskCompletionSource.setResult(value);
            } else {
                setException(new ClassMissMatchException(expectedClass, response.getClass(), response));
            }
        }

        @Override
        public void setException(Exception exception) {
            taskCompletionSource.setException(exception);
        }

        @Override
        public void setCancellation() {
            taskCompletionSource.setCancellation();
        }

        public Task<T> getTask() {
            return taskCompletionSource.getTask();
        }
    }

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private final ResponseHandler responseHandler;
    private Status status = Status.DISCONNECTED;
    private final SparseArray<UncheckedTaskCompletionSource> pendingActions = new SparseArray<>();
    private static int random = 0;
    private final Handler handler = new Handler();
    private int privilegeMask;
    private ClientInfo credentials;
    private Handler writerHandler;

    public Client(ResponseHandler handler) {
        this.responseHandler = handler;
    }

    public void setCredentials(ClientInfo clientInfo) {
        this.credentials = clientInfo;
    }

    public Task<ConnectResult> connect() {
        if (credentials == null) {
            throw new RuntimeException("Credentials is missing");
        }
        disconnect();
        TaskCompletionSource<ConnectResult> source = new TaskCompletionSource<>();
        new Thread(() -> {
            status = Status.CONNECTING;
            final ConnectResult completedStatus = establishConnection();
            source.setResult(completedStatus);
            if (completedStatus == ConnectResult.OK) {
                Client.this.status = Status.CONNECTED;
                listen();
            } else {
                status = Status.DISCONNECTED;
            }
        }).start();
        return source.getTask();
    }

    private ConnectResult establishConnection() {
        if (credentials == null) {
            return ConnectResult.CREDENTIALS_MISSING;
        }
        try {
            socket = new Socket();
            InetSocketAddress sockAddress = new InetSocketAddress(credentials.host, credentials.port);
            socket.connect(sockAddress, 10_000);
        } catch (SocketTimeoutException ignored) {
            return ConnectResult.HOST_NOT_FOUND;
        } catch (Exception e) {
            return ConnectResult.CANNOT_CONNECT;
        }
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            writer.write(credentials.userName + " " + credentials.password);
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            disconnect();
            return ConnectResult.CANNOT_CONNECT;
        }
        String line;
        try {
            line = reader.readLine();
        } catch (IOException e1) {
            return ConnectResult.CANNOT_CONNECT;
        }
        if (!TextUtils.isEmpty(line)) {
            int code;
            try {
                code = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                return ConnectResult.PARSE_ERROR;
            }
            if (code >= 0) {
                privilegeMask = code;
            }
            return code < 0 ? ConnectResult.INVALID_AUTHENTICATION : ConnectResult.OK;
        } else {
            return ConnectResult.PARSE_ERROR;
        }
    }

    public void disconnect() {
        if (status == Status.DISCONNECTED) {
            return;
        }
        status = Status.DISCONNECTED;
        clearPendingRequests();
        try {
            writer.close();
        } catch (Exception ignored) {
        }
        try {
            reader.close();
        } catch (Exception ignored) {
        }
        try {
            socket.close();
        } catch (Exception ignored) {
        }
        reader = null;
        writer = null;
        socket = null;
        if (writerHandler != null) {
            writerHandler.getLooper().quit();
            writerHandler = null;
        }
    }

    private void clearPendingRequests() {
        for (int i = 0; i < pendingActions.size(); i++) {
            pendingActions.get(pendingActions.keyAt(i)).setCancellation();
        }
        pendingActions.clear();
    }

    private void listen() {
        prepareWriterThread();
        Response response;
        String message;
        while (true) {
            if (reader == null) {
                break;
            }
            try {
                message = reader.readLine();
                if (message == null) {
                    break;
                }
            } catch (IOException e) {
                break;
            }
            try {
                response = Response.PARSER.parse(new Reader(message));
            } catch (MissingFieldException e) {
                e.printStackTrace();
                continue;
            }
            performResponse(response);
        }
        disconnect();
    }

    private void prepareWriterThread() {
        new Thread(() -> {
            Looper.prepare();
            writerHandler = new Handler();
            Looper.loop();
        }).start();
    }


    private Task<Boolean> sendRawRequest(Request request) {
        TaskCompletionSource<Boolean> source = new TaskCompletionSource<>();
        if (writer == null || writerHandler == null) {
            source.setResult(false);
        } else {
            writerHandler.post(() -> {
                try {
                    writer.write(request.toMessage());
                    writer.newLine();
                    writer.flush();
                } catch (IOException e) {
                    disconnect();
                    source.setResult(false);
                    return;
                }
                source.setResult(true);
            });
        }
        return source.getTask();
    }

    private void performResponse(final Response response) {
        final UncheckedTaskCompletionSource action = pendingActions.get(response.getSequenceId());
        if (action != null) {
            pendingActions.delete(response.getSequenceId());
            handler.post(() -> action.setResult(response));
        } else {
            handler.post(() -> responseHandler.handle(response));
        }
    }

    public <T extends Response> Task<T> sendRequest(Request request, Class<T> expected) {
        CheckedTaskCompletionSource<T> taskCompletionSource = new CheckedTaskCompletionSource<>(expected);
        trySendRequest(request, taskCompletionSource);
        return taskCompletionSource.getTask();
    }

    public void sendRequest(Request request) {
        trySendRequest(request, null);
    }

    private <T extends Response> void trySendRequest(Request request, @Nullable CheckedTaskCompletionSource<T> taskSource) {
        int sequenceId = taskSource == null ? 0 : ++random;
        request.setSequenceId(sequenceId);
        sendRawRequest(request).then(task -> {
            if (!task.getResult()) {
                if (credentials != null) {
                    reconnect(request, taskSource);
                } else {
                    if (taskSource != null) {
                        taskSource.setException(new UnableToSendRequestException());
                    }
                }
            } else if (taskSource != null) {
                pendingActions.put(sequenceId, taskSource);
            }
        }, ContinuationOptions.EXECUTE_SYNCHRONOUSLY);

    }

    private <T extends Response> void reconnect(Request request, @Nullable CheckedTaskCompletionSource<T> taskSource) {
        responseHandler.statusChanged(Status.DISCONNECTED, Status.CONNECTING);
        connect().then(task -> {
            if (task.getResult() == ConnectResult.OK) {
                responseHandler.statusChanged(Status.CONNECTED, Status.CONNECTED);
                trySendRequest(request, taskSource);
            } else {
                if (taskSource != null) {
                    taskSource.setException(new UnableToSendRequestException());
                }
                responseHandler.statusChanged(Status.DISCONNECTED, Status.DISCONNECTED);
            }
        });
    }

    public boolean isConnected() {
        return status == Status.CONNECTED;
    }

    public int getPrivilegeMask() {
        return privilegeMask;
    }

}
