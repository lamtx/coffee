package erika.core.net;

import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import erika.core.threading.CancellationToken;
import erika.core.threading.CancellationTokenSource;
import erika.core.threading.Task;
import erika.core.threading.TaskBody;
import erika.core.threading.TaskFactory;

public class HttpDownloadClient {
    private final String url;
    private final boolean explicitPath;
    private long length;
    private long current;
    private String path;
    private Credentials credentials;

    private HttpURLConnection connection;
    private CancellationToken cancellationToken;

    public long length() {
        return length;
    }

    public long current() {
        return current;
    }


    /**
     * Instance the {@code HttpDownloadClient} object
     *
     * @param path Saved location
     * @param url  Url of url
     */
    public HttpDownloadClient(String path, String url, boolean explicitPath) {
        this.path = path;
        this.url = url;
        length = 0;
        current = 0;
        this.explicitPath = explicitPath;
    }

    public HttpDownloadClient authorize(Credentials credentials) {
        this.credentials = credentials;
        return this;
    }

    public HttpDownloadClient cancellationToken(CancellationToken cancellationToken) {
        this.cancellationToken = cancellationToken;
        return this;
    }

    /**
     * Return location of downloaded file if successfully
     */
    public Task<String> start() {
        return TaskFactory.startNew(new TaskBody<String>() {
            @Override
            public String apply() throws Exception {
                URL request = new URL(url);
                connection = (HttpURLConnection) request.openConnection();
                connection.setRequestMethod(HttpMethod.GET.name());
                connection.connect();
                if (credentials != null) {
                    credentials.prepareRequest(connection);
                }
                writeOutput(connection);
                try {
                    return readInput(connection);
                } finally {
                    connection.disconnect();
                }
            }

            private String readInput(HttpURLConnection connection) throws IOException, HttpStatusException {
                int responseCode = connection.getResponseCode();
                InputStream contentStream;
                try {
                    contentStream = connection.getInputStream();
                } catch (IOException e) {
                    contentStream = connection.getErrorStream();
                }
                if (responseCode < 200 || 300 <= responseCode) {
                    String content = Utility.readString(contentStream, connection.getContentEncoding(),
                            connection.getContentLength());
                    if (cancellationToken != null) {
                        cancellationToken.throwIfRequested();
                    }
                    throw new HttpStatusException(content, responseCode);
                }

                String fileName = explicitPath
                        ? path
                        : path + "/" + findFileName(url, connection.getHeaderField(HttpHeaders.CONTENT_DISPOSITION), "unknown");
                prepareFile(fileName);
                OutputStream outputStream = new FileOutputStream(fileName);
                byte buffer[] = new byte[1024];
                current = 0;
                int len;
                CancellationToken canceller = cancellationToken == null ? CancellationTokenSource.emptyToken() : cancellationToken;
                while (!canceller.isCancellationRequested() && (len = contentStream.read(buffer)) > 0) {
                    current += len;
                    outputStream.write(buffer, 0, len);
                }
                outputStream.close();
                canceller.throwIfRequested();
                return fileName;
            }

            private boolean prepareFile(String fileName) {
                File parent = new File(fileName).getParentFile();
                return parent.exists() || parent.mkdirs();
            }

            private void writeOutput(HttpURLConnection connection) {
                // Empty
            }
        });
    }

    private static String findFileName(String url, String contentDisposition, String defaultName) {
        if (!TextUtils.isEmpty(contentDisposition)) {
            Pattern regex = Pattern.compile("(?<=filename=\").*?(?=\")");
            Matcher regexMatcher = regex.matcher(contentDisposition);
            if (regexMatcher.find()) {
                String fileName = regexMatcher.group();
                if (!TextUtils.isEmpty(fileName)) {
                    return fileName;
                }
            }
        }
        int index = url.lastIndexOf('/');
        if (index != -1 && index != url.length() - 1) {
            return url.substring(index + 1);
        }
        index = url.lastIndexOf('\\');
        if (index != -1 && index != url.length() - 1) {
            return url.substring(index + 1);
        }
        return defaultName;
    }


    public void close() {
        if (connection != null) {
            connection.disconnect();
        }
    }
}
