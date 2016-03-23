package com.ylj.task.fragment;

import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ylj.R;
import com.ylj.common.widget.PlotView;
import com.ylj.daemon.bean.Record;
import com.ylj.task.ITestCtrl;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.fragment_plot)
public class PlotFragment extends AbstractTestFragment implements ITestCtrl.OnTestDataRefreshListener {

    public static final String EXTRA_MODE = "EXTRA_MODE";

    public static final int MODE_QUAKE_PLOT = 0;
    public static final int MODE_TEMP_PLOT = 1;

    public static final int REFRESH_NUM = 100;

    public final static int MAX_VALUE_TEMP = 240;
    public final static int MIN_VALUE_TEMP = 0;

    public final static int MAX_VALUE_QUAKE = 13;
    public final static int MIN_VALUE_QUAKE = -2;

    public final static int PLOT_GRID_ROW = 6;
    public final static int PLOT_GRID_COLUMN = 8;

    private int mMode = MODE_QUAKE_PLOT;

    List<PointF> mDatas = new ArrayList<>();

    @ViewInject(R.id.tv_quake)
    TextView mQuakeTag;

    @ViewInject(R.id.tv_temp)
    TextView mTempTag;

    @ViewInject(R.id.tv_value)
    TextView mValueTag;

    @ViewInject(R.id.plotview)
    PlotView mPlotView;

    @ViewInject(R.id.layout_wait)
    RelativeLayout mWaitLayout;

    @ViewInject(R.id.layout_test)
    LinearLayout mTestLayout;

    public static PlotFragment newQuakePlotFragment() {
        PlotFragment fragment = new PlotFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_MODE, MODE_QUAKE_PLOT);
        fragment.setArguments(args);
        return fragment;
    }

    public static PlotFragment newTempPlotFragment() {
        PlotFragment fragment = new PlotFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_MODE, MODE_TEMP_PLOT);
        fragment.setArguments(args);
        return fragment;
    }

    public PlotFragment() {
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
        initPlotView();
        getTestCtrl().addOnTestDataRefreshListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getTestCtrl().deleteOnTestDataRefreshListener(this);
    }

    private void initPlotView() {
        PlotView.DrawEdit edit = mPlotView.getEdit();
        edit.clear();
        edit.setGrid(PLOT_GRID_ROW, PLOT_GRID_COLUMN);
        edit.setGridEnable(true);
        edit.setXAxes(0, REFRESH_NUM);

        float ymin, ymax;
        if (mMode == MODE_QUAKE_PLOT) {
            ymax = MAX_VALUE_QUAKE;
            ymin = MIN_VALUE_QUAKE;
        } else {
            ymax = MAX_VALUE_TEMP;
            ymin = MIN_VALUE_TEMP;
        }
        edit.setYAxes(ymin, ymax);
        edit.commit();
    }

    private void initLayout() {
        showWaitPage();
        if (mMode == MODE_QUAKE_PLOT) {
            mQuakeTag.setVisibility(View.VISIBLE);
            mTempTag.setVisibility(View.GONE);
        } else {
            mQuakeTag.setVisibility(View.GONE);
            mTempTag.setVisibility(View.VISIBLE);
        }
    }

    private void initArguments() {
        if (getArguments() != null) {
            mMode = getArguments().getInt(EXTRA_MODE);
        }
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

    boolean mIsRefreshPage = false;

    @Override
    public synchronized void refreshPage() {
        if (!isAdded())
            return;
        mIsRefreshPage = true;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                PlotView.DrawEdit edit = mPlotView.getEdit();
                edit.clear();
                for (PointF data : mDatas) {
                    edit.addPoint(data);
                }
                edit.commit();
                mIsRefreshPage = false;
            }
        });
    }

    @Override
    public void clearPage() {
        mDatas.clear();
        if (!isAdded())
            return;
        initPlotView();
    }

    @Override
    public synchronized void onRefresh(Record data) {
        double value = (mMode == MODE_QUAKE_PLOT) ? data.getQuake() : data.getTemp();
        if (mDatas.size() >= REFRESH_NUM) {
            mDatas.clear();
            clearPage();
        }
        PointF pointF = new PointF(mDatas.size(), (float) value);
        mDatas.add(pointF);
        if (!isAdded())
            return;
        if (mIsRefreshPage)
            return;
        mPlotView.getEdit().addPoint(pointF).commit();

        if(mMode == MODE_QUAKE_PLOT){
            mValueTag.setText(String.format("%.2f",data.getQuake())+getString(R.string.test_quake_unit));
        }else {
            mValueTag.setText(String.format("%.1f",data.getTemp())+getString(R.string.test_temp_unit));
        }
    }
}
