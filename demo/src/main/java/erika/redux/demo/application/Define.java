package erika.redux.demo.application;

import java.text.DecimalFormat;

public class Define {
    private Define() {
    }

    public static final java.text.DecimalFormat DecimalFormat = new DecimalFormat("###,###,###,###");
    @Deprecated
    public static final String DEFAULT_KEY = "default";
    public static final int PORT = 36524;
    // Status
    public static final int STATUS_DISCONNECTED = 0;
    public static final int STATUS_CONNECTED = 1;
    public static final int STATUS_WAITING = 2;
    public static final int SOCKET_ERROR_OK = 0;
    public static final int SOCKET_ERROR_NOT_CONNECTED = -1;
    public static final int SOCKET_ERROR_IOEXCEPTION = -2;
    public static final int SOCKET_ERROR_WAIT_RESPONSE_TIMEOUT = -3;
    public static final int SOCKET_ERROR_MESSAGE_NOT_MATCH = -4;
    public static final int CONNECTION_TIMEOUT = 20000;
    protected static final String KEY_COLUMN_WIDTH = "column-width";
}
