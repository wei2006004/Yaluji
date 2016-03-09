package com.ylj.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.ylj.R;
import com.ylj.common.BaseActivity;
import com.ylj.connect.ConnectActivity;
import com.ylj.setting.SettingActivity;
import com.ylj.setting.UserActivity;
import com.ylj.staff.StaffManagerActivity;
import com.ylj.task.NewTaskActivity;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Event(R.id.fbt_setting)
    private void onSettingButtonClick(View view){
        Intent intent=new Intent(this,UserActivity.class);
        startActivity(intent);
    }

    @Event(R.id.rl_connect)
    private void onConnectLayoutClick(View view){
        Intent intent=new Intent(x.app(), ConnectActivity.class);
        startActivity(intent);
    }

    @Event(R.id.rl_manager)
    private void onManagerLayoutClick(View view){
        Intent intent=new Intent(x.app(), StaffManagerActivity.class);
        startActivity(intent);
    }

    @Event(R.id.rl_task)
    private void onTaskLayoutClick(View view){
        Intent intent=new Intent(x.app(), NewTaskActivity.class);
        startActivity(intent);
    }

    @Event(R.id.rl_hisdata)
    private void onHisdataLayoutClick(View view){

    }

}
