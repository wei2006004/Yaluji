package com.ylj.daemon.manager;

import android.graphics.Color;

import com.ylj.task.bean.ColorData;

import java.util.List;

/**
 * Created by Administrator on 2016/3/17 0017.
 */
public class ColorCalculatorImpl implements IColorCalculator {

    private double mRoadLength = 50;
    private double mRoadWidth = 30;

    private int mRowNum = 8;
    private int mColumnNum = 15;

    private ColorData[][] mColorDatas;

    private IColorConvertor mColorConvertor;

    @Override
    public void setRoad(double roadWidth, double roadLength) {
        mRoadWidth = roadWidth;
        mRoadLength = roadLength;
    }

    @Override
    public void setColorConvertor(IColorConvertor colorConvertor) {
        mColorConvertor = colorConvertor;
    }

    @Override
    public void setGrid(int row, int column) {
        mRowNum = row;
        mColumnNum = column;
    }

    @Override
    public int getRow() {
        return mRowNum;
    }

    @Override
    public int getColumn() {
        return mColumnNum;
    }

    @Override
    public ColorData addData(double posX, double posY, double value) {
        int row = 0, column = 0;
        column = (int) (posX / (mRoadLength / mColumnNum));
        row = (int) (posY / (mRoadWidth / mRowNum));

        if (mColorDatas == null) {
            initColorDatas();
        }
        return addGridData(row, column, value);
    }

    private ColorData addGridData(int row, int column, double value) {
        ColorData colorData = getColorData(row, column);
        if (colorData == null)
            return null;
        double sum = colorData.getValue() * colorData.getCount() + value;
        double newValue = sum / (colorData.getCount() + 1);
        int color = mColorConvertor.convertToColor(newValue);

        colorData.setValue(newValue);
        colorData.setColor(color);
        colorData.setCount(colorData.getCount() + 1);

        return colorData;
    }

    public ColorData getColorData(int row, int column) {
        if ((row < 0 || row >= mRowNum) || (column < 0 || column >= mColumnNum))
            return null;
        return mColorDatas[row][column];
    }

    private void setColorData(int row, int column, ColorData data) {
        mColorDatas[row][column] = data;
    }

    private void initColorDatas() {
        mColorDatas = new ColorData[mRowNum][mColumnNum];
        ColorData colorData;
        for (int i = 0; i < mRowNum; i++) {
            for (int j = 0; j < mColumnNum; j++) {
                colorData = new ColorData(i, j, Color.WHITE, 0, 0.0);
                mColorDatas[i][j] = colorData;
            }
        }
    }

    @Override
    public void addColorDatas(List<ColorData> dataList) {
        if (mColorDatas == null) {
            initColorDatas();
        }
        for (ColorData data : dataList) {
            addColorData(data);
        }
    }

    private void addColorData(ColorData data) {
        setColorData(data.getRow(), data.getColumn(), data);
    }
}
