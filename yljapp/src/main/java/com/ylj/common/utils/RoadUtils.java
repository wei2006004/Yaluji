package com.ylj.common.utils;

import android.util.Pair;

/**
 * Created by Administrator on 2016/3/17 0017.
 */
public class RoadUtils {

    public final static double DEFAULT_STEP = 5;

    public Pair<Integer, Integer> getRoadGrid(double roadWidth, double roadLength, double rollerWidth) {
        return getRoadGrid(roadWidth, roadLength, rollerWidth, DEFAULT_STEP);
    }

    public Pair<Integer, Integer> getRoadGrid(double roadWidth, double roadLength, double rollerWidth, double step) {
        int row = (int) (roadWidth / rollerWidth);
        int column = (int) (roadLength / step);
        return new Pair<>(row, column);
    }
}
