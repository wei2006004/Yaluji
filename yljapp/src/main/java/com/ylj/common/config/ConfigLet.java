package com.ylj.common.config;

import com.ylj.setting.def.SettingDefault;

/**
 * Created by Administrator on 2016/3/11 0011.
 */
public class ConfigLet {

    public static boolean isFirstLaunch(){
        Config config=Config.appInstance();
        return config.getBoolConfig(Global.PREF_TAG_FIRST_LAUNCH, SettingDefault.DEFAULT_TAG_FIRST_LAUNCH);
    }

    public static boolean isDebug(){
        Config config=Config.appInstance();
        return config.getBoolConfig(Global.PREF_TAG_DEBGU, SettingDefault.DEFAULT_TAG_DEBGU);
    }

    public static String getFtpIp() {
        Config config = Config.appInstance();
        return config.getConfig(Global.PREF_TAG_FTP_IP, SettingDefault.DEFAULT_TAG_FTP_IP);
    }

    public static int getFtpPort() {
        Config config = Config.appInstance();
        return Integer.parseInt(config.getConfig(Global.PREF_TAG_FTP_PORT, SettingDefault.DEFAULT_TAG_FTP_PORT));
    }

    public static String getFtpUser() {
        Config config = Config.appInstance();
        return config.getConfig(Global.PREF_TAG_FTP_USER, SettingDefault.DEFAULT_TAG_FTP_USER);
    }

    public static String getFtpPasswd() {
        Config config = Config.appInstance();
        return config.getConfig(Global.PREF_TAG_FTP_PASSWD, SettingDefault.DEFAULT_TAG_FTP_PASSWD);
    }

    public static String getWifiIp() {
        Config config = Config.appInstance();
        return config.getConfig(Global.PREF_TAG_WIFI_IP, SettingDefault.DEFAULT_TAG_WIFI_IP);
    }

    public static int getWifiPort() {
        Config config = Config.appInstance();
        return Integer.parseInt(config.getConfig(Global.PREF_TAG_WIFI_PORT, SettingDefault.DEFAULT_TAG_WIFI_PORT));
    }
}
