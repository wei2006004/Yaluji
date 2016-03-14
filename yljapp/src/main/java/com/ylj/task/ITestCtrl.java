package com.ylj.task;

import com.ylj.task.bean.DeviceData;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public interface ITestCtrl {

    void startTest();
    void stopTest();

    void addTestCtrlListener(OnCtrlLister listener);
    void deleteTestCtrlListener(OnCtrlLister listener);

    interface OnCtrlLister {
        void onTestStart();
        void onTestStop();
        void onTestRefresh(DeviceData data);
    }
}
