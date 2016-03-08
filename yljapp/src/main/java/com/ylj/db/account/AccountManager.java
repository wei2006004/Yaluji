package com.ylj.db.account;

import com.ylj.common.bean.Admin;
import com.ylj.common.bean.Staff;
import com.ylj.db.config.Constant;
import com.ylj.db.AbstractDbManager;

import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by Administrator on 2016/3/6 0006.
 */
public class AccountManager extends AbstractDbManager {

    public AccountManager(String dbName) {
        super(dbName);
    }

    public int doStaffLogin(final long staff_id) {
        Staff staff = findStaffById(staff_id);
        if (staff == null) {
            return LoginResult.LOGIN_ERROR_NO_ACCOUNT;
        }
        return LoginResult.LOGIN_SUCCESS;
    }

    private Staff findStaffById(long staff_id) {
        Staff staff = null;
        try {
            staff = db.findById(Staff.class, staff_id);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return staff;
    }

    public int doAdminLogin(String account_name, String passwd) {
        Admin admin = findAdminByAccountName(account_name);
        if (admin == null) {
            return LoginResult.LOGIN_ERROR_NO_ACCOUNT;
        }
        if (admin.getPasswd().equals(passwd)) {
            return LoginResult.LOGIN_ERROR_WRONG_PASSWD;
        }
        return LoginResult.LOGIN_SUCCESS;
    }

    private Admin findAdminByAccountName(String accountName) {
        Admin admin = null;
        try {
            admin = db.selector(Admin.class).
                    where(Admin.TAG_ACCOUNT_NAME, Constant.SQL_OP_EQUAL, accountName).
                    findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return admin;
    }

    public List<Admin> getAllAdminFromDb() {
        List<Admin> list = null;
        try {
            list = db.findAll(Admin.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Staff> getAllStaffFromDb() {
        List<Staff> list = null;
        try {
            list = db.findAll(Staff.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addStaff(Staff staff) {
        try {
            db.save(staff);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void addAdmin(Admin admin) {
        try {
            db.save(admin);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void deleteAdmin(Admin admin) {
        try {
            db.delete(admin);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void deleteStaff(Staff staff) {
        try {
            db.delete(staff);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void updateStaff(Staff staff) {
        try {
            db.update(staff);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void updateAdmin(Admin admin) {
        try {
            db.update(admin);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
