package com.ylj.staff;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ylj.R;
import com.ylj.common.BaseActivity;
import com.ylj.common.bean.Admin;
import com.ylj.common.bean.Staff;
import com.ylj.common.utils.BeanUtils;
import com.ylj.db.DbLet;

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
        StaffModifyActivity.startAsNewStaffActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToobar();
        initStaffListView();
        initAdminListView();
    }

    private void initStaffListView() {
        mStaffAdapter = new SimpleAdapter(x.app(), mStaffMaps,
                R.layout.listview_manager_staff,
                new String[]{Staff.TAG_STAFF_NAME, Staff.TAG_COMPANY, Staff.TAG_GROUP},
                new int[]{R.id.tv_name, R.id.tv_company, R.id.tv_group}) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Button deleteBtn = (Button) view.findViewById(R.id.btn_detele);
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteStaff(position);
                    }
                });
                Button editBtn = (Button) view.findViewById(R.id.btn_edit);
                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editStaff(position);
                    }
                });
                return view;
            }
        };
        mStaffListView.setAdapter(mStaffAdapter);
        mStaffListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onStaffItemClick(position);
            }
        });
    }

    private void editStaff(int position) {
        Staff staff = getStaffByPosition(position);
        StaffModifyActivity.startAsModifyStaffActivity(this, staff);
    }

    private void onStaffItemClick(int position) {
        Staff staff = getStaffByPosition(position);
        StaffModifyActivity.startAsShowStaffActivity(this,staff);
    }

    private void deleteStaff(int position) {
        Staff staff = getStaffByPosition(position);
        DbLet.deleteStaff(staff);
        refreshStaffData();
    }

    private Staff getStaffByPosition(int position) {
        Map<String, Object> map = mStaffMaps.get(position);
        return Staff.createByMap(map);
    }

    private void initAdminListView() {
        mAdminAdapter = new SimpleAdapter(x.app(), mAdminMaps,
                R.layout.listview_login_staff,
                new String[]{Admin.TAG_ADMIN_NAME, Admin.TAG_COMPANY, Admin.TAG_GROUP},
                new int[]{R.id.tv_name, R.id.tv_company, R.id.tv_group});
        mAdminListView.setAdapter(mAdminAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshStaffData();
        refreshAdminData();
    }

    private void refreshAdminData() {
        x.task().run(new Runnable() {
            @Override
            public void run() {
                mAdminMaps.clear();
                List<Admin> list = DbLet.getAdminList();
                mAdminMaps.addAll(BeanUtils.convertAdmins2Maps(list));
                x.task().post(new Runnable() {
                    @Override
                    public void run() {
                        mAdminAdapter.notifyDataSetChanged();
                        mAdminListView.setSelection(0);
                    }
                });
            }
        });
    }

    private void refreshStaffData() {
        x.task().run(new Runnable() {
            @Override
            public void run() {
                mStaffMaps.clear();
                List<Staff> list = DbLet.getStaffList();
                mStaffMaps.addAll(BeanUtils.convertStaffs2Maps(list));
                x.task().post(new Runnable() {
                    @Override
                    public void run() {
                        mStaffAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void initToobar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
