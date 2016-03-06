package com.ylj.common.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/3/6 0006.
 */
@Table(name="admin")
public class Admin {
    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "admin_name")
    private String adminName="";

    @Column(name = "company")
    private String company;

    @Column(name = "group")
    private String group;

    @Column(name = "account_name")
    private String accountName="";

    @Column(name = "passwd")
    private String passwd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
