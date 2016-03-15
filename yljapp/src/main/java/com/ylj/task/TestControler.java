package com.ylj.task;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.ylj.connect.ConnectControler;
import com.ylj.daemon.YljService;
import com.ylj.task.bean.DeviceData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public class TestControler extends ConnectControler implements ITestCtrl {

    public final static String EXTRA_ACTION_FLAG = "EXTRA_ACTION_FLAG";
    public final static String EXTRA_TEST_DATA = "EXTRA_TEST_INFO";

    public final static int CTRL_FLAG_STOP = 0;
    public final static int CTRL_FLAG_START = 1;

    public final static String ACTION_TEST_CTRL = "ACTION_TEST_CTRL";
    public final static String ACTION_TEST_DATA = "ACTION_TEST_DATA";

    public static TestControler newInstance(Activity activity) {
        TestControler controler = new TestControler();
        controler.init(activity, YljService.class);
        return controler;
    }

    private BroadcastReceiver mTestReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(ACTION_TEST_CTRL)) {
                int flag = intent.getIntExtra(EXTRA_ACTION_FLAG, CTRL_FLAG_STOP);
                if (flag == CTRL_FLAG_STOP) {
                    for (OnTestCtrlListener listener : mTestListeners) {
                        listener.onTestStop();
                    }
                } else if (flag == CTRL_FLAG_START) {
                    for (OnTestCtrlListener listener : mTestListeners) {
                        listener.onTestStart();
                    }
                }
            } else if (action.equals(ACTION_TEST_DATA)) {
                DeviceData data = intent.getParcelableExtra(EXTRA_TEST_DATA);
                for (OnTestCtrlListener listener : mTestListeners) {
                    listener.onTestRefresh(data);
                }
            }
        }
    };

    @Override
    protected void onServiceConnected(IBinder binder) {
        super.onServiceConnected(binder);

        IntentFilter filter = new IntentFilter(ACTION_TEST_CTRL);
        mActivity.registerReceiver(mTestReceiver, filter);
        filter = new IntentFilter(ACTION_TEST_DATA);
        mActivity.registerReceiver(mTestReceiver, filter);
    }

    @Override
    public void release() {
        super.release();
        mActivity.unregisterReceiver(mTestReceiver);
        mTestListeners.clear();
    }

    @Override
    public void startTest() {
        if(!isConnect())
            return;
        mCleint.startTest();
    }

    @Override
    public void stopTest() {
        if(!isConnect())
            return;
        mCleint.stopTest();
    }

    private List<OnTestCtrlListener> mTestListeners = new ArrayList<>();

    @Override
    public void addTestCtrlListener(OnTestCtrlListener listener) {
        if (mTestListeners.contains(listener))
            return;
        mTestListeners.add(listener);
    }

    @Override
    public void deleteTestCtrlListener(OnTestCtrlListener listener) {
        if (mTestListeners.contains(listener)) {
            mTestListeners.remove(listener);
        }
    }
}
