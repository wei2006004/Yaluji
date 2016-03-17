package com.ylj.task.fragment;

import android.app.Activity;

import com.ylj.common.BaseFragment;
import com.ylj.common.bean.Task;
import com.ylj.daemon.bean.Record;
import com.ylj.task.AbstractTestActivity;
import com.ylj.task.ITestCtrl;

/**
 * Created by Administrator on 2016/3/13 0013.
 */
public abstract class AbstractTestFragment extends BaseFragment {

    public static final int MODE_WAIT_PAGE = 0;
    public static final int MODE_TEST_PAGE = 1;

    private int mPageMode = MODE_WAIT_PAGE;

    public void showWaitPage() {
        mPageMode = MODE_WAIT_PAGE;
    }

    public void showTestPage() {
        mPageMode = MODE_TEST_PAGE;
    }

    public boolean isWaitPage() {
        return mPageMode == MODE_WAIT_PAGE;
    }

    public boolean isTestPage() {
        return mPageMode == MODE_WAIT_PAGE;
    }

    public abstract void refreshPage();
    public abstract void clearPage();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnDataLoadListener) activity;
            AbstractTestActivity testActivity = (AbstractTestActivity)activity;
            mTestCtrl = testActivity.getTestCtrl();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDataLoadListener/AbstractTestActivity");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mTestCtrl = null;
    }

    private ITestCtrl mTestCtrl;

    protected ITestCtrl getTestCtrl(){
        return mTestCtrl;
    }

    private OnDataLoadListener mListener;

    protected OnDataLoadListener getOnDataLoadListener(){
        return mListener;
    }

    public interface OnDataLoadListener {
        void onDataLoadFinish(int flag);
    }

}
