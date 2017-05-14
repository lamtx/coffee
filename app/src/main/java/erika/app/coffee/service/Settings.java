package erika.app.coffee.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import erika.app.coffee.R;

public class Settings {
    private static Settings instance;

    public static Settings shared(Context context) {
        if (instance == null) {
            instance = new Settings(context.getApplicationContext());
        }
        return instance;
    }

    private final String keyUserName;
    private final String keyPassword;
    private final String keyHost;
    private final String keyLeftPanelWidth;
    private final SharedPreferences prefs;

    private Settings(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        keyUserName = context.getString(R.string.key_user_name);
        keyPassword = context.getString(R.string.key_password);
        keyHost = context.getString(R.string.key_host);
        keyLeftPanelWidth = context.getString(R.string.key_left_panel_width);
    }

    public String getUserName() {
        return prefs.getString(keyUserName, null);
    }

    public Settings setUserName(String userName) {
        prefs.edit().putString(keyUserName, userName).apply();
        return this;
    }

    public String getPassword() {
        return prefs.getString(keyPassword, null);
    }

    public Settings setPassword(String password) {
        prefs.edit().putString(keyPassword, password).apply();
        return this;
    }

    public String getHost() {
        return prefs.getString(keyHost, null);
    }

    public Settings setHost(String host) {
        prefs.edit().putString(keyHost, host).apply();
        return this;
    }

    public float getLeftPanelWidth() {
        return prefs.getFloat(keyLeftPanelWidth, 0.2f);
    }

    public Settings setLeftPanelWidth(float width) {
        prefs.edit().putFloat(keyLeftPanelWidth, width).apply();
        return this;
    }
}
