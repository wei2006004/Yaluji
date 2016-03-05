package com.ylj.common.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.Date;


/**
 * Created by Administrator on 2016/3/5 0005.
 */
@Table(name="Test")
public class Test {
    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "task_id")
    private long taskId;

    @Column(name = "staff_id")
    private long staffId;

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

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getStaffId() {
        return staffId;
    }

    public void setStaffId(long staffId) {
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
}
