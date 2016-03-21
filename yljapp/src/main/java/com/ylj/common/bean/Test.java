package com.ylj.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.ylj.common.utils.DateUtils;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2016/3/5 0005.
 */
@Table(name = "Test")
public class Test implements Parcelable,IMapable {

    public static final String TAG_ID = "id";
    public static final String TAG_TASK_ID = "task_id";
    public static final String TAG_IS_LOGIN = "is_login";
    public static final String TAG_IS_ADMIN = "is_admin";
    public static final String TAG_STAFF_ID = "staff_id";
    public static final String TAG_ADMIN_ID = "admin_id";
    public static final String TAG_START_TIME = "start_time";
    public static final String TAG_START_DATE = "start_date";
    public static final String TAG_END_TIME = "end_time";
    public static final String TAG_END_DATE = "end_date";
    public static final String TAG_TOTAL_TIME = "total_time";
    public static final String TAG_RECORD_FILE = "record_file";
    public static final String TAG_DISTANCE = "distance";
    public static final String TAG_LAST_POS_X = "lastPositionX";
    public static final String TAG_LAST_POS_Y = "lastPositionY";

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

    @Column(name = "distance")
    private double distance;

    @Column(name = "lastPositionX")
    private double lastPositionX;

    @Column(name = "lastPositionY")
    private double lastPositionY;

    public Test() {
    }

    protected Test(Parcel in) {
        id = in.readInt();
        taskId = in.readInt();
        isLogin = in.readByte() != 0;
        isAdmin = in.readByte() != 0;
        staffId = in.readInt();
        adminId = in.readInt();
        recordFile = in.readString();
        distance = in.readDouble();
        lastPositionX = in.readDouble();
        lastPositionY = in.readDouble();
        startTime = DateUtils.stringToTime(in.readString());
        endTime = DateUtils.stringToTime(in.readString());
        totalTime = DateUtils.stringToTime(in.readString());
        startDate = DateUtils.stringToSqlDate(in.readString());
        endDate = DateUtils.stringToSqlDate(in.readString());
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

    @Override
    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(TAG_ID, id);
        map.put(TAG_TASK_ID, taskId);
        map.put(TAG_IS_LOGIN, isLogin);
        map.put(TAG_IS_ADMIN, isAdmin);
        map.put(TAG_STAFF_ID, staffId);
        map.put(TAG_ADMIN_ID, adminId);
        map.put(TAG_START_TIME, DateUtils.timeToString(startTime));
        map.put(TAG_START_DATE, DateUtils.sqlDateToString(startDate));
        map.put(TAG_END_TIME, DateUtils.timeToString(endTime));
        map.put(TAG_END_DATE, DateUtils.sqlDateToString(endDate));
        map.put(TAG_TOTAL_TIME, DateUtils.timeToString(totalTime));
        map.put(TAG_RECORD_FILE, recordFile);
        map.put(TAG_DISTANCE, distance);
        map.put(TAG_LAST_POS_X, lastPositionX);
        map.put(TAG_LAST_POS_Y, lastPositionY);
        return map;
    }

    public static Test createByMap(Map<String, Object> map) {
        Test test = new Test();
        test.id = Integer.parseInt(map.get(TAG_ID).toString());
        test.taskId = Integer.parseInt(map.get(TAG_TASK_ID).toString());
        test.staffId = Integer.parseInt(map.get(TAG_STAFF_ID).toString());
        test.adminId = Integer.parseInt(map.get(TAG_ADMIN_ID).toString());
        test.isLogin = Boolean.parseBoolean(map.get(TAG_IS_LOGIN).toString());
        test.isAdmin = Boolean.parseBoolean(map.get(TAG_IS_ADMIN).toString());
        test.startTime = DateUtils.stringToTime(map.get(TAG_START_TIME).toString());
        test.endTime = DateUtils.stringToTime(map.get(TAG_END_TIME).toString());
        test.totalTime = DateUtils.stringToTime(map.get(TAG_TOTAL_TIME).toString());
        test.startDate = DateUtils.stringToSqlDate(map.get(TAG_START_DATE).toString());
        test.endDate = DateUtils.stringToSqlDate(map.get(TAG_END_DATE).toString());
        test.recordFile = map.get(TAG_RECORD_FILE).toString();
        test.distance = Double.parseDouble(map.get(TAG_DISTANCE).toString());
        test.lastPositionX = Double.parseDouble(map.get(TAG_LAST_POS_X).toString());
        test.lastPositionY = Double.parseDouble(map.get(TAG_LAST_POS_Y).toString());
        return test;
    }

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
        this.totalTime = new Date(endTime.getTime() - startTime.getTime());
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getLastPositionX() {
        return lastPositionX;
    }

    public void setLastPositionX(double lastPositionX) {
        this.lastPositionX = lastPositionX;
    }

    public double getLastPositionY() {
        return lastPositionY;
    }

    public void setLastPositionY(double lastPositionY) {
        this.lastPositionY = lastPositionY;
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
        dest.writeDouble(distance);
        dest.writeDouble(lastPositionX);
        dest.writeDouble(lastPositionY);
        dest.writeString(DateUtils.timeToString(startTime));
        dest.writeString(DateUtils.timeToString(endTime));
        dest.writeString(DateUtils.timeToString(totalTime));
        dest.writeString(DateUtils.sqlDateToString(startDate));
        dest.writeString(DateUtils.sqlDateToString(endDate));
    }
}
