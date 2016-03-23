package com.ylj.task.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ylj.R;
import com.ylj.common.bean.Task;
import com.ylj.common.widget.TracePlantView;
import com.ylj.daemon.bean.TaskResult;
import com.ylj.task.ITestCtrl;
import com.ylj.task.bean.TraceData;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.fragment_trace)
public class TraceFragment extends AbstractTestFragment implements ITestCtrl.OnDrawListener<TraceData> {

    public static final String EXTRA_TASK = "EXTRA_TASK";

    public static final int FRAGMENT_FLAG_TRACE = 0;

    Task mTask = new Task();

    List<TraceData> mTraceDatas = new ArrayList<>();

    @ViewInject(R.id.traceView)
    TracePlantView mTraceView;

    @ViewInject(R.id.layout_wait)
    RelativeLayout mWaitLayout;

    @ViewInject(R.id.layout_test)
    LinearLayout mTestLayout;

    @ViewInject(R.id.tv_pos_x)
    TextView mPosxText;

    @ViewInject(R.id.tv_pos_y)
    TextView mPosyText;

    @ViewInject(R.id.tv_speed)
    TextView mSpeedText;

    public static TraceFragment newInstance(Task task) {
        TraceFragment fragment = new TraceFragment();
        Bundle args = new Bundle();
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
        initTraceView();
        getTestCtrl().setOnTraceDrawListener(this);
    }

    private void initLayout() {
        showWaitPage();
    }

    private void initArguments() {
        if (getArguments() != null) {
            mTask = getArguments().getParcelable(EXTRA_TASK);
        }
    }

    private void initTraceView() {
        TracePlantView.DrawEdit edit = mTraceView.getEdit();
        edit.clear();
        edit.setField(mTask.getRoadLength(), mTask.getRoadWidth());
        edit.setPlant(mTask.getRollerWidth(), mTask.getRollerDiameter());
        edit.setOrigin(mTask.getOrigin());
        edit.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getTestCtrl().setOnTraceDrawListener(null);
    }

    public TraceFragment() {
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
        TracePlantView.DrawEdit edit = mTraceView.getEdit();
        edit.clear();
        for (TraceData data : mTraceDatas) {
            edit.addPlant(data.getPostionX(),
                    data.getPostionY(),
                    data.getDirection(),
                    data.getColor());
        }
        edit.commit();

        if(!mTraceDatas.isEmpty()){
            refreshStatus(mTraceDatas.get(mTraceDatas.size() - 1));
        }
    }

    @Override
    public void clearPage() {
        if (!isAdded())
            return;
        mTraceDatas.clear();
        initTraceView();
    }

    @Override
    public void onLoadDataStart() {
    }

    @Override
    public void onLoadDataFinish(List<TraceData> datas, TaskResult result) {
        if (datas != null) {
            mTraceDatas.addAll(datas);
            refreshPage();
        }
        if (getOnDataLoadListener() != null) {
            getOnDataLoadListener().onDataLoadFinish(FRAGMENT_FLAG_TRACE);
        }
    }

    @Override
    public void onAddData(TraceData data) {
        mTraceDatas.add(data);
        if (!isAdded())
            return;
        mTraceView.getEdit().
                addPlant(data.getPostionX(),
                        data.getPostionY(),
                        data.getDirection(),
                        data.getColor()).
                commit();

        refreshStatus(data);
    }

    private void refreshStatus(TraceData data) {
        mPosxText.setText(String.format("%.1f", data.getPostionX()) + getString(R.string.test_meter));
        mPosyText.setText(String.format("%.1f", data.getPostionY()) + getString(R.string.test_meter));
    }
}
