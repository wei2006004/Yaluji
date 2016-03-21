package com.ylj.daemon.manager;

import android.graphics.Color;

import com.ylj.task.bean.ColorData;

/**
 * Created by Administrator on 2016/3/17 0017.
 */
public class ColorConvertorImpl implements IColorConvertor {

    private ILevelConvertor levelConvertor;

    public ColorConvertorImpl(ILevelConvertor levelConvertor) {
        this.levelConvertor = levelConvertor;
    }

    @Override
    public int convertToColor(double value) {
        int level = levelConvertor.convertToLever(value);
        switch (level){
            case ColorData.LEVEL_NONE:
                return Color.WHITE;
            case ColorData.LEVEL_EXCELLENT:
                return Color.GREEN;
            case ColorData.LEVEL_GOOD:
                return Color.YELLOW;
            case ColorData.LEVEL_NOT_PASS:
                return Color.RED;
            default:
                return Color.WHITE;
        }
    }
}
