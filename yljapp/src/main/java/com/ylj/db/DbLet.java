package com.ylj.db;

import com.ylj.common.bean.Admin;
import com.ylj.common.bean.Staff;
import com.ylj.db.account.AccountManager;

import java.util.List;

/**
 * Created by Administrator on 2016/3/7 0007.
 */
public class DbLet {
    public static void addStaff(Staff staff) {
        AccountManager accountManager = ManagerFactory.getAccountManager();
        accountManager.addStaff(staff);
    }

    public static void addAdmin(Admin admin) {
        AccountManager accountManager = ManagerFactory.getAccountManager();
        accountManager.addAdmin(admin);
    }

    public static List<Admin> getAdminList() {
        AccountManager accountManager = ManagerFactory.getAccountManager();
        return accountManager.getAllAdminFromDb();
    }

    public static List<Staff> getStaffList() {
        AccountManager accountManager = ManagerFactory.getAccountManager();
        return accountManager.getAllStaffFromDb();
    }

    public static void deleteAdmin(Admin admin) {
        AccountManager accountManager = ManagerFactory.getAccountManager();
        accountManager.deleteAdmin(admin);
    }

    public static void deleteStaff(Staff staff) {
        AccountManager accountManager = ManagerFactory.getAccountManager();
        accountManager.deleteStaff(staff);
    }

    public static void updateStaff(Staff staff) {
        AccountManager accountManager = ManagerFactory.getAccountManager();
        accountManager.updateStaff(staff);
    }

    public static void updateAdmin(Admin admin) {
        AccountManager accountManager = ManagerFactory.getAccountManager();
        accountManager.updateAdmin(admin);
    }

    public static void saveOrUpdate(Admin admin) {
        AccountManager accountManager = ManagerFactory.getAccountManager();
        accountManager.saveOrUpdate
                (admin);
    }
}
