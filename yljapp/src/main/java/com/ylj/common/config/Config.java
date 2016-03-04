package com.ylj.common.config;

import android.content.Context;
import android.content.SharedPreferences;

import org.xutils.x;

/**
 * Created by Administrator on 2016/3/4 0004.
 */
public class Config {
    public static final String PREF_NAME = "PREF_NAME_GLOBEL";

    public static final String PREF_TAG_DEBGU = "PREF_TAG_DEBGU";

    public static final String PREF_TAG_FTP_IP = "PREF_TAG_FTP_IP";
    public static final String PREF_TAG_FTP_PORT = "PREF_TAG_FTP_PORT";
    public static final String PREF_TAG_FTP_USER = "PREF_TAG_FTP_USER";
    public static final String PREF_TAG_FTP_PASSWD = "PREF_TAG_FTP_PASSWD";

    public static final String PREF_TAG_WIFI_IP = "PREF_TAG_WIFI_IP";
    public static final String PREF_TAG_WIFI_PORT = "PREF_TAG_WIFI_PORT";

    private Config() {
    }

    private static Config mInstance = new Config();

    public static Config instance() {
        return mInstance;
    }

    public String getConfig(String pref_tag) {
        return getConfig(pref_tag, "");
    }

    public String getConfig(String pref_tag, String default_value) {
        SharedPreferences sharedPreferences = getAppPreferences();
        return sharedPreferences.getString(pref_tag, default_value);
    }

    public void setConfig(String pref_tag, String value) {
        SharedPreferences sharedPreferences = getAppPreferences();
        sharedPreferences.edit().putString(pref_tag, value).apply();
    }

    private static SharedPreferences getAppPreferences() {
        return x.app().getApplicationContext().
                getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE);
    }

}
