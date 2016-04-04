package com.ylj.task.fragment;


import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    @ViewInject(R.id.colorView)
    ColorView mColorView;

    @ViewInject(R.id.layout_wait)
    RelativeLayout mWaitLayout;

    @ViewInject(R.id.layout_test)
    LinearLayout mTestLayout;

    @ViewInject(R.id.layout_test_info)
    LinearLayout mTestInfoLayout;

    @ViewInject(R.id.layout_result_info)
    LinearLayout mResultInfoLayout;

    @ViewInject(R.id.tv_pos_x)
    TextView mColumnView;

    @ViewInject(R.id.tv_pos_y)
    TextView mRowView;

    @ViewInject(R.id.tv_times)
    TextView mTimesView;

    @ViewInject(R.id.tv_status)
    TextView mStatusView;

    @ViewInject(R.id.tv_no_pass)
    TextView mNoPassView;

    @ViewInject(R.id.tv_pass_num)
    TextView mPassView;

    @ViewInject(R.id.tv_good_num)
    TextView mGoodView;

    @ViewInject(R.id.tv_excellent_num)
    TextView mExcellentView;

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
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initLayout();
        initColorView();
        initInfoView();
        getTestCtrl().setOnColorDrawListener(this);
    }

    private void initInfoView() {
        if (mMode == MODE_SHOW_RESULT) {
            refreshResultInfoView(new TaskResult());
        } else {
            refreshTestInfoView(new ColorData());
        }
    }

    private void refreshResultInfoView(TaskResult result) {
        if (result == null)
            return;
        mNoPassView.setText(String.valueOf(result.getNotPassNum()));
        mPassView.setText(String.valueOf(result.getPassNum()));
        mGoodView.setText(String.valueOf(result.getGoodNum()));
        mExcellentView.setText(String.valueOf(result.getExcellentNum()));
    }

    private void refreshTestInfoView(ColorData data) {
        if (data == null)
            return;
        mRowView.setText(String.valueOf(data.getRow() + 1));
        mColumnView.setText(String.valueOf(data.getColumn() + 1));
        mTimesView.setText(String.valueOf(data.getTimes()));
        switch (data.getLevel()) {
            case ColorData.LEVEL_NONE:
                mStatusView.setText(R.string.test_none);
                break;
            case ColorData.LEVEL_NOT_PASS:
                mStatusView.setText(R.string.test_not_pass);
                break;
            case ColorData.LEVEL_PASS:
                mStatusView.setText(R.string.test_pass);
                break;
            case ColorData.LEVEL_GOOD:
                mStatusView.setText(R.string.test_good);
                break;
            case ColorData.LEVEL_EXCELLENT:
                mStatusView.setText(R.string.test_excellent);
                break;
        }
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

        if (mMode == MODE_SHOW_RESULT) {
            mResultInfoLayout.setVisibility(View.VISIBLE);
            mTestInfoLayout.setVisibility(View.GONE);
        } else {
            mTestInfoLayout.setVisibility(View.VISIBLE);
            mResultInfoLayout.setVisibility(View.GONE);
        }
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
        if (!mColorDatas.isEmpty()) {
            refreshTestInfoView(mColorDatas.get(mColorDatas.size() - 1));
        }
    }

    @Override
    public void clearPage() {
        if (!isAdded())
            return;
        mColorDatas.clear();
        initColorView();
        initInfoView();
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
                refreshResultInfoView(result);
            }
            if (!datas.isEmpty()) {
                refreshTestInfoView(datas.get(datas.size() - 1));
            }
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
        refreshTestInfoView(data);
    }
}
