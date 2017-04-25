package erika.core.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import erika.core.threading.Task;
import erika.core.threading.TaskBody;
import erika.core.threading.TaskFactory;

public class HttpUploadClient {
    private static final String lineEnd = "\r\n";
    private static final String twoHyphens = "--";
    private static final String boundary = "----WebKitFormBoundaryVXhXkAWCsEUBfHEa";
    private static final Charset UTF8 = Charset.forName("utf-8");
    protected static final int maxBufferSize = 1024 * 1024;

    private long length;
    private long current;
    private final File file;
    private URL request;
    private Credentials credentials;

    private HttpURLConnection connection;

    public long length() {
        return length;
    }

    public long current() {
        return current;
    }

    public File getFile() {
        return file;
    }

    public HttpUploadClient(String path, String request) {
        this.file = new File(path);
        if (!file.isFile() && !file.exists()) {
            throw new RuntimeException(new FileNotFoundException(path));
        }
        try {
            this.request = new URL(request);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        length = file.length();
        current = 0;
    }

    public HttpUploadClient authorize(Credentials credentials) {
        this.credentials = credentials;
        return this;
    }

    public Task<String> start() {
        return TaskFactory.startNew(new TaskBody<String>() {

            @Override
            public String apply() throws Exception {
                connection = (HttpURLConnection) request.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty(HttpHeaders.CONTENT_TYPE,
                        "multipart/form-data; boundary=" + boundary);
                if (credentials != null) {
                    credentials.prepareRequest(connection);
                }

                writeOutput(connection);
                HttpStatusException ex = readInput(connection);
                if (ex.isOk()) {
                    return ex.content;
                } else {
                    throw ex;
                }
            }

            private HttpStatusException readInput(HttpURLConnection connection) throws IOException {
                int responseCode = connection.getResponseCode();
                InputStream contentStream;
                try {
                    contentStream = connection.getInputStream();
                } catch (IOException e) {
                    contentStream = connection.getErrorStream();
                }
                String content;
                try {
                    content = Utility.readString(contentStream, connection.getContentEncoding(),
                            connection.getContentLength());
                } finally {
                    try {
                        contentStream.close();
                    } catch (IOException ignored) {
                    }
                }
                return new HttpStatusException(content, responseCode);
            }

            private void writeOutput(HttpURLConnection connection) throws IOException {
                String fileName = file.getName();

                byte[] headerBytes = (twoHyphens + boundary + lineEnd
                        + "Content-Disposition: form-data; name=\"files\"; filename=\"" + fileName + "\"" + lineEnd
                        + "Content-Type: " + URLConnection.guessContentTypeFromName(fileName)
                        + lineEnd + lineEnd).getBytes(UTF8);
                byte[] footerBytes = (lineEnd + twoHyphens + boundary + twoHyphens + lineEnd).getBytes(UTF8);
                int contentLength = headerBytes.length + (int) file.length() + footerBytes.length;

                connection.setRequestProperty(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));

                OutputStream output = connection.getOutputStream();
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[Math.min((int) file.length(), maxBufferSize)];
                int len;
                try {
                    output.write(headerBytes, 0, headerBytes.length);
                    while ((len = input.read(buffer, 0, buffer.length)) > 0) {
                        output.write(buffer, 0, len);
                    }
                    output.write(footerBytes, 0, footerBytes.length);
                    output.flush();
                } finally {
                    try {
                        input.close();
                    } catch (IOException ignored) {
                    }
                    try {
                        output.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        });
    }


    public void close() {
        if (connection != null) {
            connection.disconnect();
        }
    }
}
