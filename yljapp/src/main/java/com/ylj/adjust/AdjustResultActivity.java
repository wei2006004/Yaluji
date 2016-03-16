package com.ylj.adjust;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ylj.R;
import com.ylj.adjust.bean.AdjustResult;
import com.ylj.common.BaseActivity;
import com.ylj.common.bean.Task;
import com.ylj.common.widget.PlotView;
import com.ylj.db.DbLet;
import com.ylj.task.TaskActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_adjust_result)
public class AdjustResultActivity extends BaseActivity {

    public static final int ADJUST_TIME = 3;

    public static final String EXTRA_TASK = "EXTRA_TASK";
    public static final String EXTRA_LIGHT_ADJUST = "EXTRA_LIGHT_ADJUST";
    public static final String EXTRA_MIDDLE_ADJUST = "EXTRA_MIDDLE_ADJUST";
    public static final String EXTRA_HEAVY_ADJUST = "EXTRA_HEAVY_ADJUST";

    private final static int MIN_VCV_VALUE = -5;
    private final static int MAX_VCV_VALUE = 15;
    private final static int MIN_X_VALUE = -5;
    private final static int MAX_X_VALUE = 15;

    Task mTask;
    AdjustResult[] mResults = new AdjustResult[ADJUST_TIME];

    @ViewInject(R.id.plotview)
    PlotView plotview;

    @ViewInject(R.id.tv_atext)
    TextView mAText;

    @ViewInject(R.id.tv_btext)
    TextView mBText;

    @ViewInject(R.id.tv_rtext)
    TextView mRText;

    @ViewInject(R.id.tv_vcvtext)
    TextView mVCVText;

    @ViewInject(R.id.et_result_x)
    EditText mXEdit;

    @ViewInject(R.id.tv_promt)
    TextView mMsgText;

    @ViewInject(R.id.btn_adjust_again)
    Button mAgainButton;

    @ViewInject(R.id.fab)
    FloatingActionButton mFabButton;

    @Event(R.id.btn_adjust_again)
    private void onAgainClick(View view) {
        Intent intent=new Intent(this,AdjustActivity.class);
        intent.putExtra(EXTRA_TASK, mTask);
        startActivity(intent);
    }

    @Event(R.id.fab)
    private void onFabClick(View view) {
        if(mXEdit.getText().toString().equals("")){
            showToast("please input x");
            return;
        }
        String vcvText=mVCVText.getText().toString();
        mTask.setVCV(Double.parseDouble(vcvText));
        mTask.setIsAdjust(true);
        DbLet.saveOrUpdateTask(mTask);
        showToast("task save");

        TaskActivity.startNewActivity(this,mTask);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initExtraData();
        initToolbar();
        initPlotView();
        countParas();
        fillParasView();
        addXInputListener();
        initLayout();
    }

    private void initLayout() {
        if(r>0.7){
            mFabButton.setVisibility(View.VISIBLE);
            mAgainButton.setVisibility(View.GONE);
        }else {
            mFabButton.setVisibility(View.GONE);
            mAgainButton.setVisibility(View.VISIBLE);
        }
    }

    private void addXInputListener() {
        mXEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                try {
                    double value = Double.parseDouble(mXEdit.getText().toString());
                    mVCVText.setText(String.format("%.2f", value * b + a));
                } catch (Exception e) {
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                drawDatas();
            }
        });
    }

    private void drawDatas() {
        PlotView.DrawEdit edit = plotview.getEdit();
        for (int i = 0; i < ADJUST_TIME; i++) {
            AdjustResult result = mResults[i];
            for (int j = 0; j < AdjustResult.ADJUST_POINT_NUM; j++) {
                float compaction = (float) result.getCompaction(j);
                float quake = (float) result.getQuake(j);
                edit.addDrawPoint(new PointF(compaction, quake));
            }
        }
        edit.addPoint(new PointF(MIN_X_VALUE, (float) (a + b * MIN_X_VALUE)));
        edit.addPoint(new PointF(MAX_X_VALUE, (float) (a + b * MAX_X_VALUE)));
        edit.addPoint(new PointF((float) ((MIN_VCV_VALUE - a) / b), MIN_VCV_VALUE));
        edit.addPoint(new PointF((float) ((MAX_VCV_VALUE - a) / b), MAX_VCV_VALUE));
        edit.commit();
    }

    private void fillParasView() {
        mRText.setText(String.format("%.2f", r));
        mAText.setText(String.format("%.2f", a));
        mBText.setText(String.format("%.2f", b));
        if (r < 0.7) {
            mMsgText.setTextColor(Color.RED);
            mMsgText.setText(R.string.ad_promt_fail);
        } else {
            mMsgText.setText(R.string.ad_promt_pass);
        }
    }

    double a, b, r;

    private void countParas() {
        double qavg, cavg, qsum = 0, csum = 0;
        for (int i = 0; i < ADJUST_TIME; i++) {
            AdjustResult result = mResults[i];
            for (int j = 0; j < AdjustResult.ADJUST_POINT_NUM; j++) {
                qsum += result.getQuake(j);
                csum += result.getCompaction(j);
            }
        }
        qavg = qsum / (ADJUST_TIME * AdjustResult.ADJUST_POINT_NUM);
        cavg = csum / (ADJUST_TIME * AdjustResult.ADJUST_POINT_NUM);

        double qc = 0, q2 = 0, c2 = 0;
        double quake, com;
        for (int i = 0; i < ADJUST_TIME; i++) {
            AdjustResult result = mResults[i];
            for (int j = 0; j < AdjustResult.ADJUST_POINT_NUM; j++) {
                quake = result.getQuake(j);
                com = result.getCompaction(j);
                qc += (quake - qavg) * (com - cavg);
                q2 += (quake - qavg) * (quake - qavg);
                c2 += (com - cavg) * (com - cavg);
            }
        }

        b = qc / c2;
        a = qavg - b * cavg;
        r = qc / (float) Math.sqrt(q2 * c2);
        r = r < 0 ? -r : r;
    }

    private void initPlotView() {
        PlotView.DrawEdit edit = plotview.getEdit();
        edit.setGridEnable(false);
        edit.setXAxes(MIN_X_VALUE, MAX_X_VALUE);
        edit.setYAxes(MIN_VCV_VALUE, MAX_VCV_VALUE);
        edit.commit();
    }

    private void initExtraData() {
        Intent intent = getIntent();
        mTask = intent.getParcelableExtra(EXTRA_TASK);
        mResults[0] = intent.getParcelableExtra(EXTRA_LIGHT_ADJUST);
        mResults[1] = intent.getParcelableExtra(EXTRA_MIDDLE_ADJUST);
        mResults[2] = intent.getParcelableExtra(EXTRA_HEAVY_ADJUST);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert("Info", "Do you want to leave this adjust result?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AdjustResultActivity.this.finish();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
