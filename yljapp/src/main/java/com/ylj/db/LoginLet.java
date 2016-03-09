package com.ylj.db;

import com.ylj.common.bean.Admin;
import com.ylj.common.bean.Staff;
import com.ylj.common.config.AppStatus;
import com.ylj.db.account.AccountManager;

/**
 * Created by Administrator on 2016/3/9 0009.
 */
public class LoginLet {

    public static final int LOGIN_SUCCESS = 0;

    public static final int LOGIN_ERROR_NO_ACCOUNT = 1;
    public static final int LOGIN_ERROR_WRONG_PASSWD = 2;
    public static final int LOGIN_ERROR_UNKNOW = 3;

    public static boolean doStaffLogin(Staff staff){
        AccountManager accountManager=ManagerFactory.getAccountManager();
        if(accountManager.hasStaff(staff)){
            AppStatus appStatuss = AppStatus.instance();
            appStatuss.setStaffLogin(staff);
            return true;
        }
        return false;
    }

    public static int doAdminLogin(String account_name,String passwd){
        AccountManager accountManager=ManagerFactory.getAccountManager();
        int result = accountManager.verifyAdmin(account_name, passwd);
        switch (result){
            case AccountManager.VERIFY_ERROR_NO_ACCOUNT:
                return LOGIN_ERROR_NO_ACCOUNT;
            case AccountManager.VERIFY_ERROR_WRONG_PASSWD:
                return LOGIN_ERROR_WRONG_PASSWD;
            case AccountManager.VERIFY_SUCCESS:
                Admin admin=accountManager.getAdminByAccountName(account_name);
                AppStatus appStatuss = AppStatus.instance();
                appStatuss.setAdminLogin(admin);
                return LOGIN_SUCCESS;
        }
        return LOGIN_ERROR_UNKNOW;
    }

    public static void doAnonymousLogin() {
        AppStatus appStatuss = AppStatus.instance();
        appStatuss.setAnonymousLogin();
    }

    public static boolean doLogout(){
        AppStatus appStatuss = AppStatus.instance();
        appStatuss.setLogout();
        return true;
    }
}
