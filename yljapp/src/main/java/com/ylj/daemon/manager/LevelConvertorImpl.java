package com.ylj.daemon.manager;

import com.ylj.task.bean.ColorData;

/**
 * Created by Administrator on 2016/3/21 0021.
 */
public class LevelConvertorImpl implements ILevelConvertor {

    private double mVCV;

    public LevelConvertorImpl(double vcv) {
        mVCV = vcv;
    }

    @Override
    public int convertToLever(double value) {
        int level = ColorData.LEVEL_NONE;
        if (value > 0.95 * mVCV) {
            level = ColorData.LEVEL_EXCELLENT;
        } else if (value > 0.75 * mVCV) {
            level = ColorData.LEVEL_GOOD;
        } else if (value > 0.6 * mVCV) {
            level = ColorData.LEVEL_PASS;
        }else {
            level = ColorData.LEVEL_NOT_PASS;
        }
        return level;
    }
}
