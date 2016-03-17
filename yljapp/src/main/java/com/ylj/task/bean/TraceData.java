package com.ylj.task.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/3/17 0017.
 */
@Table(name = "trace_data")
public class TraceData  implements Parcelable{

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "postionY")
    double postionY;

    @Column(name = "postionX")
    double postionX;

    @Column(name = "direction")
    double direction;

    @Column(name = "color")
    int color;

    public TraceData(){}

    protected TraceData(Parcel in) {
        id = in.readInt();
        postionY = in.readDouble();
        postionX = in.readDouble();
        direction = in.readDouble();
        color = in.readInt();
    }

    public static final Creator<TraceData> CREATOR = new Creator<TraceData>() {
        @Override
        public TraceData createFromParcel(Parcel in) {
            return new TraceData(in);
        }

        @Override
        public TraceData[] newArray(int size) {
            return new TraceData[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPostionY() {
        return postionY;
    }

    public void setPostionY(double postionY) {
        this.postionY = postionY;
    }

    public double getPostionX() {
        return postionX;
    }

    public void setPostionX(double postionX) {
        this.postionX = postionX;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(postionY);
        dest.writeDouble(postionX);
        dest.writeDouble(direction);
        dest.writeInt(color);
    }
}
