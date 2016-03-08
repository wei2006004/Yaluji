package com.ylj.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/6 0006.
 */
@Table(name = "admin")
public class Admin implements Parcelable, IMapable {

    public static final String TAG_ID = "id";
    public static final String TAG_ADMIN_NAME = "admin_name";
    public static final String TAG_COMPANY = "company";
    public static final String TAG_GROUP = "group";
    public static final String TAG_ACCOUNT_NAME = "account_name";
    public static final String TAG_PASSWD = "passwd";

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "admin_name")
    private String adminName = "";

    @Column(name = "company")
    private String company;

    @Column(name = "group")
    private String group;

    @Column(name = "account_name")
    private String accountName = "";

    @Column(name = "passwd")
    private String passwd;

    @Override
    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(TAG_ID, id);
        map.put(TAG_COMPANY, company);
        map.put(TAG_GROUP, group);
        map.put(TAG_ADMIN_NAME, adminName);
        map.put(TAG_ACCOUNT_NAME, accountName);
        map.put(TAG_PASSWD, passwd);
        return map;
    }

    public static Admin createByMap(Map<String, Object> map) {
        Admin admin = new Admin();
        if (map == null)
            return admin;
        admin.id = Integer.parseInt(map.get(TAG_ID).toString());
        admin.adminName = map.get(TAG_ADMIN_NAME).toString();
        admin.group = map.get(TAG_GROUP).toString();
        admin.company = map.get(TAG_COMPANY).toString();
        admin.accountName = map.get(TAG_ACCOUNT_NAME).toString();
        admin.passwd = map.get(TAG_PASSWD).toString();
        return admin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(adminName);
        dest.writeString(company);
        dest.writeString(group);
        dest.writeString(accountName);
        dest.writeString(passwd);
    }

    public static final Parcelable.Creator<Admin> CREATOR = new Creator<Admin>() {

        @Override
        public Admin createFromParcel(Parcel source) {
            Admin admin = new Admin();
            admin.id = source.readInt();
            admin.adminName = source.readString();
            admin.company = source.readString();
            admin.group = source.readString();
            admin.accountName = source.readString();
            admin.passwd = source.readString();
            return admin;
        }

        @Override
        public Admin[] newArray(int size) {
            return new Admin[size];
        }
    };

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
