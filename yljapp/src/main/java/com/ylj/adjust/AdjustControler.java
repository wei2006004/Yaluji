package com.ylj.adjust;

import android.app.Activity;
import android.os.Bundle;

import com.ylj.connect.ConnectControler;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public class AdjustControler extends ConnectControler implements IAdjustCtrl {

    public static AdjustControler newInstance(Activity activity, Class<?> cls) {
        return new AdjustControler(activity,cls);
    }

    AdjustControler(Activity activity, Class<?> cls) {
        super(activity, cls);
    }

    @Override
    protected void onReceive(Bundle data) {

    }

    @Override
    public void startAdjust() {

    }

    @Override
    public void stopAdjust() {

    }

    @Override
    public void addAdjustCtrlListener(OnCtrlLister listener) {

    }

    @Override
    public void deleteAdjustCtrlListener(OnCtrlLister listener) {

    }

}
