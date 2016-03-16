package com.ylj.common.config;

/**
 * Created by Administrator on 2016/3/16 0016.
 */
public class StatusLet {

    public static boolean isConnect(){
        return AppStatus.instance().isConnect();
    }

    public static boolean isLogin(){
        return AppStatus.instance().isLogin();
    }

    public static boolean isAdmin(){
        return AppStatus.instance().isAdmin();
    }

}
