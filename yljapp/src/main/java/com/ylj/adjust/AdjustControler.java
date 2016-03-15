package com.ylj.adjust;

import android.app.Activity;
import android.os.IBinder;

import com.ylj.daemon.YljService;
import com.ylj.task.TestControler;
import com.ylj.task.bean.DeviceData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public class AdjustControler extends TestControler implements IAdjustCtrl {

    public static AdjustControler newInstance(Activity activity) {
        AdjustControler controler = new AdjustControler();
        controler.init(activity, YljService.class);
        return controler;
    }

    @Override
    protected void onServiceConnected(IBinder binder) {
        super.onServiceConnected(binder);
        addTestCtrlListener(new OnTestCtrlListener() {
            @Override
            public void onTestStart() {
                for (OnAdjustCtrlLister listener : mAdjustListeners) {
                    listener.onAdjustStart();
                }
            }

            @Override
            public void onTestStop() {
                for (OnAdjustCtrlLister listener : mAdjustListeners) {
                    listener.onAdjustStop();
                }
            }

            @Override
            public void onTestRefresh(DeviceData data) {
                for (OnAdjustCtrlLister listener : mAdjustListeners) {
                    listener.onAdjustRefresh(data.getQuake());
                }
            }
        });
    }

    @Override
    public void startAdjust() {
        startTest();
    }

    @Override
    public void stopAdjust() {
        stopTest();
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
