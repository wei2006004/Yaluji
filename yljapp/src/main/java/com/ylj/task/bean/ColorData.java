package com.ylj.task.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/3/17 0017.
 */
@Table(name = "color_data")
public class ColorData implements Parcelable{

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "row")
    private int row;

    @Column(name = "column")
    private int column;

    @Column(name = "color")
    int color;

    @Column(name = "count")
    int count;

    @Column(name = "value")
    double value;

    public ColorData(){}

    public ColorData(int row, int column, int color, int count ,double value) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.count = count;
        this.value = value;
    }

    protected ColorData(Parcel in) {
        id = in.readInt();
        row = in.readInt();
        column = in.readInt();
        color = in.readInt();
        count = in.readInt();
        value = in.readDouble();
    }

    public static final Creator<ColorData> CREATOR = new Creator<ColorData>() {
        @Override
        public ColorData createFromParcel(Parcel in) {
            return new ColorData(in);
        }

        @Override
        public ColorData[] newArray(int size) {
            return new ColorData[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(row);
        dest.writeInt(column);
        dest.writeInt(color);
        dest.writeInt(count);
        dest.writeDouble(value);
    }
}
