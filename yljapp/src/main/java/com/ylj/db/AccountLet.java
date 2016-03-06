package com.ylj.db;

import com.ylj.common.bean.Admin;
import com.ylj.common.bean.Staff;
import com.ylj.common.config.AppStatus;
import com.ylj.db.account.IAccountManager;

import java.util.List;

/**
 * Created by Administrator on 2016/3/6 0006.
 */
public class AccountLet {

    public static void addStaff(Staff staff){
        IAccountManager accountManager=ManagerFactory.getAccountManager();
        accountManager.addStaff(staff);
    }

    public static void addAdmin(Admin admin){
        IAccountManager accountManager=ManagerFactory.getAccountManager();
        accountManager.addAdmin(admin);
    }

    public static void doStaffLogin(int staff_id,final IRequestListener listener){
        IAccountManager accountManager=ManagerFactory.getAccountManager();
        accountManager.doStaffLogin(staff_id, new IRequestListener<Staff>() {
            @Override
            public void onRequestSuccess(Staff staff) {
                AppStatus appStatuss=AppStatus.instance();
                appStatuss.setIsAdmin(false);
                appStatuss.setIsLogin(true);
                appStatuss.setCurrentStaff(staff);

                if(listener!=null){
                    listener.onRequestSuccess(null);
                }
            }

            @Override
            public void onRequestFail(int error) {
                if(listener!=null){
                    listener.onRequestFail(error);
                }
            }
        });
    }

    public static void doAdminLogin(String account_name,String passwd,final IRequestListener listener){
        IAccountManager accountManager=ManagerFactory.getAccountManager();
        accountManager.doAdminLogin(account_name, passwd, new IRequestListener<Admin>() {
            @Override
            public void onRequestSuccess(Admin admin) {
                AppStatus appStatuss = AppStatus.instance();
                appStatuss.setIsAdmin(true);
                appStatuss.setIsLogin(true);
                appStatuss.setCurrentAdmin(admin);

                if (listener != null) {
                    listener.onRequestSuccess(null);
                }
            }

            @Override
            public void onRequestFail(int error) {
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

//    public static void getAdminById(long admin_id,final IRequestListener<Admin> listener){
//        IAccountManager accountManager=ManagerFactory.getAccountManager();
//        accountManager.getAdmin(admin_id,listener);
//    }
//
//    public static void getStaffById(long staff_id,final IRequestListener<Staff> listener){
//        IAccountManager accountManager=ManagerFactory.getAccountManager();
//        accountManager.getStaff(staff_id, listener);
//    }

    public static void getAdminList(final IRequestListener<List<Admin>> listener){
        IAccountManager accountManager=ManagerFactory.getAccountManager();
        accountManager.getAdminList(listener);
    }

    public static void getStaffList(final IRequestListener<List<Staff>> listener){
        IAccountManager accountManager=ManagerFactory.getAccountManager();
        accountManager.getStaffList(listener);
    }

}
