package com.ylj.daemon.connect;

import android.util.Log;

import com.ylj.daemon.config.ConnectState;

import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public class DebugConnector extends BaseConnector {

    int mState = ConnectState.STATE_NONE;

    @Override
    public void connect() {
        mState = ConnectState.STATE_CONNECTED;
        mListener.onStateChange(mState);
    }

    @Override
    public void disconnect() {
        mState = ConnectState.STATE_NONE;
        mListener.onStateChange(mState);
    }

    @Override
    public boolean isConnect() {
        return mState == ConnectState.STATE_CONNECTED;
    }

    @Override
    public void sendDeviceMessage() {
        super.sendDeviceMessage();
        final String info = createDeviceInfoMsg();
        x.task().postDelayed(new Runnable() {
            @Override
            public void run() {
                onMessageArrive(info);
            }
        }, 500);
    }

    public static final char START_FLAG = 0x02;
    public static final char END_FLAG = 0x04;

    private String createDeviceInfoMsg() {
        String text = "<DeviceInfo>" +                  //状态信息报文标签
                "<deviceId>DNSE00628</deviceId>" +            //硬件板
                "<version>VERSION 1.0.2</version>" +             //硬件版本
                "<softVersion>VERSION 2.0.1</softVersion>" +       //硬件板程序版本
                "<productDate>2015-12-30</productDate>" +    //硬件板生产日期
                "<huoerState>normal</huoerState>" +     //霍尔传感器状态
                "<dirState>normal</dirState>" +         //电子罗盘状态
                "<tempState>normal</tempState>" +       //温度传感器状态
                "<quakeState>normal</quakeState>" +     //振动传感器状态
                "</DeviceInfo>";
        return "" + START_FLAG + text + END_FLAG;
    }

    boolean mIsRun = false;
    Timer mTimer = new Timer();

    @Override
    public void sendStartMessage() {
        super.sendStartMessage();
        mIsRun = true;
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mIsRun) {
                    final String data = createDeviceDataMsg();
                    onMessageArrive(data);
                } else {
                    mTimer.cancel();
                }
            }
        }, 500, 500);
    }

    private String createDeviceDataMsg() {
        String text = "<DeviceData>" +                      //实时采集数据报文标签
                "<quake>12.3</quake>" +                     //振动加速度数据
                "<temp>80.4</temp>" +                       //温度数据
                "<pulse>39</pulse>" +                       //位移脉冲数据
                "<speed>2.1</speed>" +                      //速度脉冲数据
                "<state>head</state>" +                     //前进、停止和后退状态
                "<compassPitch>36</compassPitch>" +         //电子罗盘Pitch方向
                "<compassRoll>34</compassRoll>" +           //电子罗盘Roll方向
                "<compassHeading>23</compassHeading>" +     //电子罗盘Heading方向
                "</DeviceData>";
        return "" + START_FLAG + text + END_FLAG;
    }

    @Override
    public void sendStopMessage() {
        super.sendStopMessage();
        mIsRun = false;
    }

    @Override
    public void sendMessage(String msg) {
        Log.d("Debug$sendMessage", "message:" + msg);
    }

    OnStateChangeListener mListener;

    @Override
    public void setOnStateChangeListener(OnStateChangeListener listener) {
        mListener = listener;
    }
}
