package com.ylj.adjust;

/**
 * Created by Administrator on 2016/3/3 0003.
 */
public interface IAdjustCtrl {
    void startAdjust();
    void stopAdjust();

    void addOnRefreshListener(OnCtrlLister listener);
    void deleteOnRefreshListener(OnCtrlLister listener);

    interface OnCtrlLister {
        void onAdjustStart();
        void onAdjustStop();
        void refresh(double data);
    }
}
