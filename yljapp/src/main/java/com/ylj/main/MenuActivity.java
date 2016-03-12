package com.ylj.main;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.ylj.R;
import com.ylj.common.BaseActivity;
import com.ylj.common.bean.Task;
import com.ylj.common.config.AppStatus;
import com.ylj.connect.ConnectActivity;
import com.ylj.setting.UserActivity;
import com.ylj.staff.StaffManagerActivity;
import com.ylj.task.TaskManagerActivity;
import com.ylj.task.TaskModifyActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

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

    @Event(R.id.rl_task)
    private void onTaskLayoutClick(View view){
        TaskModifyActivity.startAsShowTaskActivity(this,new Task());
    }

    @Event(R.id.rl_hisdata)
    private void onHisdataLayoutClick(View view){
        Intent intent=new Intent(this, TaskManagerActivity.class);
        startActivity(intent);
    }

}
