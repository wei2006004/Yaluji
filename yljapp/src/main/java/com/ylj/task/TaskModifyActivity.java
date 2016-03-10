package com.ylj.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylj.R;
import com.ylj.common.BaseActivity;
import com.ylj.common.bean.Task;
import com.ylj.db.DbLet;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_task_modify)
public class TaskModifyActivity extends BaseActivity {

    public static final String EXTRA_MODE = "EXTRA_MODE";
    public static final String EXTRA_TASK = "EXTRA_TASK";

    public static final int MODE_NEW_TASK = 0;
    public static final int MODE_MODIFY_TASK = 1;
    public static final int MODE_SHOW_INFO = 2;
    public static final int MODE_SHOW_INFO_ONLY = 4;

    private int mMode = MODE_NEW_TASK;
    private Task mTask = new Task();

    public static void startAsNewTaskActivity(Context context) {
        Intent intent = new Intent(context, TaskModifyActivity.class);
        intent.putExtra(EXTRA_MODE, MODE_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startAsModifyTaskActivity(Context context, Task task) {
        Intent intent = new Intent(context, TaskModifyActivity.class);
        intent.putExtra(EXTRA_MODE, MODE_MODIFY_TASK);
        intent.putExtra(EXTRA_TASK, task);
        context.startActivity(intent);
    }

    public static void startAsShowTaskActivity(Context context, Task task) {
        Intent intent = new Intent(context, TaskModifyActivity.class);
        intent.putExtra(EXTRA_MODE, MODE_SHOW_INFO);
        intent.putExtra(EXTRA_TASK, task);
        context.startActivity(intent);
    }

    public static void startAsShowOnlyTaskActivity(Context context, Task task) {
        Intent intent = new Intent(context, TaskModifyActivity.class);
        intent.putExtra(EXTRA_MODE, MODE_SHOW_INFO_ONLY);
        intent.putExtra(EXTRA_TASK, task);
        context.startActivity(intent);
    }

    public static void startAsModifyTaskActivityForResult(Activity context, Task task, int requestCode) {
        Intent intent = new Intent(context, TaskModifyActivity.class);
        intent.putExtra(EXTRA_MODE, MODE_MODIFY_TASK);
        intent.putExtra(EXTRA_TASK, task);
        context.startActivityForResult(intent, requestCode);
    }

    public static void startAsShowTaskActivityForResult(Activity context, Task task, int requestCode) {
        Intent intent = new Intent(context, TaskModifyActivity.class);
        intent.putExtra(EXTRA_MODE, MODE_SHOW_INFO);
        intent.putExtra(EXTRA_TASK, task);
        context.startActivityForResult(intent, requestCode);
    }

    @ViewInject(R.id.tv_task_name)
    TextView mTaskNameText;

    @ViewInject(R.id.tv_road_name)
    TextView mRoadNameText;

    @ViewInject(R.id.et_task_name)
    EditText mTaskNameEdit;

    @ViewInject(R.id.et_road_name)
    EditText mRoadNameEdit;

    @ViewInject(R.id.tv_road_width)
    TextView mRoadWidthText;

    @ViewInject(R.id.tv_road_length)
    TextView mRoadLengthText;

    @ViewInject(R.id.tv_roller_width)
    TextView mRollerWidthText;

    @ViewInject(R.id.tv_roller_diameter)
    TextView mRollerDiameterText;

    @ViewInject(R.id.tv_origin)
    TextView mOriginText;

    @ViewInject(R.id.tv_huoer_num)
    TextView mHuoerNumText;

    @ViewInject(R.id.toolbar)
    Toolbar mToolbar;

    @ViewInject(R.id.layout_info)
    LinearLayout mInfoLayout;

    @ViewInject(R.id.layout_edit)
    LinearLayout mEditLayout;

    @ViewInject(R.id.layout_common)
    LinearLayout mCommonLayout;

    @ViewInject(R.id.layout_save_and_adjust)
    LinearLayout mAdjustLayout;

    @ViewInject(R.id.fab)
    FloatingActionButton mFabButtion;

    @ViewInject(R.id.layout_edit_origin)
    LinearLayout mOriginLayout;

    @ViewInject(R.id.layout_road_width)
    LinearLayout mRoadWidthLayout;

    @ViewInject(R.id.layout_road_length)
    LinearLayout mRoadLengthLayout;

    @ViewInject(R.id.layout_roller_width)
    LinearLayout mRollerWidthLayout;

    @ViewInject(R.id.layout_roller_diameter)
    LinearLayout mRollerDiameterLayout;

    @ViewInject(R.id.layout_huoer_num)
    LinearLayout mHuoerNumLayout;

    @Event(R.id.layout_edit_origin)
    private void mOriginLayoutClick(View view){
    }

    @Event(R.id.layout_road_width)
    private void mRoadWidthLayoutClick(View view){
        mTask.setRoadWidth(4);
        mRoadWidthText.setText("4");
    }

    @Event(R.id.layout_road_length)
    private void mRoadLengthLayoutClick(View view){

    }

    @Event(R.id.layout_roller_width)
    private void mRollerWidthLayoutClick(View view){

    }

    @Event(R.id.layout_roller_diameter)
    private void mRollerDiameterLayoutClick(View view){

    }

    @Event(R.id.layout_huoer_num)
    private void mHuoerNumLayoutClick(View view){

    }

    @Event(R.id.btn_save)
    private void onSaveButtonClick(){
        updateTask();
    }

    @Event(R.id.btn_save_and_adjust)
    private void onAdjustButtonClick(){
        updateTask();
        //// TODO: 2016/3/10 0010 跳转
        finish();
    }
    
    @Event(R.id.fab)
    private void onFabClick(View view) {
        switch (mMode) {
            case MODE_SHOW_INFO:
                showModifyView();
                enableEditListener(true);
                mMode = MODE_MODIFY_TASK;
                break;
            case MODE_MODIFY_TASK:
                updateTask();
                setResultAndFinish();
                break;
            default:
                break;
        }
    }

    private void setResultAndFinish() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TASK, mTask);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void updateTask() {
        mTask.setTaskName(mTaskNameEdit.getText().toString());
        mTask.setRoadName(mRoadNameEdit.getText().toString());
        DbLet.saveOrUpdateTask(mTask);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initExtraData();
        initToolbar();
        initContentView();
    }

    private void initContentView() {
        switch (mMode) {
            case MODE_SHOW_INFO_ONLY:
                mFabButtion.setVisibility(View.GONE);
            case MODE_SHOW_INFO:
                showInfoView();
                enableEditListener(false);
                break;
            case MODE_MODIFY_TASK:
                showModifyView();
                enableEditListener(true);
                break;
            case MODE_NEW_TASK:
                showNewTaskView();
                enableEditListener(true);
            default:
                break;
        }
    }

    private void enableEditListener(boolean editable) {
        mOriginLayout.setClickable(editable);
        mRoadWidthLayout.setClickable(editable);
        mRoadLengthLayout.setClickable(editable);
        mRollerWidthLayout.setClickable(editable);
        mRollerDiameterLayout.setClickable(editable);
        mHuoerNumLayout.setClickable(editable);
    }

    private void showNewTaskView() {
        setNewLayout();
        fillViews();
    }

    private void fillViews() {
        mTaskNameText.setText(mTask.getTaskName());
        mTaskNameEdit.setText(mTask.getTaskName());
        mRoadNameText.setText(mTask.getRoadName());
        mRoadNameEdit.setText(mTask.getRoadName());
        switch (mTask.getOrigin()){
            case Task.ORIGIN_ANTICLOCKWISE:
                mOriginText.setText("ANTICLOCKWISE");
                break;
            case Task.ORIGIN_CLOCKWISE:
                mOriginText.setText("CLOCKWISE");
                break;
        }
        mRoadWidthText.setText(String.valueOf(mTask.getRoadWidth()));
        mRoadLengthText.setText(String.valueOf(mTask.getRoadLength()));
        mRollerWidthText.setText(String.valueOf(mTask.getRollerWidth()));
        mRollerDiameterText.setText(String.valueOf(mTask.getRollerDiameter()));
        mOriginText.setText(String.valueOf(mTask.getOrigin()));
    }

    private void showModifyView() {
        setModifyLayout();
        fillViews();
    }

    private void showInfoView() {
        setInfoLayout();
        fillViews();
    }

    private void setNewLayout() {
        mEditLayout.setVisibility(View.VISIBLE);
        mInfoLayout.setVisibility(View.GONE);
        mAdjustLayout.setVisibility(View.VISIBLE);
        mFabButtion.setVisibility(View.GONE);
    }

    private void setModifyLayout() {
        mEditLayout.setVisibility(View.VISIBLE);
        mInfoLayout.setVisibility(View.GONE);
        mAdjustLayout.setVisibility(View.GONE);
    }

    private void setInfoLayout() {
        mInfoLayout.setVisibility(View.VISIBLE);
        mEditLayout.setVisibility(View.GONE);
        mAdjustLayout.setVisibility(View.GONE);
    }

    private void initToolbar() {
        switch (mMode) {
            case MODE_SHOW_INFO:
                mToolbar.setTitle("task info");
                break;
            case MODE_MODIFY_TASK:
                mToolbar.setTitle("modify task");
                break;
            case MODE_NEW_TASK:
                mToolbar.setTitle("new task");
            default:
                break;
        }

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskModifyActivity.this.finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initExtraData() {
        Intent intent = getIntent();
        mMode = intent.getIntExtra(EXTRA_MODE, MODE_NEW_TASK);
        if (mMode != MODE_NEW_TASK) {
            mTask = intent.getParcelableExtra(EXTRA_TASK);
        }else {
            mTask = new Task();
        }
    }

}
