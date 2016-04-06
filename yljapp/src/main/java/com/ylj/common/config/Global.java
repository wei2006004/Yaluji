package com.ylj.common.config;

import android.os.Environment;

import org.xutils.x;

/**
 * Created by Administrator on 2016/3/6 0006.
 */
public class Global {

    public static final String SYSTEM_DB_NAME = "yljapp.db";

    public static final String RECORD_DB_STORAGE_DIR = "yljapp/record/";

    public static final String PREF_TAG_DEBGU = "PREF_TAG_DEBGU";
    public static final String PREF_TAG_FIRST_LAUNCH = "PREF_TAG_FIRST_LAUNCH";

    public static final String PREF_TAG_FTP_IP = "PREF_TAG_FTP_IP";
    public static final String PREF_TAG_FTP_PORT = "PREF_TAG_FTP_PORT";
    public static final String PREF_TAG_FTP_USER = "PREF_TAG_FTP_USER";
    public static final String PREF_TAG_FTP_PASSWD = "PREF_TAG_FTP_PASSWD";

    public static final String PREF_TAG_WIFI_IP = "PREF_TAG_WIFI_IP";
    public static final String PREF_TAG_WIFI_PORT = "PREF_TAG_WIFI_PORT";

    public static String getRecordStorgeDir() {
        if(!isSdCardExist()){
            return x.app().getApplicationContext().getFilesDir().getAbsolutePath()+ "/" + RECORD_DB_STORAGE_DIR;
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + RECORD_DB_STORAGE_DIR;
    }

    private static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }
}
