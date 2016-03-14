package com.ylj.common.bean;

/**
 * Created by Administrator on 2016/3/4 0004.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.ylj.common.bean.def.TaskDefault;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.HashMap;
import java.util.Map;

@Table(name = "task")
public class Task implements Parcelable, IMapable {

    public static final String TAG_ID = "id";
    public static final String TAG_TASK_NAME = "task_name";
    public static final String TAG_ROAD_NAME = "road_name";
    public static final String TAG_ORIGIN = "origin";
    public static final String TAG_ROAD_WIDTH = "road_width";
    public static final String TAG_ROAD_LENGTH = "road_length";
    public static final String TAG_ROLLER_WIDTH = "roller_width";
    public static final String TAG_ROLLER_DIAMETER = "roller_diameter";
    public static final String TAG_HUOER_NUM = "huoer_num";
    public static final String TAG_VCV = "vcv";
    public static final String TAG_IS_ADJUST = "is_adjust";
    public static final String TAG_IS_TEST = "is_test";
    public static final String TAG_IS_FINISH = "is_finish";
    public static final String TAG_IS_CREATE_RESULT = "is_create_result";
    public static final String TAG_RESULT_ID = "result_id";

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "task_name")
    private String taskName = TaskDefault.DEFAULT_TASK_NAME;

    @Column(name = "road_name")
    private String roadName = TaskDefault.DEFAULT_ROAD_NAME;

    public final static int ORIGIN_CLOCKWISE = 0;
    public final static int ORIGIN_ANTICLOCKWISE = 1;

    @Column(name = "origin")
    private int origin = TaskDefault.DEFAULT_ORIGIN;

    @Column(name = "road_width")
    private double roadWidth = TaskDefault.DEFAULT_ROAD_WIDTH;

    @Column(name = "road_length")
    private double roadLength = TaskDefault.DEFAULT_ROAD_LENGTH;

    @Column(name = "roller_width")
    private double rollerWidth = TaskDefault.DEFAULT_ROLLER_WIDTH;

    @Column(name = "roller_diameter")
    private double rollerDiameter = TaskDefault.DEFAULT_ROLLER_DIAMETER;

    @Column(name = "huoer_num")
    private int huoerNum = TaskDefault.DEFAULT_HUOER_NUM;

    @Column(name = "vcv")
    private double VCV;

    @Column(name = "is_adjust")
    private boolean isAdjust = false;

    @Column(name = "is_finish")
    private boolean isFinish = false;

    @Column(name = "is_test")
    private boolean isTest = false;

    @Column(name = "is_create_result")
    private boolean isCreateResult = false;

    @Column(name = "result_id")
    private int resultId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(TAG_ID, id);
        map.put(TAG_TASK_NAME, taskName);
        map.put(TAG_ROAD_NAME, roadName);
        map.put(TAG_ORIGIN, origin);
        map.put(TAG_ROAD_WIDTH, roadWidth);
        map.put(TAG_ROAD_LENGTH, roadLength);
        map.put(TAG_ROLLER_WIDTH, rollerWidth);
        map.put(TAG_ROLLER_DIAMETER, rollerDiameter);
        map.put(TAG_HUOER_NUM, huoerNum);
        map.put(TAG_VCV, VCV);
        map.put(TAG_IS_ADJUST, isAdjust);
        map.put(TAG_IS_TEST, isTest);
        map.put(TAG_IS_FINISH, isFinish);
        map.put(TAG_IS_CREATE_RESULT, isCreateResult);
        map.put(TAG_RESULT_ID, resultId);
        return map;
    }

    public static Task createByMap(Map<String, Object> map) {
        Task task = new Task();
        task.id = Integer.parseInt(map.get(TAG_ID).toString());
        task.taskName = map.get(TAG_TASK_NAME).toString();
        task.roadName = map.get(TAG_ROAD_NAME).toString();
        task.origin = Integer.parseInt(map.get(TAG_ORIGIN).toString());
        task.roadWidth = Double.parseDouble(map.get(TAG_ROAD_WIDTH).toString());
        task.roadLength = Double.parseDouble(map.get(TAG_ROAD_LENGTH).toString());
        task.rollerWidth = Double.parseDouble(map.get(TAG_ROLLER_WIDTH).toString());
        task.rollerDiameter = Double.parseDouble(map.get(TAG_ROLLER_DIAMETER).toString());
        task.huoerNum = Integer.parseInt(map.get(TAG_HUOER_NUM).toString());
        task.VCV = Double.parseDouble(map.get(TAG_VCV).toString());
        task.isAdjust = Boolean.parseBoolean(map.get(TAG_IS_ADJUST).toString());
        task.isTest = Boolean.parseBoolean(map.get(TAG_IS_TEST).toString());
        task.isFinish = Boolean.parseBoolean(map.get(TAG_IS_FINISH).toString());
        task.isCreateResult = Boolean.parseBoolean(map.get(TAG_IS_CREATE_RESULT).toString());
        task.resultId = Integer.parseInt(map.get(TAG_RESULT_ID).toString());
        return task;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(taskName);
        dest.writeString(roadName);
        dest.writeInt(origin);
        dest.writeDouble(roadWidth);
        dest.writeDouble(roadLength);
        dest.writeDouble(rollerWidth);
        dest.writeDouble(rollerDiameter);
        dest.writeInt(huoerNum);
        dest.writeDouble(VCV);
        dest.writeBooleanArray(new boolean[]{isAdjust, isTest, isFinish, isCreateResult});
        dest.writeInt(resultId);
    }

    public static final Parcelable.Creator<Task> CREATOR = new Creator<Task>() {

        @Override
        public Task createFromParcel(Parcel source) {
            Task task = new Task();
            task.id = source.readInt();
            task.taskName = source.readString();
            task.roadName = source.readString();
            task.origin = source.readInt();
            task.roadWidth = source.readDouble();
            task.roadLength = source.readDouble();
            task.rollerWidth = source.readDouble();
            task.rollerDiameter = source.readDouble();
            task.huoerNum = source.readInt();
            task.VCV = source.readDouble();
            boolean flags[] = new boolean[4];
            source.readBooleanArray(flags);
            task.isAdjust = flags[0];
            task.isTest = flags[1];
            task.isFinish = flags[2];
            task.isCreateResult = flags[3];
            task.resultId = source.readInt();
            return task;
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public double getRoadWidth() {
        return roadWidth;
    }

    public void setRoadWidth(double roadWidth) {
        this.roadWidth = roadWidth;
    }

    public double getRoadLength() {
        return roadLength;
    }

    public void setRoadLength(double roadLength) {
        this.roadLength = roadLength;
    }

    public double getRollerWidth() {
        return rollerWidth;
    }

    public void setRollerWidth(double rollerWidth) {
        this.rollerWidth = rollerWidth;
    }

    public double getRollerDiameter() {
        return rollerDiameter;
    }

    public void setRollerDiameter(double rollerDiameter) {
        this.rollerDiameter = rollerDiameter;
    }

    public int getHuoerNum() {
        return huoerNum;
    }

    public void setHuoerNum(int huoerNum) {
        this.huoerNum = huoerNum;
    }

    public double getVCV() {
        return VCV;
    }

    public void setVCV(double VCV) {
        this.VCV = VCV;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setIsFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }

    public boolean isCreateResult() {
        return isCreateResult;
    }

    public void setIsCreateResult(boolean isCreateResult) {
        this.isCreateResult = isCreateResult;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public boolean isAdjust() {
        return isAdjust;
    }

    public void setIsAdjust(boolean isAdjust) {
        this.isAdjust = isAdjust;
    }

    public boolean isTest() {
        return isTest;
    }

    public void setIsTest(boolean isTest) {
        this.isTest = isTest;
    }
}
