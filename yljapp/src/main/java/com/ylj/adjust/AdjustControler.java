package com.ylj.adjust;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.ylj.common.utils.DataConvertor;
import com.ylj.connect.ConnectControler;
import com.ylj.daemon.YljService;
import com.ylj.task.bean.DeviceData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public class AdjustControler extends ConnectControler implements IAdjustCtrl {

    public final static String EXTRA_ACTION_FLAG = "EXTRA_ACTION_FLAG";
    public final static String EXTRA_TEST_DATA = "EXTRA_TEST_INFO";

    public final static int CTRL_FLAG_STOP = 0;
    public final static int CTRL_FLAG_START = 1;

    public final static String ACTION_TEST_CTRL = "ACTION_TEST_CTRL";
    public final static String ACTION_TEST_DATA = "ACTION_TEST_DATA";

    public static AdjustControler newInstance(Activity activity) {
        AdjustControler controler = new AdjustControler();
        controler.init(activity, YljService.class);
        return controler;
    }

    private BroadcastReceiver mAdjustReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(ACTION_TEST_CTRL)) {
                int flag = intent.getIntExtra(EXTRA_ACTION_FLAG, CTRL_FLAG_STOP);
                if (flag == CTRL_FLAG_STOP) {
                    for (OnAdjustCtrlLister listener : mAdjustListeners) {
                        listener.onAdjustStop();
                    }
                } else if (flag == CTRL_FLAG_START) {
                    for (OnAdjustCtrlLister listener : mAdjustListeners) {
                        listener.onAdjustStart();
                    }
                }
            } else if (action.equals(ACTION_TEST_DATA)) {
                DeviceData data = intent.getParcelableExtra(EXTRA_TEST_DATA);
                for (OnAdjustCtrlLister listener : mAdjustListeners) {
                    listener.onAdjustRefresh(data);
                }
            }
        }
    };

    @Override
    protected void onServiceConnected(IBinder binder) {
        super.onServiceConnected(binder);
        IntentFilter filter = new IntentFilter(ACTION_TEST_CTRL);
        mActivity.registerReceiver(mAdjustReceiver, filter);
        filter = new IntentFilter(ACTION_TEST_DATA);
        mActivity.registerReceiver(mAdjustReceiver, filter);
    }

    @Override
    public void release() {
        super.release();
        mActivity.unregisterReceiver(mAdjustReceiver);
        mAdjustListeners.clear();
    }

    @Override
    public void startAdjust() {
        if(!isConnect())
            return;
        mCleint.startAdjust();
    }

    @Override
    public void stopAdjust() {
        if(!isConnect())
            return;
        mCleint.stopAdjust();
    }

    private List<OnAdjustCtrlLister> mAdjustListeners = new ArrayList<>();

    @Override
    public void addAdjustCtrlListener(OnAdjustCtrlLister listener) {
        if (mAdjustListeners.contains(listener))
            return;
        mAdjustListeners.add(listener);
    }

    @Override
    public void deleteAdjustCtrlListener(OnAdjustCtrlLister listener) {
        if (mAdjustListeners.contains(listener)) {
            mAdjustListeners.remove(listener);
        }
    }

}
