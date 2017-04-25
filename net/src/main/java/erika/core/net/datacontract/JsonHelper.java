package erika.core.net.datacontract;

import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class JsonHelper {
    private final JSONObject json;

    public JsonHelper(JSONObject json) {
        this.json = json;
    }

    public final String readString(String property, String defaultValue) {
        try {
            return json.getString(property);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    public final String readString(String property) {
        try {
            String value = json.getString(property);
            if ("null".equals(value)) {
                return null; // Should fuck Android or should fuck ASP.NET
            }
            return value;
        } catch (JSONException e) {
            return null;
        }
    }

    public final long readLong(String property) {
        try {
            return json.getLong(property);
        } catch (JSONException e) {
            return 0;
        }
    }

    public final int readInteger(String property) {
        try {
            return json.getInt(property);
        } catch (JSONException e) {
            return 0;
        }
    }

    public final double readDouble(String property) {
        return readDouble(property, 0);
    }

    public final <T> T readObject(DataCreator<T> creator, String property) {
        JSONObject json = readJsonObject(property);
        if (json == null) {
            return null;
        }
        return creator.createFromJson(new JsonHelper(json));
    }

    @Nullable
    public final <T> ArrayList<T> readArrayList(DataCreator<T> creator, String property) {
        JSONArray array = readJsonArray(property);
        if (array == null) {
            return null;
        }
        ArrayList<T> result = new ArrayList<>(array.length());
        for (int i = 0; i < array.length(); i++) {
            T t;
            try {
                t = creator.createFromJson(new JsonHelper(array.getJSONObject(i)));
            } catch (JSONException e) {
                continue;
            }
            result.add(t);
        }
        return result;
    }

    public final <T> T[] readArray(DataCreator<T> creator, String property) {
        JSONArray array = readJsonArray(property);
        if (array == null) {
            return null;
        }
        T[] t = creator.newArray(array.length());
        for (int i = 0; i < t.length; i++) {
            try {
                t[i] = creator.createFromJson(new JsonHelper(array.getJSONObject(i)));
            } catch (JSONException e) {
                t[i] = null;
            }
        }
        return t;
    }

    public final boolean readBoolean(String property, boolean defaultValue) {
        try {
            return json.getBoolean(property);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    public final boolean readBoolean(String property) {
        try {
            return json.getBoolean(property);
        } catch (JSONException e) {
            return false;
        }
    }

    public final Date readDate(String property) {
        try {
            return ISO0861DateParser.parse(json.getString(property));
        } catch (JSONException | ParseException e) {
            return null;
        }
    }

    public final JSONArray readJsonArray(String property) {
        try {
            return json.getJSONArray(property);
        } catch (JSONException e) {
            return null;
        }
    }

    public final JSONObject readJsonObject(String property) {
        try {
            return json.getJSONObject(property);
        } catch (JSONException e) {
            return null;
        }
    }

    public final String[] readStringArray(String property) {
        JSONArray imageJsons = readJsonArray(property);

        if (imageJsons != null) {
            String[] images;
            images = new String[imageJsons.length()];
            for (int i = 0; i < images.length; i++) {
                try {
                    images[i] = imageJsons.getString(i);
                } catch (JSONException e) {
                    images[i] = null;
                }
            }
            return images;
        }
        return null;
    }

    public double readDouble(String property, double defaultValue) {
        try {
            return json.getDouble(property);
        } catch (JSONException e) {
            return defaultValue;
        }
    }
}
