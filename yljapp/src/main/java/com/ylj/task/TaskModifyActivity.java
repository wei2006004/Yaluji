package com.ylj.task;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylj.R;
import com.ylj.adjust.AdjustActivity;
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

    @ViewInject(R.id.tv_origin_edit)
    TextView mOriginEditText;

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
    LinearLayout mSaveAdjustLayout;

    @ViewInject(R.id.layout_enter_adjust)
    LinearLayout mEnterAdjustLayout;

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
    private void onOriginLayoutClick(View view){
        showSingleChoiceDialog(getString(R.string.task_origin_setting),
                new String[]{getString(R.string.task_clockwise), getString(R.string.task_anticlockwise)},
                mTask.getOrigin(),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTask.setOrigin(which);
                        switch (which) {
                            case Task.ORIGIN_ANTICLOCKWISE:
                                mOriginEditText.setText(R.string.task_anticlockwise);
                                mOriginText.setText(R.string.task_anticlockwise);
                                break;
                            case Task.ORIGIN_CLOCKWISE:
                                mOriginEditText.setText(R.string.task_clockwise);
                                mOriginText.setText(R.string.task_clockwise);
                                break;
                        }
                        dialog.dismiss();
                    }
                });
    }

    @Event(R.id.layout_road_width)
    private void onRoadWidthLayoutClick(View view){
        showDoubleEditDialog("road width setting", mTask.getRoadWidth(),
                new OnButtonClick<Double>() {
                    @Override
                    public void onConfirm(DialogInterface dialog, Double result) {
                        mTask.setRoadWidth(result);
                        mRoadWidthText.setText(String.valueOf(result));
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
    }

    @Event(R.id.layout_road_length)
    private void onRoadLengthLayoutClick(View view){
        showDoubleEditDialog("road length setting", mTask.getRoadLength(),
                new OnButtonClick<Double>() {
                    @Override
                    public void onConfirm(DialogInterface dialog, Double result) {
                        mTask.setRoadLength(result);
                        mRoadLengthText.setText(String.valueOf(result));
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
    }

    @Event(R.id.layout_roller_width)
    private void onRollerWidthLayoutClick(View view){
        showDoubleEditDialog("roller width setting", mTask.getRollerWidth(),
                new OnButtonClick<Double>() {
                    @Override
                    public void onConfirm(DialogInterface dialog, Double result) {
                        mTask.setRollerWidth(result);
                        mRollerWidthText.setText(String.valueOf(result));
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
    }

    @Event(R.id.layout_roller_diameter)
    private void onRollerDiameterLayoutClick(View view){
        showDoubleEditDialog("roller diameter setting", mTask.getRollerDiameter(),
                new OnButtonClick<Double>() {
                    @Override
                    public void onConfirm(DialogInterface dialog, Double result) {
                        mTask.setRoadWidth(result);
                        mRollerDiameterText.setText(String.valueOf(result));
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
    }

    @Event(R.id.layout_huoer_num)
    private void onHuoerNumLayoutClick(View view){
        showIntEditDialog("huoer num setting", mTask.getHuoerNum(),
                new OnButtonClick<Integer>() {
                    @Override
                    public void onConfirm(DialogInterface dialog, Integer result) {
                        mTask.setHuoerNum(result);
                        mHuoerNumText.setText(String.valueOf(result));
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
    }

    @Event(R.id.btn_save)
    private void onSaveButtonClick(View view){
        updateTaskByView();
        saveTask();
    }

    @Event(R.id.btn_enter_adjust)
    private void onEnterAdjustButtonClick(View view){
        updateTaskByView();
        switchToAdjustActivity();
        finish();
    }

    @Event(R.id.btn_save_and_adjust)
    private void onAdjustButtonClick(View view){
        updateTaskByView();
        saveTask();
        switchToAdjustActivity();
        finish();
    }

    private void switchToAdjustActivity() {
        AdjustActivity.startNewActivity(this,mTask);
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
                updateTaskByView();
                saveTask();
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

    private void saveTask() {
        DbLet.saveOrUpdateTask(mTask);
        showToast("task saved");
    }

    private void updateTaskByView() {
        mTask.setTaskName(mTaskNameEdit.getText().toString());
        mTask.setRoadName(mRoadNameEdit.getText().toString());
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
                mOriginEditText.setText(R.string.task_anticlockwise);
                mOriginText.setText(R.string.task_anticlockwise);
                break;
            case Task.ORIGIN_CLOCKWISE:
                mOriginEditText.setText(R.string.task_clockwise);
                mOriginText.setText(R.string.task_clockwise);
                break;
        }
        mRoadWidthText.setText(String.valueOf(mTask.getRoadWidth()));
        mRoadLengthText.setText(String.valueOf(mTask.getRoadLength()));
        mRollerWidthText.setText(String.valueOf(mTask.getRollerWidth()));
        mRollerDiameterText.setText(String.valueOf(mTask.getRollerDiameter()));
        mHuoerNumText.setText(String.valueOf(mTask.getHuoerNum()));
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
        mSaveAdjustLayout.setVisibility(View.VISIBLE);
        mFabButtion.setVisibility(View.GONE);
        mEnterAdjustLayout.setVisibility(View.GONE);
    }

    private void setModifyLayout() {
        mEditLayout.setVisibility(View.VISIBLE);
        mInfoLayout.setVisibility(View.GONE);
        mEnterAdjustLayout.setVisibility(View.GONE);
        if(!mTask.isAdjust()){
            mSaveAdjustLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setInfoLayout() {
        mInfoLayout.setVisibility(View.VISIBLE);
        mEditLayout.setVisibility(View.GONE);
        mSaveAdjustLayout.setVisibility(View.GONE);
        if(!mTask.isAdjust()){
            mEnterAdjustLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initToolbar() {
        switch (mMode) {
            case MODE_SHOW_INFO:
                mToolbar.setTitle(R.string.title_activity_task_info);
                break;
            case MODE_MODIFY_TASK:
                mToolbar.setTitle(R.string.title_activity_modify_task);
                break;
            case MODE_NEW_TASK:
                mToolbar.setTitle(R.string.title_activity_new_task);
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
