package com.ylj.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.ylj.R;
import com.ylj.common.BaseActivity;
import com.ylj.common.bean.Task;
import com.ylj.common.config.AppStatus;
import com.ylj.common.utils.BeanUtils;
import com.ylj.connect.ConnectActivity;
import com.ylj.db.DbLet;
import com.ylj.setting.UserActivity;
import com.ylj.staff.StaffManagerActivity;
import com.ylj.task.TaskActivity;
import com.ylj.task.TaskManagerActivity;
import com.ylj.task.TaskModifyActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_menu)
public class MenuActivity extends BaseActivity {

    @ViewInject(R.id.rl_connect)
    private RelativeLayout mConnectLayout;

    @ViewInject(R.id.rl_hisdata)
    private RelativeLayout mHisdataLayout;

    @ViewInject(R.id.rl_task)
    private RelativeLayout mTaskLayout;

    @ViewInject(R.id.rl_manager)
    private RelativeLayout mManagerLayout;

    @Override
    protected void onResume(){
        super.onResume();
        adjustLayout();
    }

    private void adjustLayout() {
        AppStatus appStatus=AppStatus.instance();
        if(appStatus.isAdmin()){
            mManagerLayout.setVisibility(View.VISIBLE);
        }else {
            mManagerLayout.setVisibility(View.GONE);
        }
    }

    @Event(R.id.fbt_setting)
    private void onSettingButtonClick(View view){
        Intent intent=new Intent(this,UserActivity.class);
        startActivity(intent);
    }

    @Event(R.id.rl_connect)
    private void onConnectLayoutClick(View view){
        Intent intent=new Intent(this, ConnectActivity.class);
        startActivity(intent);
    }

    @Event(R.id.rl_manager)
    private void onManagerLayoutClick(View view){
        Intent intent=new Intent(this, StaffManagerActivity.class);
        startActivity(intent);
    }

    List<Map<String, Object>> mTaskMaps = new ArrayList<>();

    @Event(R.id.rl_task)
    private void onTaskLayoutClick(View view){
        x.task().run(new Runnable() {
            @Override
            public void run() {
                mTaskMaps.clear();
                List<Task> list = DbLet.getNotFinishTaskList();
                mTaskMaps.addAll(BeanUtils.convertTasks2Maps(list));
                x.task().post(new Runnable() {
                    @Override
                    public void run() {
                        if (mTaskMaps.isEmpty()) {
                            TaskModifyActivity.startAsNewTaskActivity(MenuActivity.this);
                        } else {
                            showNoFinishDialog();
                        }
                    }
                });
            }
        });
    }

    private void showNoFinishDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Task list");
        ListAdapter taskAdapter =createNoFinishTaskAdapter();
        builder.setSingleChoiceItems(taskAdapter, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onTaskItemClick(which);
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("New Task", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TaskModifyActivity.startAsNewTaskActivity(MenuActivity.this);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private ListAdapter createNoFinishTaskAdapter() {
        return new SimpleAdapter(this, mTaskMaps,
                R.layout.listview_dialog_no_finish_task,
                new String[]{Task.TAG_TASK_NAME, Task.TAG_ROAD_NAME},
                new int[]{R.id.tv_task_name, R.id.tv_road_name}) {
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (isTaskAdjust(position)) {
                    view.findViewById(R.id.tv_no_finish).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.tv_no_adjust).setVisibility(View.GONE);
                } else {
                    view.findViewById(R.id.tv_no_adjust).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.tv_no_finish).setVisibility(View.GONE);
                }
                return view;
            }
        };
    }

    private void onTaskItemClick(int position) {
        Task task = getTaskByPosition(position);
        TaskActivity.startNewActivity(this, task);
    }

    private boolean isTaskAdjust(int position) {
        Task task = getTaskByPosition(position);
        return task.isAdjust();
    }

    private Task getTaskByPosition(int position) {
        Map<String, Object> map = mTaskMaps.get(position);
        Task task = Task.createByMap(map);
        return task;
    }

    @Event(R.id.rl_hisdata)
    private void onHisdataLayoutClick(View view){
        Intent intent=new Intent(this, TaskManagerActivity.class);
        startActivity(intent);
    }

}
