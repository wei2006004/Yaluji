package com.ylj.task;

import android.app.Activity;

import com.ylj.connect.ConnectControler;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public class TestControler extends ConnectControler implements ITestCtrl {

    public static TestControler newInstance(Activity activity, Class<?> cls) {
        return new TestControler(activity,cls);
    }

    public TestControler(Activity activity, Class<?> cls) {
        super(activity, cls);
    }

    @Override
    public void startTest() {

    }

    @Override
    public void stopTest() {

    }

    @Override
    public void addTestCtrlListener(OnCtrlLister listener) {

    }

    @Override
    public void deleteTestCtrlListener(OnCtrlLister listener) {

    }
}
