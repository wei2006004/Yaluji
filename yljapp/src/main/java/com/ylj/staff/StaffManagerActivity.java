package com.ylj.staff;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.ylj.R;
import com.ylj.common.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_staff_manager)
public class StaffManagerActivity extends BaseActivity {

    @ViewInject(R.id.lv_admin)
    ListView mAdminListView;

    @ViewInject(R.id.lv_staff)
    ListView mStaffListView;

    @Event(R.id.btn_admin_edit)
    private void onAdminEditClick(View view){
        Intent intent=new Intent(x.app(),AdminModifyActivity.class);
        startActivity(intent);
    }

    @Event(R.id.btn_add_staff)
    private void onStaffAddClick(View view){
        Intent intent=new Intent(x.app(),StaffModifyActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
