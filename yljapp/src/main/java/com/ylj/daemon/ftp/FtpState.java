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
}
