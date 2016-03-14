package com.ylj.adjust;

/**
 * Created by Administrator on 2016/3/3 0003.
 */
public interface IAdjustCtrl {
    void startAdjust();
    void stopAdjust();

    void addAdjustCtrlListener(OnCtrlLister listener);
    void deleteAdjustCtrlListener(OnCtrlLister listener);

    interface OnCtrlLister {
        void onAdjustStart();
        void onAdjustStop();
        void onAdjustRefresh(double data);
    }
}
