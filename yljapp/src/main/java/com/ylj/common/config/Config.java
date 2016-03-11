package com.ylj.common.config;

import android.content.Context;
import android.content.SharedPreferences;

import org.xutils.x;

/**
 * Created by Administrator on 2016/3/4 0004.
 */
public class Config {
    public static final String PREF_NAME_GLOBEL = "PREF_NAME_GLOBEL";

    private String mPrefName = PREF_NAME_GLOBEL;

    public Config(String prefName) {
        mPrefName = prefName;
    }

    private static Config mInstance;

    public static Config appInstance() {
        if (mInstance == null)
            mInstance = new Config(PREF_NAME_GLOBEL);
        return mInstance;
    }

    public String getConfig(String pref_tag) {
        return getConfig(pref_tag, "");
    }

    public String getConfig(String pref_tag, String default_value) {
        SharedPreferences sharedPreferences = getPreferences();
        return sharedPreferences.getString(pref_tag, default_value);
    }

    public boolean getBoolConfig(String pref_tag) {
        return getBoolConfig(pref_tag, false);
    }

    public boolean getBoolConfig(String pref_tag, boolean default_value) {
        SharedPreferences sharedPreferences = getPreferences();
        return sharedPreferences.getBoolean(pref_tag, default_value);
    }

    public void setBoolConfig(String pref_tag, boolean value) {
        SharedPreferences sharedPreferences = getPreferences();
        sharedPreferences.edit().putBoolean(pref_tag, value).apply();
    }

    public void setConfig(String pref_tag, String value) {
        SharedPreferences sharedPreferences = getPreferences();
        sharedPreferences.edit().putString(pref_tag, value).apply();
    }

    private SharedPreferences getPreferences() {
        return x.app().getApplicationContext().
                getSharedPreferences(
                        mPrefName,
                        Context.MODE_PRIVATE);
    }

}
