package com.ylj.db;

import com.ylj.common.bean.Admin;
import com.ylj.common.bean.Staff;
import com.ylj.common.config.AppStatus;
import com.ylj.db.account.AccountManager;
import com.ylj.db.account.LoginResult;

import java.util.List;

/**
 * Created by Administrator on 2016/3/7 0007.
 */
public class DbLet {
    public static void addStaff(Staff staff){
        AccountManager accountManager=ManagerFactory.getAccountManager();
        accountManager.addStaff(staff);
    }

    public static void addAdmin(Admin admin){
        AccountManager accountManager=ManagerFactory.getAccountManager();
        accountManager.addAdmin(admin);
    }

    public static boolean doStaffLogin(int staff_id){
        AccountManager accountManager=ManagerFactory.getAccountManager();
        return accountManager.doStaffLogin(staff_id)== LoginResult.LOGIN_SUCCESS;
    }

    public static int doAdminLogin(String account_name,String passwd){
        AccountManager accountManager=ManagerFactory.getAccountManager();
        return accountManager.doAdminLogin(account_name,passwd);
    }

    public static boolean doLogout(){
        AppStatus appStatuss = AppStatus.instance();
        appStatuss.setIsLogin(false);
        return true;
    }

    public static List<Admin> getAdminList(){
        AccountManager accountManager=ManagerFactory.getAccountManager();
        return accountManager.getAllAdminFromDb();
    }

    public static List<Staff> getStaffList(){
        AccountManager accountManager=ManagerFactory.getAccountManager();
        return accountManager.getAllStaffFromDb();
    }
}
