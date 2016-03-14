package com.ylj.task.utils;

import com.ylj.task.bean.DeviceData;
import com.ylj.task.bean.Record;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public class DataConvertor {

    private static double TEMP_OFFSET = (-250.0 / 4);
    private static double QUAKE_OFFSET = 0;

    private static double TEMP_SCALE = (25.0 * 5000) / (65536.0 * 24 * 16);
    private static double QUAKE_SCALE = 1.0 / 4096;

    public static Record covertDeviceData2Record(double lastPosx, double lastPosy, double step, DeviceData mDataSource) {
        int state = mDataSource.getState();
        double distance = step * mDataSource.getPulse();
        double temp = mDataSource.getTemp() * TEMP_SCALE + TEMP_OFFSET;
        double quake = mDataSource.getQuake() * QUAKE_SCALE + QUAKE_OFFSET;
        double direction = mDataSource.getCompassHeading() / 100.0;

        double speed = step * mDataSource.getSpeed();
        double xspeed = speed * Math.cos(Math.toRadians(direction));
        double yspeed = speed * Math.sin(Math.toRadians(direction));

        if (state == DeviceData.STATE_BACKING) {
            xspeed = -xspeed;
            yspeed = -yspeed;
        }
        double posx = lastPosx + xspeed * step;
        double posy = lastPosy + yspeed * step;

        Record record=new Record();
        record.setState(state);
        record.setDirection(direction);
        record.setDistance(distance);
        record.setQuake(quake);
        record.setTemp(temp);
        record.setSpeed(speed);
        record.setPositionX(posx);
        record.setPositionY(posy);

        return record;
    }

}
