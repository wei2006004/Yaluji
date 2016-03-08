package com.ylj.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2016/3/5 0005.
 */
@Table(name = "staff")
public class Staff implements Parcelable, IMapable {

    public static final String TAG_ID = "id";
    public static final String TAG_STAFF_NAME = "staff_name";
    public static final String TAG_COMPANY = "company";
    public static final String TAG_GROUP = "group";

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "staff_name")
    private String staffName = "";

    @Column(name = "company")
    private String company;

    @Column(name = "group")
    private String group;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(staffName);
        dest.writeString(company);
        dest.writeString(group);
    }

    public static final Parcelable.Creator<Staff> CREATOR = new Creator<Staff>() {

        @Override
        public Staff createFromParcel(Parcel source) {
            Staff staff = new Staff();
            staff.id = source.readInt();
            staff.staffName = source.readString();
            staff.company = source.readString();
            staff.group = source.readString();
            return staff;
        }

        @Override
        public Staff[] newArray(int size) {
            return new Staff[size];
        }
    };

    @Override
    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(TAG_ID, id);
        map.put(TAG_COMPANY, company);
        map.put(TAG_GROUP, group);
        map.put(TAG_STAFF_NAME, staffName);
        return map;
    }

    public static Staff createByMap(Map<String, Object> map) {
        Staff staff = new Staff();
        if (map == null)
            return staff;
        staff.id = Integer.parseInt(map.get(TAG_ID).toString());
        staff.staffName = map.get(TAG_STAFF_NAME).toString();
        staff.group = map.get(TAG_GROUP).toString();
        staff.company = map.get(TAG_COMPANY).toString();
        return staff;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
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
