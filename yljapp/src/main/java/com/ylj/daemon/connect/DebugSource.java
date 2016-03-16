package com.ylj.daemon.connect;

import com.ylj.task.bean.DeviceData;

import java.util.Random;

public class DebugSource extends DeviceData {
    public final static int MAX_ADVALUE = 50000;

    public void start() {
        speed = (float)0.2;
        state = STATE_HEADING;
        pulse = 0;
        compassHeading = 0;
    }

    public void stop() {
    }

    public boolean refresh() {
        Random random = new Random();
        pulse+=speed;
        compassHeading = random.nextInt(5);

        temp = (int) (random.nextFloat() * MAX_ADVALUE);
        quake = (int) (random.nextFloat() * MAX_ADVALUE);
        return true;
    }
}
