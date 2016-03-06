package com.ylj.db.account;

import com.ylj.common.bean.Admin;
import com.ylj.common.bean.Staff;
import com.ylj.db.AbstractManager;
import com.ylj.db.IRequestListener;

import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/3/6 0006.
 */
public class AccountManager extends AbstractManager implements IAccountManager {

    @Override
    public void doStaffLogin(final long staff_id, final OnLoginListener listener) {
        runInBackground(new Runnable() {
            @Override
            public void run() {
                Staff s =null;
                try {
                    s = findStaffById(staff_id);
                } catch (DbException e) {
                    e.printStackTrace();
                    //// TODO: 2016/3/6 0006 出错处理
                }
                final Staff staff = s;
                postToUi(new Runnable() {
                    @Override
                    public void run() {
                        if (staff == null) {
                            listener.onLoginFail(1);
                        } else {
                            listener.onLoginSuccess(false, staff.getId(), staff.getStaffName());
                        }
                    }
                });

            }
        });
    }

    @Override
    public void doAdminLogin(String account_name, String passwd, OnLoginListener listener) {

    }

    @Override
    public void getAdmin(long admin_id, IRequestListener<Admin> listener) {

    }

    private Admin findAdminById(long admin_id) throws DbException {
        //// TODO: 2016/3/6 0006 不抛异常 返回null
        return null;
    }

    @Override
    public void getStaff(long staff_id, IRequestListener<Staff> listener) {

    }

    private Staff findStaffById(long staff_id) throws DbException {
        return null;
    }

    @Override
    public void getAdminList(IRequestListener<List<Admin>> listener) {

    }

    @Override
    public void getStaffList(IRequestListener<List<Staff>> listener) {

    }
}
