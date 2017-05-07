package erika.core.communication;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class NoneKeepAliveClient<T extends ReadableMessage> {
    public interface MessageParser<T> {
        <X extends T> X parse(String message);
    }

    public interface OnCompleted<T> {
        void onReceiveResponse(T response);

        void onReceiveError(SocketStatus status, String message);
    }

    private static class ResponseWrapper<T> {
        final T response;
        final SocketStatus status;
        final Exception exception;

        public ResponseWrapper(SocketStatus status, T response, Exception ex) {
            this.response = response;
            this.status = status;
            this.exception = ex;
        }
    }

    private static final int TIME_OUT = 10000;
    private final String host;
    private final int port;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private final MessageParser<T> parser;

    public NoneKeepAliveClient(String host, int port, MessageParser<T> parser) {
        this.host = host;
        this.port = port;
        this.parser = parser;
    }

    public <X extends T> void sendRequest(final WritableMessage request, final OnCompleted<X> onCompleted) {
        new AsyncTask<Void, Void, ResponseWrapper<X>>() {

            @Override
            protected ResponseWrapper<X> doInBackground(Void... params) {
                if (!establishConnection()) {
                    return new ResponseWrapper<>(SocketStatus.CANNOT_CONNECT, null, null);
                }
                try {
                    sendRequest(request);
                    X response = waitForResponse();
                    return new ResponseWrapper<>(SocketStatus.OK, response, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ResponseWrapper<>(SocketStatus.PARSE_ERROR, null, e);
                } finally {
                    close();
                }
            }

            protected void onPostExecute(ResponseWrapper<X> result) {
                if (result.status == SocketStatus.OK) {
                    onCompleted.onReceiveResponse(result.response);
                } else {
                    String message = null;
                    if (result.exception != null) {
                        message = result.exception.getMessage();
                    }
                    onCompleted.onReceiveError(result.status, message);
                }
            }

        }.execute();
    }

    private <X extends T> X waitForResponse() throws IOException {
        String line = reader.readLine();
        return parser.parse(line);
    }

    private void sendRequest(WritableMessage request) throws IOException {
        writer.write(request.toMessage());
        writer.newLine();
        writer.flush();
    }

    private boolean establishConnection() {
        try {
            socket = new Socket();
            InetSocketAddress sockAddr = new InetSocketAddress(host, port);
            socket.connect(sockAddr, TIME_OUT);
        } catch (Exception e) {
            return false;
        }
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception e) {
            close();
            return false;
        }
        return true;
    }

    private void close() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException ignored) {
            }
        }
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException ignored) {
            }
        }
        reader = null;
        writer = null;
        socket = null;
    }

}
