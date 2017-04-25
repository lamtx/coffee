package erika.core.net;

import java.net.HttpURLConnection;


/**
 * Interface for credentials to be sent in a request
 */
public interface Credentials {

    /**
     * Adds the credentials to the request
     *
     * @param connection The request to prepare
     */
    void prepareRequest(HttpURLConnection connection);
}
