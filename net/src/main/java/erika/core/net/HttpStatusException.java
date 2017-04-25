package erika.core.net;

import java.net.HttpURLConnection;
import java.util.Locale;

public class HttpStatusException extends Exception {
    public final int statusCode;
    public final String content;

    public HttpStatusException(String content, int statusCode) {
        super(String.format(Locale.US, "%d : %s", statusCode, content));
        this.statusCode = statusCode;
        this.content = content;
    }

    /**
     *
     */
    private static final long serialVersionUID = -4314839439005366241L;

    public final boolean isOk() {
        return statusCode == HttpURLConnection.HTTP_OK;
    }

    public String getStatusCodeDescription() {
        return "HTTP Status code " + statusCode;
    }
}
