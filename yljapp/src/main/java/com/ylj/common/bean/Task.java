package com.ylj.common.bean;

/**
 * Created by Administrator on 2016/3/4 0004.
 */

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name="task")
public class Task {
    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "task_name")
    private String taskName="";

    @Column(name = "road_name")
    private String roadName="";

    public final static int ORIGIN_CLOCKWISE=0;
    public final static int ORIGIN_ANTICLOCKWISE=1;

    @Column(name = "origin")
    private int origin=ORIGIN_ANTICLOCKWISE;

    @Column(name = "road_width")
    private double roadWidth;

    @Column(name = "road_length")
    private double roadLength;

    @Column(name = "roller_width")
    private double rollerWidth;

    @Column(name = "roller_diameter")
    private double rollerDiameter;

    @Column(name = "huoer_num")
    private int huoerNum;

    @Column(name = "vcv")
    private double VCV;

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
}
