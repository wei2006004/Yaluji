package com.ylj.common.config;

import com.ylj.common.bean.Task;

/**
 * Created by Administrator on 2016/3/4 0004.
 */
public class AppStatus {
    boolean isLogin = false;
    boolean isAdmin = false;
    long loginId;
    String loginName = ""; //姓名

    public static final int CONNECT_MODE_NONE = 0;
    public static final int CONNECT_MODE_BLUETOOTH = 1;
    public static final int CONNECT_MODE_WIFI = 2;

    boolean isConnect = false;
    int connectMode = CONNECT_MODE_NONE;

    Task currentTask = null;

    private AppStatus() {
    }

    private static AppStatus mInstance = new AppStatus();

    public static AppStatus instance() {
        return mInstance;
    }

    public long getLoginId() {
        return loginId;
    }

    public void setLoginId(long loginId) {
        this.loginId = loginId;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setIsConnect(boolean isConnect) {
        this.isConnect = isConnect;
    }

    public int getConnectMode() {
        return connectMode;
    }

    public void setConnectMode(int connectMode) {
        this.connectMode = connectMode;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }
}
