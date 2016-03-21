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

    public static final int VERIFY_SUCCESS = 0;

    public static final int VERIFY_ERROR_NO_ACCOUNT = 1;
    public static final int VERIFY_ERROR_WRONG_PASSWD = 2;
    public static final int VERIFY_ERROR_UNKNOW = 3;

    public AccountManager(String dbName) {
        super(dbName);
    }

    public Staff getStaffById(long staff_id) {
        Staff staff = null;
        try {
            staff = db.findById(Staff.class, staff_id);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return staff;
    }

    public Admin getAdminById(int adminId) {
        Admin admin = null;
        try {
            admin = db.findById(Admin.class, adminId);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return admin;
    }

    public int verifyAdmin(String account_name, String passwd) {
        Admin admin = getAdminByAccountName(account_name);
        if (admin == null) {
            return VERIFY_ERROR_NO_ACCOUNT;
        }
        if (admin.getPasswd().equals(passwd)) {
            return VERIFY_SUCCESS;
        }
        return VERIFY_ERROR_WRONG_PASSWD;
    }

    public Admin getAdminByAccountName(String accountName) {
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

    public void saveOrUpdate(Admin admin) {
        try {
            db.saveOrUpdate(admin);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public boolean hasStaff(Staff staff) {
        Staff newStaff = getStaffById(staff.getId());
        if (newStaff == null) {
            return false;
        }
        if(!staff.getStaffName().equals(newStaff.getStaffName())){
            return false;
        }
        return true;
    }


}
