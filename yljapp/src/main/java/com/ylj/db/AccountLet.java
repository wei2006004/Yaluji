package com.ylj.db;

import com.ylj.common.bean.Admin;
import com.ylj.common.bean.Staff;
import com.ylj.common.config.AppStatus;
import com.ylj.db.account.AccountManager;
import com.ylj.db.account.IAccountManager;

import java.util.List;

/**
 * Created by Administrator on 2016/3/6 0006.
 */
public class AccountLet {

    public static void doStaffLogin(int staff_id,final IRequestListener listener){
        IAccountManager accountManager=ManagerFactory.getAccountManager();
        accountManager.doStaffLogin(staff_id, new AccountManager.OnLoginListener() {
            @Override
            public void onLoginSuccess(boolean isAdmin, long loginId, String loginName) {
                AppStatus appStatuss=AppStatus.instance();
                appStatuss.setIsAdmin(isAdmin);
                appStatuss.setIsLogin(true);
                appStatuss.setLoginName(loginName);
                appStatuss.setLoginId(loginId);

                if(listener!=null){
                    listener.onRequestSuccess(null);
                }
            }

            @Override
            public void onLoginFail(int error) {
                if(listener!=null){
                    listener.onRequestFail(error);
                }
            }
        });
    }

    public static void doAdminLogin(String account_name,String passwd,final IRequestListener listener){
        IAccountManager accountManager=ManagerFactory.getAccountManager();
        accountManager.doAdminLogin(account_name, passwd, new AccountManager.OnLoginListener() {
            @Override
            public void onLoginSuccess(boolean isAdmin, long loginId, String loginName) {
                AppStatus appStatuss = AppStatus.instance();
                appStatuss.setIsAdmin(isAdmin);
                appStatuss.setIsLogin(true);
                appStatuss.setLoginName(loginName);
                appStatuss.setLoginId(loginId);

                if (listener != null) {
                    listener.onRequestSuccess(null);
                }
            }

            @Override
            public void onLoginFail(int error) {
                if (listener != null) {
                    listener.onRequestFail(error);
                }
            }
        });
    }

    public static void doLogout(final IRequestListener listener){
        AppStatus appStatuss = AppStatus.instance();
        appStatuss.setIsLogin(false);
        listener.onRequestSuccess(null);
    }

    public static void getAdminById(long admin_id,final IRequestListener<Admin> listener){
        IAccountManager accountManager=ManagerFactory.getAccountManager();
        accountManager.getAdmin(admin_id,listener);
    }

    public static void getStaffById(long staff_id,final IRequestListener<Staff> listener){
        IAccountManager accountManager=ManagerFactory.getAccountManager();
        accountManager.getStaff(staff_id, listener);
    }

    public static void getAdminList(final IRequestListener<List<Admin>> listener){
        IAccountManager accountManager=ManagerFactory.getAccountManager();
        accountManager.getAdminList(listener);
    }

    public static void getStaffList(final IRequestListener<List<Staff>> listener){
        IAccountManager accountManager=ManagerFactory.getAccountManager();
        accountManager.getStaffList(listener);
    }

}
