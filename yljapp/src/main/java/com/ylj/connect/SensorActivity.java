package com.ylj.connect;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ylj.R;
import com.ylj.adjust.AdjustControler;
import com.ylj.common.BaseActivity;
import com.ylj.common.config.AppStatus;
import com.ylj.connect.bean.DeviceInfo;
import com.ylj.daemon.bean.DeviceData;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_sensor)
public class SensorActivity extends ConnectCtrlActivity implements AdjustControler.OnAdjustCtrlLister{

    AdjustControler mAdjustControler;

    @ViewInject(R.id.et_temp)
    EditText mTempEdit;

    @ViewInject(R.id.et_quake)
    EditText mQuakeEdit;

    @ViewInject(R.id.et_pulse)
    EditText mPulseEdit;

    @ViewInject(R.id.et_heading)
    EditText mHeadingEdit;

    @ViewInject(R.id.et_pitch)
    EditText mPitchEdit;

    @ViewInject(R.id.et_roll)
    EditText mRollEdit;

    @ViewInject(R.id.btn_start)
    Button mStartBtn;

    @ViewInject(R.id.btn_stop)
    Button mStopBtn;

    boolean mIsRun = false;

    @Event(R.id.btn_start)
    private void onStartClick(View view) {
        mAdjustControler.startAdjust();
    }

    @Event(R.id.btn_stop)
    private void onStopClick(View view) {
        mAdjustControler.stopAdjust();
    }

    @Override
    protected ConnectControler getConnectControler() {
        if(mAdjustControler == null){
            mAdjustControler = AdjustControler.newInstance(this);
        }
        return mAdjustControler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolBar();
        initData();
        initContentView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mIsRun) {
            mAdjustControler.stopAdjust();
            mIsRun = false;
        }
        mAdjustControler.release();
    }

    private void initData() {
        if(mAdjustControler == null){
            mAdjustControler = AdjustControler.newInstance(this);
        }
        mAdjustControler.addAdjustCtrlListener(this);
    }

    private void initContentView() {
        mStopBtn.setEnabled(false);
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsRun) {
                    mAdjustControler.stopAdjust();
                    mIsRun = false;
                }
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onAdjustStart() {
        mIsRun = true;
        mStopBtn.setEnabled(true);
        mStartBtn.setEnabled(false);
    }

    @Override
    public void onAdjustStop() {
        mIsRun = false;
        mStartBtn.setEnabled(true);
        mStopBtn.setEnabled(false);
    }

    @Override
    public void onAdjustRefresh(DeviceData data) {
        mTempEdit.setText(String.valueOf(data.getTemp()));
        mQuakeEdit.setText(String.valueOf(data.getQuake()));
        mPulseEdit.setText(String.valueOf(data.getPulse()));
        mHeadingEdit.setText(String.valueOf(data.getCompassHeading()));
        mPitchEdit.setText(String.valueOf(data.getCompassPitch()));
        mRollEdit.setText(String.valueOf(data.getCompassRoll()));
        mTempEdit.setText(String.valueOf(data.getTemp()));
    }
}
