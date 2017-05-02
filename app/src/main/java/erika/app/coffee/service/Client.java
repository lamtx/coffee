package erika.app.coffee.service;

import android.os.Handler;
import android.support.annotation.IntDef;
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

import erika.app.coffee.application.Define;
import erika.app.coffee.model.ClientInfo;
import erika.app.coffee.model.ResponseHandler;
import erika.app.coffee.service.communication.requests.Request;
import erika.app.coffee.service.communication.responses.Response;
import erika.core.communication.MissingFieldException;
import erika.core.communication.Reader;
import erika.core.threading.Task;
import erika.core.threading.TaskCompletionSource;

public class Client {

    public enum SocketStatus {
        CannotConnect("Không kết nối được thiết bị"),
        ConnectionTimeout("Kết nối hết thời gian"),
        InvalidAuthentication("Tài khoản hoặc mật khẩu không khớp"),
        ParseError("Không đọc được dữ liệu từ thiết bị"),
        OK("Đăng nhập thành công");

        public final String description;

        SocketStatus(String description) {
            this.description = description;
        }
    }

    @IntDef({
            SocketError.OK,
            SocketError.NOT_CONNECTED,
            SocketError.IO_EXCEPTION,
            SocketError.RESPONSE_TIMEOUT,
            SocketError.MESSAGE_NOT_MATCH
    })
    public @interface SocketError {
        int OK = 0;
        int NOT_CONNECTED = -1;
        int IO_EXCEPTION = -2;
        int RESPONSE_TIMEOUT = -3;
        int MESSAGE_NOT_MATCH = -4;
    }

    public enum ConnectionStatus {
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

    public static class SocketErrorException extends RuntimeException {

        @SocketError
        private final int code;

        public SocketErrorException(@SocketError int code) {
            this.code = code;
        }

        @SocketError
        public int getErrorCode() {
            return code;
        }
    }

    private interface UncheckedTaskCompletionSource {
        void setResult(Response response);
        void setException(Exception exception);
        void setCancellation();
    }

    private static class CheckedTaskCompletionSource<T extends Response> implements UncheckedTaskCompletionSource {
        final TaskCompletionSource<T> taskCompletionSource = new TaskCompletionSource<T>();
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
    private ConnectionStatus status = ConnectionStatus.DISCONNECTED;
    private final SparseArray<UncheckedTaskCompletionSource> pendingActions = new SparseArray<>();
    private static int random = 0;
    private final Handler handler = new Handler();
    private int privilegeMask;
    private ClientInfo currentClient;

    public Client(ResponseHandler handler) {
        this.responseHandler = handler;
    }

    public Task<SocketStatus> connect(ClientInfo clientInfo) {
        disconnect();
        currentClient = clientInfo;
        TaskCompletionSource<SocketStatus> source = new TaskCompletionSource<>();
        new Thread(() -> {
            status = ConnectionStatus.CONNECTING;
            final SocketStatus completedStatus = establishConnection(clientInfo);
            source.setResult(completedStatus);
            if (completedStatus == SocketStatus.OK) {
                Client.this.status = ConnectionStatus.CONNECTED;
                listen();
            } else {
                status = ConnectionStatus.DISCONNECTED;
            }
        }).start();
        return source.getTask();
    }

    private SocketStatus establishConnection(ClientInfo client) {
        try {
            socket = new Socket();
            InetSocketAddress sockAddress = new InetSocketAddress(client.host, client.port);
            socket.connect(sockAddress, Define.CONNECTION_TIMEOUT);
        } catch (Exception e) {
            return SocketStatus.CannotConnect;
        }
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            writer.write(client.userName + " " + client.password);
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            disconnect();
            return SocketStatus.CannotConnect;
        }
        String line;
        try {
            line = reader.readLine();
        } catch (IOException e1) {
            return SocketStatus.CannotConnect;
        }
        if (!TextUtils.isEmpty(line)) {
            int code;
            try {
                code = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                return SocketStatus.ParseError;
            }
            if (code >= 0) {
                privilegeMask = code;
            }

            return code < 0 ? SocketStatus.InvalidAuthentication : SocketStatus.OK;
        } else {
            return SocketStatus.ParseError;
        }
    }

    public void disconnect() {
        if (status == ConnectionStatus.DISCONNECTED) {
            return;
        }
        status = ConnectionStatus.DISCONNECTED;
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
    }

    private void clearPendingRequests() {
        for(int i = 0; i < pendingActions.size(); i++) {
            pendingActions.get(pendingActions.keyAt(i)).setCancellation();
        }
        pendingActions.clear();
    }

    private void listen() {
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

    @SocketError
    private int sendRawRequest(Request request) {
        if (writer != null) {
            try {
                writer.write(request.toMessage());
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                disconnect();
                return SocketError.IO_EXCEPTION;
            }
        } else {
            disconnect();
            return SocketError.NOT_CONNECTED;
        }
        return SocketError.OK;
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
        int code = sendRawRequest(request);
        if (code != SocketError.OK) {
            if (currentClient != null) {
                reconnect(currentClient, request, taskSource);
            } else {
                if (taskSource != null) {
                    taskSource.setException(new SocketErrorException(code));
                }
            }
            return;
        }
        if (taskSource != null) {
            pendingActions.put(sequenceId, taskSource);
        }
    }

    private <T extends Response> void reconnect(ClientInfo clientInfo, Request request, @Nullable CheckedTaskCompletionSource<T> taskSource) {
        responseHandler.statusChanged(ConnectionStatus.DISCONNECTED, ConnectionStatus.CONNECTING);
        connect(clientInfo).then(task -> {
            if (task.getResult() == SocketStatus.OK) {
                responseHandler.statusChanged(ConnectionStatus.CONNECTING, ConnectionStatus.CONNECTING);
                trySendRequest(request, taskSource);
            } else {
                if (taskSource != null) {
                    taskSource.setException(new SocketErrorException(SocketError.NOT_CONNECTED));
                }
                responseHandler.statusChanged(ConnectionStatus.DISCONNECTED, ConnectionStatus.DISCONNECTED);
            }
        });
    }

    public boolean isConnected() {
        return status == ConnectionStatus.CONNECTED;
    }

    public int getPrivilegeMask() {
        return privilegeMask;
    }

}
