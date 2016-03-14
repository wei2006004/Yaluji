package com.ylj.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.Date;


/**
 * Created by Administrator on 2016/3/5 0005.
 */
@Table(name="Test")
public class Test implements Parcelable{
    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "task_id")
    private int taskId;

    @Column(name = "is_login")
    private boolean isLogin = false;

    @Column(name = "is_admin")
    private boolean isAdmin = false;

    @Column(name = "staff_id")
    private int staffId;

    @Column(name = "admin_id")
    private int adminId;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "start_date")
    private java.sql.Date startDate;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "end_date")
    private java.sql.Date endDate;

    @Column(name = "total_time")
    private Date totalTime;

    @Column(name = "record_file")
    private String recordFile;

    public Test(){}

    protected Test(Parcel in) {
        id = in.readInt();
        taskId = in.readInt();
        isLogin = in.readByte() != 0;
        isAdmin = in.readByte() != 0;
        staffId = in.readInt();
        adminId = in.readInt();
        recordFile = in.readString();
    }

    public static final Creator<Test> CREATOR = new Creator<Test>() {
        @Override
        public Test createFromParcel(Parcel in) {
            return new Test(in);
        }

        @Override
        public Test[] newArray(int size) {
            return new Test[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecordFile() {
        return recordFile;
    }

    public void setRecordFile(String recordFile) {
        this.recordFile = recordFile;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        setStartDate(new java.sql.Date(startTime.getTime()));
        this.startTime = startTime;
    }

    public java.sql.Date getStartDate() {
        return startDate;
    }

    private void setStartDate(java.sql.Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        setEndDate(new java.sql.Date(endTime.getTime()));
        this.endTime = endTime;
    }

    public java.sql.Date getEndDate() {
        return endDate;
    }

    private void setEndDate(java.sql.Date endDate) {
        this.endDate = endDate;
    }

    public Date getTotalTime() {
        return totalTime;
    }

    public void countTotalTime() {
        this.totalTime = new Date(endTime.getTime()-startTime.getTime());
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(taskId);
        dest.writeByte((byte) (isLogin ? 1 : 0));
        dest.writeByte((byte) (isAdmin ? 1 : 0));
        dest.writeInt(staffId);
        dest.writeInt(adminId);
        dest.writeString(recordFile);
    }
}
