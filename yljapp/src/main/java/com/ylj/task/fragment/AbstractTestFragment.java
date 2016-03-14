package com.ylj.task.fragment;

import com.ylj.common.BaseFragment;
import com.ylj.task.bean.Record;

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

    public abstract void clearPlot();

    public abstract void addData(Record data);

    public abstract void addDataAndRefresh(Record data);
}
