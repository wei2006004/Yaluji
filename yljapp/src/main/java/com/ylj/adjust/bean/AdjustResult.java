package com.ylj.adjust.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/3/11 0011.
 */
public class AdjustResult implements Parcelable {
    public static final int ADJUST_POINT_NUM = 6;

    double[] quakes;
    double[] compactions;

    public AdjustResult() {
        quakes = new double[ADJUST_POINT_NUM];
        compactions = new double[ADJUST_POINT_NUM];
    }

    public void setData(int index, double quake, double compaction) {
        quakes[index] = quake;
        compactions[index] = compaction;
    }

    public double getQuake(int index){
        return quakes[index];
    }

    public double getCompaction(int index){
        return compactions[index];
    }

    protected AdjustResult(Parcel in) {
        quakes = in.createDoubleArray();
        compactions = in.createDoubleArray();
    }

    public static final Creator<AdjustResult> CREATOR = new Creator<AdjustResult>() {
        @Override
        public AdjustResult createFromParcel(Parcel in) {
            return new AdjustResult(in);
        }

        @Override
        public AdjustResult[] newArray(int size) {
            return new AdjustResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDoubleArray(quakes);
        dest.writeDoubleArray(compactions);
    }
}
