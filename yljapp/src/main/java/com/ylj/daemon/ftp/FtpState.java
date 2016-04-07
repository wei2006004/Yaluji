package com.ylj.daemon.ftp;

/**
 * Created by Administrator on 2016/4/7 0007.
 */
public class FtpState {
    public static final int STATE_NONE = 0;

    public static final int STATE_CONNECT_LOST = 1;
    public static final int STATE_SERVER_CONNECT_FAIL = 2;
    public static final int STATE_LOGIN_FAIL = 3;

    public static final int STATE_LOGIN_SUCCESS = 4;
    public static final int STATE_LOGOUT = 9;

    public static final int STATE_UPLOAD_START = 5;
    public static final int STATE_UPLOAD_FINISH = 6;
    public static final int STATE_UPLOAD_CANCEL = 7;
    public static final int STATE_UPLOAD_ERROR = 8;
}
