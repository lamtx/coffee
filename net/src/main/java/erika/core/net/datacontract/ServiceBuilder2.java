package erika.core.net.datacontract;

import android.support.annotation.Nullable;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;

import erika.core.net.Credentials;
import erika.core.net.HttpHeaders;
import erika.core.net.HttpMethod;
import erika.core.net.HttpStatusException;
import erika.core.net.MimeType;
import erika.core.net.Utility;
import erika.core.threading.CancellationToken;
import erika.core.threading.Task;
import erika.core.threading.TaskBody;
import erika.core.threading.TaskContinuation;
import erika.core.threading.TaskFactory;

public class ServiceBuilder2 {
    private HttpMethod method = HttpMethod.POST;

    private static class NameValue {
        public final String name;
        public final String value;

        public NameValue(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

    public enum ParameterEncodeType {
        URL_ENCODED_PREFERRED, URL_ENCODED, JSON
    }

    private final Parameter.ParameterHelper params = new Parameter.ParameterHelper();
    private final ArrayList<NameValue> headers = new ArrayList<>();
    private final String url;
    private boolean hasCache = false;
    private Credentials credentials;
    private ParameterEncodeType parameterEncodeType = ParameterEncodeType.URL_ENCODED_PREFERRED;
    private CancellationToken cancellationToken;
    private MimeType mimeType = MimeType.json;

    public ServiceBuilder2(String url) {
        this.url = url;
    }

    public ServiceBuilder2 hasCache() {
        this.hasCache = true;
        prepareForCache();
        return this;
    }

    public ServiceBuilder2 authorize(Credentials credentials) {
        this.credentials = credentials;
        return this;
    }

    public ServiceBuilder2 cancellationToken(CancellationToken token) {
        this.cancellationToken = token;
        return this;
    }

    public ServiceBuilder2 parameterEncodeType(ParameterEncodeType type) {
        parameterEncodeType = type;
        return this;
    }

    public ServiceBuilder2 setParameter(Parameter parameter) {
        parameter.toParameter(params);
        return this;
    }

    public ServiceBuilder2 method(HttpMethod method) {
        this.method = method;
        return this;
    }

    public ServiceBuilder2 mimeType(@Nullable MimeType mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public ServiceBuilder2 add(String name, @Nullable CharSequence value) {
        if (value == null || value.length() == 0) {
            return this;
        }
        params.add(name, value);
        return this;
    }

    public ServiceBuilder2 add(String name, int value) {
        params.add(name, value);
        return this;
    }

    public ServiceBuilder2 add(String name, long value) {
        params.add(name, value);
        return this;
    }

    public ServiceBuilder2 add(String name, double value) {
        params.add(name, value);
        return this;
    }

    public ServiceBuilder2 add(String name, boolean value) {
        params.add(name, value);
        return this;
    }

    public ServiceBuilder2 add(String name, Date value) {
        params.add(name, value);
        return this;
    }

    public ServiceBuilder2 add(String name, Parameter value) {
        params.add(name, value);
        return this;
    }

    public ServiceBuilder2 add(String name, Number value) {
        params.add(name, value);
        return this;
    }

    public ServiceBuilder2 add(String name, Boolean value) {
        params.add(name, value);
        return this;
    }

    public <T extends Parameter> ServiceBuilder2 add(String name, T[] value) {
        params.add(name, value);
        return this;
    }

    public ServiceBuilder2 add(String name, int[] value) {
        params.add(name, value);
        return this;
    }

    public ServiceBuilder2 add(String name, List<String> value) {
        params.add(name, value);
        return this;
    }

    public ServiceBuilder2 add(String name, String[] value) {
        params.add(name, value);
        return this;
    }

    public ServiceBuilder2 addHeader(String header, String value) {
        headers.add(new NameValue(header, value));
        return this;
    }

    private String makeRequest() throws IOException, HttpStatusException, CancellationException {
        URL url = new URL(parseUrl());
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method.name());
        if (mimeType != null) {
            connection.setRequestProperty(HttpHeaders.ACCEPT, mimeType.contentType);
        }
        connection.setRequestProperty(HttpHeaders.ACCEPT_CHARSET, "utf-8");
        connection.setInstanceFollowRedirects(false);
        if (credentials != null) {
            credentials.prepareRequest(connection);
        }
        for (NameValue nameValue : headers) {
            connection.setRequestProperty(nameValue.name, nameValue.value);
        }

        if (cancellationToken != null) {
            cancellationToken.onCancel(new Runnable() {
                @Override
                public void run() {
                    connection.disconnect();
                }
            });
        }
        writeContent(connection);
        int responseCode = connection.getResponseCode();
        if (responseCode < 200 || responseCode >= 300) {
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
            throw new HttpStatusException(content, responseCode);
        }
        if (cancellationToken != null) {
            cancellationToken.throwIfRequested();
        }
        return Utility.readString(connection.getInputStream(), connection.getContentEncoding(), connection.getContentLength());
    }

    private String parseUrl() {
        if (method != HttpMethod.GET || params.isEmpty()) {
            return url;
        }
        if (parameterEncodeType == ParameterEncodeType.JSON || (parameterEncodeType == ParameterEncodeType.URL_ENCODED_PREFERRED && params.hasObject())) {
            throw new UnsupportedOperationException("Method GET doesn't support content type is json");
        }
        return url + "?" + params.toUrlEncoded();
    }

    private void writeContent(HttpURLConnection connection) throws IOException {
        if (params.isEmpty() || method == HttpMethod.GET) {
            return;
        }
        ParameterEncodeType encodeType = parameterEncodeType == ParameterEncodeType.URL_ENCODED_PREFERRED
                ? (params.hasObject() ? ParameterEncodeType.JSON : ParameterEncodeType.URL_ENCODED)
                : parameterEncodeType;

        String contentType = encodeType == ParameterEncodeType.URL_ENCODED
                ? "application/x-www-form-urlencoded"
                : "application/json";

        String data = encodeType == ParameterEncodeType.URL_ENCODED
                ? params.toUrlEncoded()
                : params.toJson().toString();
        byte[] rawData = data.getBytes(Charset.forName("UTF-8"));

        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Content-Length", Integer.toString(rawData.length));
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        try {
            wr.write(rawData);
        } finally {
            wr.close();
        }
    }

    public <T> T getResponse(final DataCreator<T> creator, T defaultObject) {
        try {
            String response = makeRequest();
            JSONObject json = new JSONObject(new JSONTokener(response));
            return creator.createFromJson(new JsonHelper(json));
        } catch (Exception e) {
            return defaultObject;
        }
    }

    public Task<Void> getResponseAsync() {
        return TaskFactory.startNew(new TaskBody<Void>() {
            @Override
            public Void apply() throws Exception {
                makeRequest();
                return null;
            }
        });
    }

    public <T> Task<T> getResponseAsync(final DataCreator<T> creator) {
        return TaskFactory.startNew(new TaskBody<T>() {
            @Override
            public T apply() throws Exception {
                String response = makeRequest();
                JSONObject json = new JSONObject(new JSONTokener(response));
                return creator.createFromJson(new JsonHelper(json));
            }
        });
    }

    public <T> List<T> getArrayListResponse(final DataCreator<T> activator) {
        if (hasCache) {
            @SuppressWarnings("unchecked")
            List<T> o = (List<T>) caches.get(url);
            if (o != null) {
                return o;
            }
        }
        List<T> result;
        try {
            result = createArrayList(activator, makeRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (hasCache) {
            if (result != null && result.size() != 0) {
                caches.put(url, result);
            }
        }
        return result;
    }

    public <T> Task<List<T>> getArrayListResponseAsync(final DataCreator<T> activator) {
        if (hasCache) {
            @SuppressWarnings("unchecked")
            List<T> o = (List<T>) caches.get(url);
            if (o != null) {
                return Task.completedTask(o);
            }
        }
        Task<List<T>> task = TaskFactory.startNew(new TaskBody<List<T>>() {
            @Override
            public List<T> apply() throws Exception {
                return createArrayList(activator, makeRequest());
            }
        });
        if (hasCache) {
            return task.then(new TaskContinuation<List<T>, List<T>>() {

                @Override
                public List<T> onContinue(Task<List<T>> task) {
                    if (task.isCompleted()) {
                        caches.put(url, task.getResult());
                    }
                    return task.getResult();
                }
            });
        } else {
            return task;
        }
    }

    public <T> Task<T[]> getArrayResponseAsync(final DataCreator<T> creator) {

        if (hasCache) {
            @SuppressWarnings("unchecked")
            T[] o = (T[]) caches.get(url);
            if (o != null) {
                return Task.completedTask(o);
            }
        }


        Task<T[]> task = TaskFactory.startNew(new TaskBody<T[]>() {
            @Override
            public T[] apply() throws Exception {
                return createArray(creator, makeRequest());
            }
        });
        if (hasCache) {
            return task.then(new TaskContinuation<T[], T[]>() {

                @Override
                public T[] onContinue(Task<T[]> task) {
                    if (task.isCompleted()) {
                        caches.put(url, task.getResult());
                    }
                    return task.getResult();
                }
            });
        } else {
            return task;
        }
    }


    private static JSONArray jsonArrayFromString(String response) throws JSONException {
        return new JSONArray(new JSONTokener(response));
    }

    private static <T> T[] createArray(DataCreator<T> creator, String response) throws JSONException {
        JSONArray json = jsonArrayFromString(response);
        T[] t = creator.newArray(json.length());
        for (int i = 0; i < t.length; i++) {
            t[i] = creator.createFromJson(new JsonHelper(json.getJSONObject(i)));
        }
        return t;
    }

    private static <T> List<T> createArrayList(DataCreator<T> creator, String response) throws JSONException {
        JSONArray json = jsonArrayFromString(response);
        int len = json.length();
        ArrayList<T> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            list.add(creator.createFromJson(new JsonHelper(json.getJSONObject(i))));
        }
        return list;
    }

    private static Map<String, Object> caches = null;

    private void prepareForCache() {
        if (caches == null) {
            caches = new HashMap<>();
        }
    }
}
