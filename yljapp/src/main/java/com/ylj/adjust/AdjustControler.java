package com.ylj.adjust;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.ylj.connect.ConnectControler;
import com.ylj.daemon.YljService;
import com.ylj.daemon.bean.DeviceData;
import com.ylj.daemon.config.ServiceAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public class AdjustControler extends ConnectControler implements IAdjustCtrl {

    

    public static AdjustControler newInstance(Activity activity) {
        AdjustControler controler = new AdjustControler();
        controler.init(activity, YljService.class);
        return controler;
    }

    private BroadcastReceiver mAdjustReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            Log.d("AdjustControler", "action:" + action);
            if (action.equals(ServiceAction.ACTION_SAMPLE_CTRL_STATE_CHANGE)) {
                int flag = intent.getIntExtra(ServiceAction.EXTRA_ACTION_FLAG, ServiceAction.CTRL_FLAG_STOP);
                if (flag == ServiceAction.CTRL_FLAG_STOP) {
                    for (OnAdjustCtrlLister listener : mAdjustListeners) {
                        listener.onAdjustStop();
                    }
                } else if (flag == ServiceAction.CTRL_FLAG_START) {
                    for (OnAdjustCtrlLister listener : mAdjustListeners) {
                        listener.onAdjustStart();
                    }
                }
            } else if (action.equals(ServiceAction.ACTION_ADJUST_DATA)) {
                DeviceData data = intent.getParcelableExtra(ServiceAction.EXTRA_ADJUST_DATA);
                for (OnAdjustCtrlLister listener : mAdjustListeners) {
                    listener.onAdjustRefresh(data);
                }
            }
        }
    };

    @Override
    protected void onServiceConnected(IBinder binder) {
        super.onServiceConnected(binder);
        IntentFilter filter = new IntentFilter(ServiceAction.ACTION_SAMPLE_CTRL_STATE_CHANGE);
        mActivity.registerReceiver(mAdjustReceiver, filter);
        filter = new IntentFilter(ServiceAction.ACTION_ADJUST_DATA);
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
