package com.ylj.db.account;

import com.ylj.common.bean.Admin;
import com.ylj.common.bean.Staff;
import com.ylj.db.config.Constant;
import com.ylj.db.manager.AbstractDbManager;
import com.ylj.db.IRequestListener;

import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by Administrator on 2016/3/6 0006.
 */
public class AccountManager extends AbstractDbManager implements IAccountManager {

    public AccountManager(String dbName) {
        super(dbName);
    }

    @Override
    public void doStaffLogin(final long staff_id, final IRequestListener<Staff> listener) {
        runInBackground(new Runnable() {
            @Override
            public void run() {
                final Staff staff = findStaffById(staff_id);
                postToUi(new Runnable() {
                    @Override
                    public void run() {
                        if (staff == null) {
                            listener.onRequestFail(LoginError.LOGIN_ERROR_NO_ACCOUNT);
                        } else {
                            listener.onRequestSuccess(staff);
                        }
                    }
                });

            }
        });
    }

    @Override
    public void doAdminLogin(final String account_name, final String passwd, final IRequestListener<Admin> listener) {
        runInBackground(new Runnable() {
            @Override
            public void run() {
                final Admin admin = findAdminByAccountName(account_name);
                postToUi(new Runnable() {
                    @Override
                    public void run() {
                        if (admin == null) {
                            listener.onRequestFail(LoginError.LOGIN_ERROR_NO_ACCOUNT);
                        } else {
                            if (admin.getPasswd().equals(passwd)) {
                                listener.onRequestSuccess(admin);
                            } else {
                                listener.onRequestFail(LoginError.LOGIN_ERROR_WRONG_PASSWD);
                            }
                        }
                    }
                });
            }
        });
    }

    private Admin findAdminByAccountName(String accountName) {
        Admin admin = null;
        try {
            admin = db.selector(Admin.class).
                    where(Admin.ACCOUNT_NAME_TAG, Constant.SQL_OP_EQUAL, accountName).
                    findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return admin;
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

    @Override
    public void getAdminList(final IRequestListener<List<Admin>> listener) {
        runInBackground(new Runnable() {
            @Override
            public void run() {
                final List<Admin> list= getAllAdminFromDb();
                postToUi(new Runnable() {
                    @Override
                    public void run() {
                        listener.onRequestSuccess(list);
                    }
                });
            }
        });
    }

    private List<Admin> getAllAdminFromDb(){
        List<Admin> list=null;
        try {
            list=db.findAll(Admin.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void getStaffList(final IRequestListener<List<Staff>> listener) {
        runInBackground(new Runnable() {
            @Override
            public void run() {
                final List<Staff> list= getAllStaffFromDb();
                postToUi(new Runnable() {
                    @Override
                    public void run() {
                        listener.onRequestSuccess(list);
                    }
                });
            }
        });
    }

    @Override
    public void addStaff(Staff staff) {
        try {
            db.save(staff);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addAdmin(Admin admin) {
        try {
            db.save(admin);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private List<Staff> getAllStaffFromDb(){
        List<Staff> list=null;
        try {
            list=db.findAll(Staff.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }
}
