package com.ylj.task;

import com.ylj.daemon.bean.DeviceData;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public interface ITestCtrl {

    void startTest();
    void stopTest();

    void addTestCtrlListener(OnTestCtrlListener listener);
    void deleteTestCtrlListener(OnTestCtrlListener listener);

    interface OnTestCtrlListener {
        void onTestStart();
        void onTestStop();
        void onTestRefresh(DeviceData data);
    }
}
