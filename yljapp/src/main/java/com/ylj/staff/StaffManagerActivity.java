package com.ylj.staff;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ylj.R;
import com.ylj.common.BaseActivity;
import com.ylj.common.bean.Admin;
import com.ylj.common.bean.Staff;
import com.ylj.common.utils.BeanUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ContentView(R.layout.activity_staff_manager)
public class StaffManagerActivity extends BaseActivity {

    SimpleAdapter mAdminAdapter;
    SimpleAdapter mStaffAdapter;

    List<Map<String, Object>> mAdminMaps = new ArrayList<>();
    List<Map<String, Object>> mStaffMaps = new ArrayList<>();

    @ViewInject(R.id.lv_admin)
    ListView mAdminListView;

    @ViewInject(R.id.lv_staff)
    ListView mStaffListView;

    @Event(R.id.btn_admin_edit)
    private void onAdminEditClick(View view) {
        Intent intent = new Intent(x.app(), AdminModifyActivity.class);
        startActivity(intent);
    }

    @Event(R.id.btn_add_staff)
    private void onStaffAddClick(View view) {
        Intent intent = new Intent(x.app(), StaffModifyActivity.class);
        startActivity(intent);
    }

    @Event(R.id.btn_delete_staff)
    private void onStaffDeleteClick(View view) {
        int pos = mStaffListView.getSelectedItemPosition();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToobar();
        initListView();
    }

    @Override
    protected void onResume(){
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        x.task().run(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    private void initListView() {
        mStaffAdapter = new SimpleAdapter(x.app(), mStaffMaps,
                R.layout.listview_login_staff,
                new String[]{Staff.TAG_STAFF_NAME, Staff.TAG_COMPANY, Staff.TAG_GROUP},
                new int[]{R.id.tv_name, R.id.tv_company, R.id.tv_group});
        mStaffListView.setAdapter(mStaffAdapter);

        mAdminAdapter = new SimpleAdapter(x.app(), mAdminMaps,
                R.layout.listview_login_staff,
                new String[]{Admin.TAG_ADMIN_NAME, Admin.TAG_COMPANY, Admin.TAG_GROUP},
                new int[]{R.id.tv_name, R.id.tv_company, R.id.tv_group});
        mAdminListView.setAdapter(mAdminAdapter);
    }

    private void initToobar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
