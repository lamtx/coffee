package erika.redux.demo.services;

import android.os.Handler;
import android.text.TextUtils;
import android.util.SparseArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import erika.core.communication.MissingFieldException;
import erika.core.communication.Reader;
import erika.redux.demo.application.Define;
import erika.redux.demo.models.ResponseAction;
import erika.redux.demo.models.ResponseHandler;
import erika.redux.demo.services.communication.requests.Request;
import erika.redux.demo.services.communication.responses.Response;


public class Client {
    public enum SocketStatus {
        CannotConnect("Không kết nối được thiết bị"),
        ConnectionTimeout("Kết nối hết thời gian"),
        InvalidAuthentication("Tài khoản hoặc mật khẩu không khớp"),
        ParseError("Không đọc được dữ liệu từ thiết bị"),
        Ok("Đăng nhập thành công");

        public final String description;

        SocketStatus(String description) {
            this.description = description;
        }
    }

    public interface ConnectCompleted {
        void onConnected(SocketStatus status);
    }

    private interface ResponseActionWrapper {
        void run(Response response);

        void cancel();
    }

    private static class ResponseActionWrapperImpl<T extends Response> implements ResponseActionWrapper {
        final ResponseAction<T> action;

        public ResponseActionWrapperImpl(ResponseAction<T> action) {
            this.action = action;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run(Response response) {
            action.receive((T) response);
        }

        @Override
        public void cancel() {
            action.cancel();
        }

    }

    public static final int DISCONNECTED = 0;
    public static final int CONNECTING = 1;
    public static final int CONNECTED = 2;

    private final String host;
    private final int port;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private final ResponseHandler responseHandler;
    private int status = DISCONNECTED;
    private final SparseArray<ResponseActionWrapper> pendingAction = new SparseArray<>();
    private static int random = 0;
    private final Handler handler;
    private final String userName;
    private final String password;
    private int privilegeMask;

    public Client(String host, int port, String userName, String password, ResponseHandler handler) {
        this.handler = new Handler();
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.responseHandler = handler;
    }

    /**
     * Please call this method in background thread
     *
     */
    public void connectAsync(final ConnectCompleted onCompleted) {
        status = DISCONNECTED;
        new Thread(new Runnable() {

            @Override
            public void run() {
                status = CONNECTING;
                final SocketStatus completedStatus = establishConnection();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onCompleted.onConnected(completedStatus);
                    }
                });
                if (completedStatus == SocketStatus.Ok) {
                    Client.this.status = CONNECTED;
                    onListen();
                } else {
                    status = DISCONNECTED;
                }
            }
        }).start();

    }

    private SocketStatus establishConnection() {
        try {
            socket = new Socket();
            InetSocketAddress sockAddr = new InetSocketAddress(host, port);
            socket.connect(sockAddr, Define.CONNECTION_TIMEOUT);
        } catch (Exception e) {
            return SocketStatus.CannotConnect;
        }
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            writer.write(userName + " " + password);
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

            return code < 0 ? SocketStatus.InvalidAuthentication : SocketStatus.Ok;
        } else {
            return SocketStatus.ParseError;
        }
    }

    public void disconnect() {
        if (status == DISCONNECTED) {
            return;
        }
        status = DISCONNECTED;
        for (int i = 0; i < pendingAction.size(); i++) {
            pendingAction.valueAt(i).cancel();
        }
        pendingAction.clear();
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

    private void onListen() {
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

    private int sendRawRequest(Request request) {
        if (writer != null) {
            try {
                writer.write(request.toMessage());
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                disconnect();
                return Define.SOCKET_ERROR_IOEXCEPTION;
            }
        } else {
            disconnect();
            return Define.SOCKET_ERROR_NOT_CONNECTED;
        }
        return Define.SOCKET_ERROR_OK;
    }

    private void performResponse(final Response response) {
        final ResponseActionWrapper action = pendingAction.get(response.getSequenceId());
        if (action != null) {
            pendingAction.delete(response.getSequenceId());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    action.run(response);
                }
            });
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    responseHandler.handle(response);
                }
            });
        }
    }

    public <T extends Response> void sendRequest(Request request) {
        sendRequest(request, null);
    }

    public <T extends Response> void sendRequest(Request request, final ResponseAction<T> action) {
        int sequenceId = action == null ? 0 : ++random;
        request.setSequenceId(sequenceId);
        int code = sendRawRequest(request);
        if (code != Define.SOCKET_ERROR_OK) {
            reconnect(request, action);
            return;
        }
        if (action != null) {
            pendingAction.put(sequenceId, new ResponseActionWrapperImpl<T>(action));
        }
    }

    private <T extends Response> void reconnect(final Request request, final ResponseAction<T> action) {
        responseHandler.statusChanged(DISCONNECTED, "Đang kết nối");
        connectAsync(new ConnectCompleted() {
            @Override
            public void onConnected(SocketStatus status) {
                if (status == SocketStatus.Ok) {
                    responseHandler.statusChanged(CONNECTING, "Đang kết nối");
                    sendRequest(request, action);
                } else {
                    action.cancel();
                    responseHandler.statusChanged(DISCONNECTED, "Mất kết nối");
                }
            }
        });
    }

    public boolean isConnected() {
        return status == CONNECTED;
    }

    public int getPrivilegeMask() {
        return privilegeMask;
    }

}
