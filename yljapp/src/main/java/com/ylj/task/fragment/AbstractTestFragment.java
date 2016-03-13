package com.ylj.task.fragment;

import com.ylj.common.BaseFragment;
import com.ylj.task.bean.Record;

/**
 * Created by Administrator on 2016/3/13 0013.
 */
public abstract class AbstractTestFragment extends BaseFragment {

    public abstract void showWaitPage();
    public abstract void showTestPage();

    public abstract void redrawPlot();
    public abstract void clearPlot();

    public abstract void addData(Record data);
    public abstract void addDataAndRefresh(Record data);
}
