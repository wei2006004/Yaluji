package com.ylj.daemon.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
@Table(name = "task_result")
public class TaskResult implements Parcelable{

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "notPassNum")
    private int notPassNum;

    @Column(name = "passNum")
    private int passNum;

    @Column(name = "goodNum")
    private int goodNum;

    @Column(name = "excellentNum")
    private int excellentNum;

    @Column(name = "noReachNum")
    private int noReachNum;

    @Column(name = "row")
    private int row;

    @Column(name = "column")
    private int column;

    public TaskResult(){}

    protected TaskResult(Parcel in) {
        id = in.readInt();
        notPassNum = in.readInt();
        passNum = in.readInt();
        goodNum = in.readInt();
        excellentNum = in.readInt();
        noReachNum = in.readInt();
        row = in.readInt();
        column = in.readInt();
    }

    public static final Creator<TaskResult> CREATOR = new Creator<TaskResult>() {
        @Override
        public TaskResult createFromParcel(Parcel in) {
            return new TaskResult(in);
        }

        @Override
        public TaskResult[] newArray(int size) {
            return new TaskResult[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNotPassNum() {
        return notPassNum;
    }

    public void setNotPassNum(int notPassNum) {
        this.notPassNum = notPassNum;
    }

    public int getPassNum() {
        return passNum;
    }

    public void setPassNum(int passNum) {
        this.passNum = passNum;
    }

    public int getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(int goodNum) {
        this.goodNum = goodNum;
    }

    public int getExcellentNum() {
        return excellentNum;
    }

    public void setExcellentNum(int excellentNum) {
        this.excellentNum = excellentNum;
    }

    public int getNoReachNum() {
        return noReachNum;
    }

    public void setNoReachNum(int noReachNum) {
        this.noReachNum = noReachNum;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(notPassNum);
        dest.writeInt(passNum);
        dest.writeInt(goodNum);
        dest.writeInt(excellentNum);
        dest.writeInt(noReachNum);
        dest.writeInt(row);
        dest.writeInt(column);
    }
}
