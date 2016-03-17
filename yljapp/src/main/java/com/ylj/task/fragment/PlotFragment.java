package com.ylj.task.fragment;

import android.os.Bundle;

import com.ylj.R;
import com.ylj.daemon.bean.Record;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.fragment_plot)
public class PlotFragment extends AbstractTestFragment {

    public static final String EXTRA_MODE = "EXTRA_MODE";

    public static final int MODE_QUAKE_PLOT = 0;
    public static final int MODE_TEMP_PLOT = 1;

    private int mMode = MODE_QUAKE_PLOT;

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
        if (getArguments() != null) {
            mMode = getArguments().getInt(EXTRA_MODE);
        }
    }

    @Override
    public void showWaitPage() {
        super.showWaitPage();
    }

    @Override
    public void showTestPage() {
        super.showTestPage();
    }

    @Override
    public void refreshPage() {

    }

    @Override
    public void clearPlot() {

    }

    @Override
    public void addData(Record data) {

    }

    @Override
    public void addDataAndRefresh(Record data) {

    }
}
