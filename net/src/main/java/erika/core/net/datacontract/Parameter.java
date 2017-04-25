package erika.core.net.datacontract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public interface Parameter {
    void toParameter(ParameterHelper properties);

    final class ParameterHelper {
        private boolean hasObject = false;
        private final JSONObject properties = new JSONObject();

        public ParameterHelper add(String property, Date value) {
            if (value != null) {
                try {
                    properties.put(property, ISO0861DateParser.format(value));
                } catch (JSONException e) {
                    throw new RuntimeException("adding " + property + " with value " + String.valueOf(value) + " failure", e);
                }
                return this;
            }
            return this;
        }

        public ParameterHelper add(String property, CharSequence value) {
            if (value != null && value.length() != 0) {
                try {
                    properties.put(property, value);
                } catch (JSONException e) {
                    throw new RuntimeException("adding " + property + " with value " + String.valueOf(value) + " failure", e);
                }
                return this;
            }
            return this;
        }

        public ParameterHelper add(String property, int value) {
            try {
                properties.put(property, value);
            } catch (JSONException e) {
                throw new RuntimeException("adding " + property + " with value " + String.valueOf(value) + " failure", e);
            }
            return this;
        }

        public ParameterHelper add(String property, double value) {
            try {
                properties.put(property, value);
            } catch (JSONException e) {
                throw new RuntimeException("adding " + property + " with value " + String.valueOf(value) + " failure", e);
            }
            return this;
        }

        public ParameterHelper add(String property, long value) {
            try {
                properties.put(property, value);
            } catch (JSONException e) {
                throw new RuntimeException("adding " + property + " with value " + String.valueOf(value) + " failure", e);
            }
            return this;
        }

        public ParameterHelper add(String property, boolean value) {
            try {
                properties.put(property, value);
            } catch (JSONException e) {
                throw new RuntimeException("adding " + property + " with value " + String.valueOf(value) + " failure", e);
            }
            return this;
        }

        public ParameterHelper add(String property, Boolean value) {
            if (value == null) {
                return this;
            }
            try {
                properties.put(property, value);
            } catch (JSONException e) {
                throw new RuntimeException("adding " + property + " with value " + String.valueOf(value) + " failure", e);
            }
            return this;
        }

        public ParameterHelper add(String property, Number value) {
            if (value == null) {
                return this;
            }
            try {
                properties.put(property, value);
            } catch (JSONException e) {
                throw new RuntimeException("adding " + property + " with value " + String.valueOf(value) + " failure", e);
            }
            return this;
        }

        public JSONObject toJson() {
            return properties;
        }

        public String toJson(boolean prettyFormat) {
            try {
                return properties.toString(prettyFormat ? 0 : 4);
            } catch (JSONException e) {
                return "";
            }
        }

        public ParameterHelper add(String property, String[] values) {
            if (values == null || values.length == 0) {
                return this;
            }
            hasObject = true;
            JSONArray array = new JSONArray();
            for (String value : values) {
                array.put(value);
            }
            try {
                properties.put(property, array);
            } catch (JSONException e) {
                throw new RuntimeException("adding " + property + " with value " + Arrays.toString(values) + " failure", e);
            }
            return this;
        }

        public ParameterHelper add(String property, List<String> values) {
            if (values == null || values.size() == 0) {
                return this;
            }
            hasObject = true;
            JSONArray array = new JSONArray();
            for (String value : values) {
                array.put(value);
            }
            try {
                properties.put(property, array);
            } catch (JSONException e) {
                throw new RuntimeException("adding " + property + " with value " + String.valueOf(values) + " failure", e);
            }
            return this;
        }

        public <T extends Parameter> ParameterHelper add(String property, T[] values) {
            if (values == null || values.length == 0) {
                return this;
            }
            hasObject = true;
            JSONArray array = new JSONArray();
            for (Parameter data : values) {
                ParameterHelper helper = new ParameterHelper();
                data.toParameter(helper);
                array.put(helper.toJson());
            }
            try {
                properties.put(property, array);
            } catch (JSONException e) {
                throw new RuntimeException("adding " + property + " with value " + String.valueOf(array) + " failure", e);
            }
            return this;
        }

        public ParameterHelper add(String property, int[] values) {
            hasObject = true;
            JSONArray array = new JSONArray();
            for (int data : values) {
                array.put(data);
            }
            try {
                properties.put(property, array);
            } catch (JSONException e) {
                throw new RuntimeException("adding " + property + " with value " + String.valueOf(array) + " failure", e);
            }
            return this;
        }

        public ParameterHelper add(String property, Iterable<? extends Parameter> values) {
            hasObject = true;
            JSONArray array = new JSONArray();
            for (Parameter data : values) {
                ParameterHelper helper = new ParameterHelper();
                data.toParameter(helper);
                array.put(helper.toJson());
            }
            try {
                properties.put(property, array);
            } catch (JSONException e) {
                throw new RuntimeException("adding " + property + " with value " + String.valueOf(array) + " failure", e);
            }
            return this;
        }

        public ParameterHelper add(String property, Parameter value) {
            if (value == null) {
                return this;
            }
            hasObject = true;
            try {
                ParameterHelper helper = new ParameterHelper();
                value.toParameter(helper);
                properties.put(property, helper.toJson());
            } catch (JSONException e) {
                throw new RuntimeException("adding " + property + " with value " + String.valueOf(value) + " failure", e);
            }
            return this;
        }

        public boolean isEmpty() {
            return properties.length() == 0;
        }

        public boolean hasObject() {
            return hasObject;
        }

        public String toUrlEncoded() {
            if (hasObject) {
                throw new UnsupportedOperationException("Content has object, can not be format at urlEncoded, use json instead");
            }
            StringBuilder sb = new StringBuilder();
            Iterator<String> keys = properties.keys();
            String key;
            while (keys.hasNext()) {
                key = keys.next();
                if (sb.length() != 0) {
                    sb.append("&");
                }
                try {
                    sb.append(URLEncoder.encode(key, "utf-8"))
                            .append("=")
                            .append(URLEncoder.encode(properties.get(key).toString(), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new UnsupportedOperationException("UnsupportedEncodingException:" + e.getMessage());
                } catch (JSONException e) {
                    throw new RuntimeException("JSONException:" + e.getMessage());
                }
            }
            return sb.toString();
        }
    }
}
