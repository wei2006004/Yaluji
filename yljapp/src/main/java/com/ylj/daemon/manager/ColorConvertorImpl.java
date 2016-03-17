package com.ylj.daemon.manager;

import android.graphics.Color;

/**
 * Created by Administrator on 2016/3/17 0017.
 */
public class ColorConvertorImpl implements IColorConvertor {
    private double mVCV;

    public ColorConvertorImpl(double vcv) {
        mVCV = vcv;
    }

    @Override
    public int convertToColor(double value) {
        int color = Color.RED;
        if (value > 0.95 * mVCV) {
            color = Color.GREEN;
        } else if (value > 0.75 * mVCV) {
            color = Color.BLUE;
        } else if (value > 0.6 * mVCV) {
            color = Color.YELLOW;
        }
        return color;
    }
}
