package com.ylj.adjust;

/**
 * Created by Administrator on 2016/3/3 0003.
 */
public interface IAdjustCtrl {
    void startAdjust();
    void stopAdjust();

    void addOnRefreshListener(OnRefreshLister listener);
    void deleteOnRefreshListener(OnRefreshLister listener);

    interface OnRefreshLister{
        void refresh(double data);
    }
}
