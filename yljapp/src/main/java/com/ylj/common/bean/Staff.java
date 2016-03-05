package com.ylj.common.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/3/5 0005.
 */
@Table(name="staff")
public class Staff {
    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "staff_name")
    private String staffName="";

    @Column(name = "company")
    private String company;

    @Column(name = "group")
    private String group;

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
