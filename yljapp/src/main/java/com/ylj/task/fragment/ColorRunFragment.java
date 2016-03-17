package com.ylj.task.fragment;


import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ylj.R;
import com.ylj.common.bean.Task;
import com.ylj.common.utils.RoadUtils;
import com.ylj.common.widget.ColorView;
import com.ylj.daemon.bean.TaskResult;
import com.ylj.task.ITestCtrl;
import com.ylj.task.bean.ColorData;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.fragment_color_run)
public class ColorRunFragment extends AbstractTestFragment implements ITestCtrl.OnDrawListener<ColorData> {

    public static final String EXTRA_MODE = "EXTRA_MODE";
    public static final String EXTRA_TASK = "EXTRA_TASK";

    public static final int MODE_SHOW_RESULT = 0;
    public static final int MODE_TASK_TEST = 1;

    public static final int FRAGMENT_FLAG_COLOR = 1;

    private int mMode = MODE_SHOW_RESULT;

    private Task mTask = new Task();
    private TaskResult mResult = new TaskResult();

    List<ColorData> mColorDatas = new ArrayList<>();

    @ViewInject(R.id.traceView)
    ColorView mColorView;

    @ViewInject(R.id.layout_wait)
    RelativeLayout mWaitLayout;

    @ViewInject(R.id.layout_test)
    LinearLayout mTestLayout;

    public static ColorRunFragment newInstance(Task task, int mode) {
        ColorRunFragment fragment = new ColorRunFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_MODE, mode);
        args.putParcelable(EXTRA_TASK, task);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initArguments();
        initLayout();
        initColorView();
        refreshInfoView();
        getTestCtrl().setOnColorDrawListener(this);
    }

    private void refreshInfoView() {

    }

    private void initColorView() {
        Pair<Integer, Integer> pair = RoadUtils.getRoadGrid(
                mTask.getRoadWidth(),
                mTask.getRoadLength(),
                mTask.getRollerWidth());
        ColorView.DrawEdit edit = mColorView.getEdit();
        edit.clear();
        edit.setGrid(pair.first, pair.second);
        edit.commint();
    }

    private void initLayout() {
        showWaitPage();
    }

    private void initArguments() {
        if (getArguments() != null) {
            mTask = getArguments().getParcelable(EXTRA_TASK);
            mMode = getArguments().getInt(EXTRA_MODE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getTestCtrl().setOnColorDrawListener(null);
    }

    public ColorRunFragment() {
    }

    @Override
    public void showWaitPage() {
        super.showWaitPage();
        if (!isAdded())
            return;
        mWaitLayout.setVisibility(View.VISIBLE);
        mTestLayout.setVisibility(View.GONE);
    }

    @Override
    public void showTestPage() {
        super.showTestPage();
        if (!isAdded())
            return;
        mWaitLayout.setVisibility(View.GONE);
        mTestLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void refreshPage() {
        if (!isAdded())
            return;
        ColorView.DrawEdit edit = mColorView.getEdit();
        edit.clear();
        for (ColorData data : mColorDatas) {
            edit.setColor(data.getRow(),
                    data.getColumn(),
                    data.getColor());
        }
        edit.commint();
    }

    @Override
    public void clearPage() {
        if (!isAdded())
            return;
        mColorDatas.clear();
        initColorView();
        refreshInfoView();
    }

    @Override
    public void onLoadDataStart() {
    }

    @Override
    public void onLoadDataFinish(List<ColorData> datas, TaskResult result) {
        if (datas != null) {
            mColorDatas.addAll(datas);
            refreshPage();
            if (mMode == MODE_SHOW_RESULT) {
                mResult = result;
            }
            refreshInfoView();
        }
        getOnDataLoadListener().onDataLoadFinish(FRAGMENT_FLAG_COLOR);
    }

    @Override
    public void onAddData(ColorData data) {
        mColorDatas.add(data);
        if (!isAdded())
            return;
        mColorView.getEdit()
                .setColor(data.getRow(), data.getColumn(), data.getColor())
                .commint();
    }
}
