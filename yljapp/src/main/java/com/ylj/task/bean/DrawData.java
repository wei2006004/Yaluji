package com.ylj.task.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.ylj.daemon.bean.Record;

/**
 * Created by Administrator on 2016/3/17 0017.
 */
public class DrawData implements Parcelable{

    ColorData colorData;
    TraceData traceData;
    Record record;

    public DrawData(){}

    protected DrawData(Parcel in) {
        colorData = in.readParcelable(ColorData.class.getClassLoader());
        traceData = in.readParcelable(TraceData.class.getClassLoader());
        record = in.readParcelable(Record.class.getClassLoader());
    }

    public static final Creator<DrawData> CREATOR = new Creator<DrawData>() {
        @Override
        public DrawData createFromParcel(Parcel in) {
            return new DrawData(in);
        }

        @Override
        public DrawData[] newArray(int size) {
            return new DrawData[size];
        }
    };

    public ColorData getColorData() {
        return colorData;
    }

    public void setColorData(ColorData colorData) {
        this.colorData = colorData;
    }

    public TraceData getTraceData() {
        return traceData;
    }

    public void setTraceData(TraceData traceData) {
        this.traceData = traceData;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(colorData, flags);
        dest.writeParcelable(traceData, flags);
        dest.writeParcelable(record, flags);
    }
}
