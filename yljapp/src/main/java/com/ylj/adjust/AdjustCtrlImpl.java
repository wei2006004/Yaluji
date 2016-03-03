package com.ylj.adjust;

/**
 * Created by Administrator on 2016/3/3 0003.
 */
public class AdjustCtrlImpl implements IAdjustCtrl {

    public static IAdjustCtrl instance(){
        return new AdjustCtrlImpl();
    }

    @Override
    public void startAdjust() {

    }

    @Override
    public void stopAdjust() {

    }

    @Override
    public void addOnRefreshListener(OnRefreshLister listener) {

    }

    @Override
    public void deleteOnRefreshListener(OnRefreshLister listener) {

    }
}
