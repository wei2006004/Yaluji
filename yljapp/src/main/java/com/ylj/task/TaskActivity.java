package com.ylj.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ylj.R;
import com.ylj.adjust.AdjustActivity;
import com.ylj.common.BaseActivity;
import com.ylj.common.bean.Task;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_task)
public class TaskActivity extends BaseActivity {

    public static final String EXTRA_TASK = "EXTRA_TASK";

    public static final int ACTIVITY_REQUEST_TASK = 1;

    Task mTask = new Task();
    boolean mIsAdjust = false;
    boolean mIsFinish = false;

    public static void startNewActivity(Context context, Task task) {
        Intent intent = new Intent(context, TaskActivity.class);
        intent.putExtra(EXTRA_TASK, task);
        context.startActivity(intent);
    }

    @ViewInject(R.id.tv_task_name)
    TextView mTaskNameText;

    @ViewInject(R.id.tv_road_name)
    TextView mRoadNameText;

    @ViewInject(R.id.tv_road_width)
    TextView mRoadWidthText;

    @ViewInject(R.id.tv_road_length)
    TextView mRoadLengthText;

    @ViewInject(R.id.tv_status)
    TextView mStatusText;

    @ViewInject(R.id.lv_test)
    ListView mTestListView;

    @ViewInject(R.id.layout_task_edit)
    RelativeLayout mEditLayout;

    @ViewInject(R.id.layout_adjust)
    LinearLayout mAdjustLayout;

    @ViewInject(R.id.layout_test)
    LinearLayout mTestLayout;

    @ViewInject(R.id.layout_enter_test)
    LinearLayout mEnterTestLayout;

    @Event(R.id.btn_task_edit)
    private void onTaskEditClick(View view) {
        TaskModifyActivity.startAsModifyTaskActivityForResult(this, mTask, ACTIVITY_REQUEST_TASK);
    }

    @Event(R.id.rl_task_info)
    private void onTaskInfoLayoutClick(View view) {
        if (mIsFinish) {
            TaskModifyActivity.startAsShowOnlyTaskActivity(this, mTask);
        } else {
            TaskModifyActivity.startAsShowTaskActivityForResult(this, mTask, ACTIVITY_REQUEST_TASK);
        }
    }

    @Event(R.id.btn_enter_adjust)
    private void onEnterAdjustButtonClick(View view) {
        AdjustActivity.startNewActivity(this, mTask);
    }

    @Event(R.id.btn_enter_test)
    private void onEnterTestButtonClick(View view) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case ACTIVITY_REQUEST_TASK:
                initExtraData(data);
                initLayout();
                initInfoView();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initExtraData(getIntent());
        initToolbar();
        initLayout();
        initInfoView();
        initTestListView();
    }

    private void initTestListView() {

    }

    private void initInfoView() {
        mTaskNameText.setText(mTask.getTaskName());
        mRoadNameText.setText(mTask.getRoadName());
        mRoadLengthText.setText(String.format("%.2f", mTask.getRoadLength()));
        mRoadWidthText.setText(String.format("%.2f", mTask.getRoadWidth()));
        if (mIsAdjust) {
            if (mIsFinish) {
                mStatusText.setText("finished");
            } else {
                mStatusText.setText("no finished");
            }
        } else {
            mStatusText.setText("no adjust");
        }
    }

    private void initLayout() {
        if (mIsAdjust) {
            mAdjustLayout.setVisibility(View.GONE);
            mEnterTestLayout.setVisibility(View.VISIBLE);
            mTestLayout.setVisibility(View.VISIBLE);
        } else {
            mAdjustLayout.setVisibility(View.VISIBLE);
            mEnterTestLayout.setVisibility(View.GONE);
            mTestLayout.setVisibility(View.GONE);
        }

        if (mIsFinish) {
            mEnterTestLayout.setVisibility(View.GONE);
            mEditLayout.setVisibility(View.GONE);
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskActivity.this.finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initExtraData(Intent intent) {
        mTask = intent.getParcelableExtra(EXTRA_TASK);
        mIsAdjust = mTask.isAdjust();
        mIsFinish = mTask.isFinish();
    }

}
