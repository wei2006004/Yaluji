package com.ylj.daemon.msghandler;

import com.ylj.connect.bean.DeviceInfo;
import com.ylj.daemon.bean.DeviceData;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public interface IMessageHandler {
    void onHandleMessage(String msg);

    interface OnHandleListener {
        void onHandleDeviceInfo(DeviceInfo info);

        void onHandleDeviceData(DeviceData data);

        void onHandleWrongMessage(String msg);
    }
}
