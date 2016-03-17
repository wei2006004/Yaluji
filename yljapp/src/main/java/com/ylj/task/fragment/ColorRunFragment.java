package com.ylj.task.fragment;

import android.app.Activity;

import com.ylj.R;
import com.ylj.daemon.bean.Record;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.fragment_color_run)
public class ColorRunFragment extends AbstractTestFragment {


    public ColorRunFragment() {
    }


    @Override
    public void showWaitPage() {

    }

    @Override
    public void showTestPage() {

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnColorDataLoadListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnColorDataLoadListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private OnColorDataLoadListener mListener;

    public interface OnColorDataLoadListener {
        public void onColorDataLoadFinish();
    }
}
