package com.ylj.daemon.manager;

import com.ylj.daemon.bean.TaskResult;
import com.ylj.task.bean.ColorData;

import java.util.List;

/**
 * Created by Administrator on 2016/3/17 0017.
 */
public interface IColorCalculator {
    void setRoad(double roadWidth,double roadLength);
    void setColorConvertor(IColorConvertor colorConvertor);
    void setGrid(int row,int column);

    int getRow();
    int getColumn();

    ColorData getColorData(int row,int column);

    ColorData addData(double posX,double posY,double value);

    void addColorDatas(List<ColorData> dataList);

    TaskResult resultCalculate();
}
