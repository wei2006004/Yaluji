package com.ylj.db.account;

import com.ylj.common.bean.Admin;
import com.ylj.common.bean.Staff;
import com.ylj.db.IRequestListener;

import java.util.List;

/**
 * Created by Administrator on 2016/3/6 0006.
 */
public interface IAccountManager {

    void doStaffLogin(final long staff_id,final IRequestListener<Staff> listener);

    void doAdminLogin(final String account_name,final String passwd,final IRequestListener<Admin> listener);

//    void getAdmin(final long admin_id,final IRequestListener<Admin> listener);
//
//    void getStaff(final long staff_id,final IRequestListener<Staff> listener);

    void getAdminList(final IRequestListener<List<Admin>> listener);

    void getStaffList(final IRequestListener<List<Staff>> listener);

    void addStaff(Staff staff);

    void addAdmin(Admin admin);

}
