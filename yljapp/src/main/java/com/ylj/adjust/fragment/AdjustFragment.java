package com.ylj.adjust.fragment;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ylj.R;
import com.ylj.adjust.IAdjustCtrl;
import com.ylj.adjust.bean.AdjustResult;
import com.ylj.common.BaseFragment;
import com.ylj.common.config.ConfigLet;
import com.ylj.common.utils.DataConvertor;
import com.ylj.common.widget.PlotView;
import com.ylj.daemon.bean.DeviceData;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ContentView(R.layout.fragment_adjust)
public class AdjustFragment extends BaseFragment implements IAdjustCtrl.OnAdjustCtrlLister {

    public final static int MAX_QUAKE = 15;
    public final static int MIN_QUAKE = -5;

    public final static int ADJUST_LENGTH = 100;
    public static final int ADJUST_RESULT_POINT_NUM = 6;

    List<Double> quakeDatas = new ArrayList<>();

    boolean mIsRun = false;
    boolean mIsFinish = false;

    IAdjustCtrl mAdjustCtrl = null;

    @ViewInject(R.id.plotview)
    PlotView plotView;

    @ViewInject(R.id.pos1)
    EditText posText1;

    @ViewInject(R.id.pos2)
    EditText posText2;

    @ViewInject(R.id.pos3)
    EditText posText3;

    @ViewInject(R.id.pos4)
    EditText posText4;

    @ViewInject(R.id.pos5)
    EditText posText5;

    @ViewInject(R.id.pos6)
    EditText posText6;

    @ViewInject(R.id.compact1)
    EditText comText1;

    @ViewInject(R.id.compact2)
    EditText comText2;

    @ViewInject(R.id.compact3)
    EditText comText3;

    @ViewInject(R.id.compact4)
    EditText comText4;

    @ViewInject(R.id.compact5)
    EditText comText5;

    @ViewInject(R.id.compact6)
    EditText comText6;

    @ViewInject(R.id.tv_status)
    TextView mStatusText;

    @ViewInject(R.id.btn_auto)
    Button mAutoButton;

    @ViewInject(R.id.btn_run)
    Button mRunButton;

    @Event(R.id.btn_auto)
    private void onAutoClick(View view) {
        Random random = new Random();
        int level=random.nextInt(13);
        quakeDatas.clear();
        for (int i = 0; i < ADJUST_LENGTH; i++) {
            quakeDatas.add(random.nextDouble()  + level);
        }
        PlotView.DrawEdit edit = plotView.getEdit();
        edit.clear();
        for (int i = 0; i < ADJUST_LENGTH; i++) {
            double quake = quakeDatas.get(i);
            edit.addPoint(new PointF(i, (float) quake));
        }
        edit.commit();

        for (int i = 0; i < ADJUST_RESULT_POINT_NUM; i++) {
            int pos = random.nextInt(ADJUST_LENGTH);
            double compact = quakeDatas.get(pos) - random.nextDouble()/2;
            mPosTextArray[i].setText(String.valueOf(pos));
            mComTextArray[i].setText(String.format("%.1f", compact));
        }

        if (mListener != null) {
            mListener.onAdjustFinish();
        }
        setStatus(R.string.adjust_finish);
        mRunButton.setEnabled(false);
        mRunButton.setText(R.string.adjust_run);
    }

    @Event(R.id.btn_run)
    private void onRunClick(View view) {
        if (mIsFinish) {
            initStutas();
            mIsFinish = false;
        }
        if (mIsRun) {
            doPause();
            mIsRun = false;
        } else {
            doRun();
            mIsRun = true;
        }
    }

    public void setAdjustCtrl(IAdjustCtrl ctrl){
        mAdjustCtrl = ctrl;
    }

    private void initStutas() {
        quakeDatas.clear();
        plotView.getEdit().clear().commit();
    }

    private void setStatus(String text){
        mStatusText.setText(text);
    }

    private void setStatus(int text){
        mStatusText.setText(text);
    }

    private void doPause() {
        if(mAdjustCtrl != null){
            mAdjustCtrl.stopAdjust();
            setStatus(R.string.adjust_pause);
            mRunButton.setText(R.string.adjust_run);
        }
    }

    private void doRun() {
        if(mAdjustCtrl != null){
            mAdjustCtrl.addAdjustCtrlListener(this);
            mAdjustCtrl.startAdjust();

            setStatus(R.string.adjust_run);
            mRunButton.setText(R.string.adjust_pause);
        }
    }

    EditText[] mPosTextArray = new EditText[ADJUST_RESULT_POINT_NUM];
    EditText[] mComTextArray = new EditText[ADJUST_RESULT_POINT_NUM];

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPosTextArray = new EditText[]{posText1, posText2, posText3, posText4, posText5, posText6};
        mComTextArray = new EditText[]{comText1, comText2, comText3, comText4, comText5, comText6};

        initContentView();
    }

    private void initContentView() {
        plotView.getEdit()
                .setXAxes(0, ADJUST_LENGTH)
                .setYAxes(MIN_QUAKE, MAX_QUAKE)
                .commit();

        if (ConfigLet.isDebug()) {
            mAutoButton.setVisibility(View.VISIBLE);
        } else {
            mAutoButton.setVisibility(View.GONE);
        }
        setStatus(R.string.adjust_wait);
    }

    public AdjustResult requestAdjustResult() {
        AdjustResult result = new AdjustResult();
        for (int i = 0; i < ADJUST_RESULT_POINT_NUM; i++) {
            String posString = mPosTextArray[i].getText().toString();
            String comString = mComTextArray[i].getText().toString();
            if (posString.equals("") || comString.equals("")) {
                showToast(R.string.toast_adjust_data_empty);
                return null;
            }
            int position = Integer.parseInt(posString);
            if (position < 1 || position > ADJUST_LENGTH) {
                showToast(R.string.toast_position_valid);
                return null;
            }
        }

        for (int i = 0; i < ADJUST_RESULT_POINT_NUM; i++) {
            String posString = mPosTextArray[i].getText().toString();
            String comString = mComTextArray[i].getText().toString();
            int position = Integer.parseInt(posString);
            double quake = quakeDatas.get(position - 1);
            double compaction = Double.parseDouble(comString);
            result.setData(i, quake, compaction);
        }
        return result;
    }

    private OnAdjustFinishListener mListener;

    public AdjustFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnAdjustFinishListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAdjustFinishListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mIsRun){
            if(mAdjustCtrl != null){
                mAdjustCtrl.stopAdjust();
                mAdjustCtrl.deleteAdjustCtrlListener(this);
            }
        }
    }

    public void refreshPlot() {
        PointF pointF;
        double data;
        PlotView.DrawEdit edit = plotView.getEdit();
        edit.clear();
        for (int i = 0; i < quakeDatas.size(); i++) {
            data = quakeDatas.get(i);
            pointF = new PointF(i, (float) data);
            edit.addPoint(pointF);
        }
        edit.commit();
    }

    @Override
    public void onAdjustStart() {

    }

    @Override
    public void onAdjustStop() {
        if(mAdjustCtrl != null){
            mAdjustCtrl.deleteAdjustCtrlListener(this);
        }
    }

    @Override
    public void onAdjustRefresh(DeviceData data) {
        double quake = DataConvertor.covertQuakeFromAD(data.getQuake());
        if (quakeDatas.size() >= ADJUST_LENGTH) {
            if(mAdjustCtrl != null){
                mAdjustCtrl.stopAdjust();
            }
            mIsFinish = true;
            if (mListener != null) {
                mListener.onAdjustFinish();
            }
            setStatus(R.string.adjust_finish);
            mRunButton.setText(R.string.adjust_run);
            mRunButton.setEnabled(false);
            return;
        }
        quakeDatas.add(quake);
        PointF pointF = new PointF(quakeDatas.size() - 1, (float) quake);
        plotView.getEdit()
                .addPoint(pointF)
                .commit();
    }

    public interface OnAdjustFinishListener {
        void onAdjustFinish();
    }

}
