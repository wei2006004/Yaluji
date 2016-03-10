package com.ylj.staff;

import android.content.DialogInterface;
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
import android.widget.TextView;

import com.ylj.R;
import com.ylj.common.BaseActivity;
import com.ylj.common.bean.Admin;
import com.ylj.common.bean.Staff;
import com.ylj.common.config.AppStatus;
import com.ylj.common.utils.BeanUtils;
import com.ylj.db.DbLet;
import com.ylj.db.LoginLet;
import com.ylj.main.LoginActivity;

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

    public static final int ACTIVITY_REQUEST_ADMIN = 1;
    public static final int ACTIVITY_REQUEST_MODIFY_PASSWD = 2;

    public static final String EXTRA_ADMIN = "EXTRA_ADMIN";

    Admin mAdmin;

    SimpleAdapter mAdminAdapter;
    SimpleAdapter mStaffAdapter;

    List<Map<String, Object>> mAdminMaps = new ArrayList<>();
    List<Map<String, Object>> mStaffMaps = new ArrayList<>();

    @ViewInject(R.id.tv_admin_name)
    TextView mNameText;

    @ViewInject(R.id.tv_admin_company)
    TextView mCompanyText;

    @ViewInject(R.id.lv_admin)
    ListView mAdminListView;

    @ViewInject(R.id.lv_staff)
    ListView mStaffListView;

    @Event(R.id.rl_admin_info)
    private void onAdminInfoLayoutClick(View view) {
        AdminModifyActivity.startAsShowAdminActivityForResult(this, mAdmin, ACTIVITY_REQUEST_ADMIN);
    }

    @Event(R.id.btn_edit_passwd)
    private void onAdminPasswdEditClick(View view) {
        PasswdModifyActivity.startAsModifyPasswdActivityForResult(this, mAdmin, ACTIVITY_REQUEST_MODIFY_PASSWD);
    }

    @Event(R.id.btn_logout)
    private void onLogoutClick(View view) {
        LoginLet.doLogout();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Event(R.id.btn_admin_edit)
    private void onAdminEditClick(View view) {
        AdminModifyActivity.startAsModifyAdminActivityForResult(this, mAdmin, ACTIVITY_REQUEST_ADMIN);
    }

    @Event(R.id.btn_add_admin)
    private void onAdminAddClick(View view) {
        AdminModifyActivity.startAsNewAdminActivity(this);
    }

    @Event(R.id.btn_add_staff)
    private void onStaffAddClick(View view) {
        StaffModifyActivity.startAsNewStaffActivity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case ACTIVITY_REQUEST_ADMIN:
                mAdmin = data.getParcelableExtra(EXTRA_ADMIN);
                AppStatus.instance().setCurrentAdmin(mAdmin);
                refreshInfoLayout();
                break;
            case ACTIVITY_REQUEST_MODIFY_PASSWD:
                mAdmin = data.getParcelableExtra(EXTRA_ADMIN);
                AppStatus.instance().setCurrentAdmin(mAdmin);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToobar();
        initStaffListView();
        initAdminListView();

        initInfoData();
        refreshInfoLayout();
    }

    private void refreshInfoLayout() {
        mNameText.setText(mAdmin.getAdminName());
        mCompanyText.setText(mAdmin.getCompany());
    }

    private void initInfoData() {
        AppStatus appStatus = AppStatus.instance();
        mAdmin = appStatus.getCurrentAdmin();
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
        StaffModifyActivity.startAsShowStaffActivity(this, staff);
    }

    private void deleteStaff(final int position) {
        showAlert("Warning", "Do you want to delete this staff?",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Staff staff = getStaffByPosition(position);
                        DbLet.deleteStaff(staff);
                        refreshStaffData();
                        dialog.dismiss();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    private Staff getStaffByPosition(int position) {
        Map<String, Object> map = mStaffMaps.get(position);
        return Staff.createByMap(map);
    }

    private void initAdminListView() {
        mAdminAdapter = new SimpleAdapter(x.app(), mAdminMaps,
                R.layout.listview_manager_admin,
                new String[]{Admin.TAG_ADMIN_NAME, Admin.TAG_ACCOUNT_NAME, Admin.TAG_COMPANY, Admin.TAG_GROUP},
                new int[]{R.id.tv_name, R.id.tv_account_name, R.id.tv_company, R.id.tv_group}) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                Button deleteBtn = (Button) view.findViewById(R.id.btn_detele);
                Button editBtn = (Button) view.findViewById(R.id.btn_edit);
                if (isCurrentAdminPosition(position)) {
                    deleteBtn.setVisibility(View.GONE);
                    editBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editAdminForResult(position);
                        }
                    });
                } else {
                    deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteAdmin(position);
                        }
                    });
                    editBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editAdmin(position);
                        }
                    });
                }
                return view;
            }
        };
        mAdminListView.setAdapter(mAdminAdapter);
        mAdminListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onAdminItemClick(position);
            }
        });
    }

    private void editAdminForResult(int position) {
        Admin admin = getAdminByPosition(position);
        AdminModifyActivity.startAsModifyAdminActivityForResult(this, admin, ACTIVITY_REQUEST_ADMIN);
    }

    private boolean isCurrentAdminPosition(int position) {
        Admin admin = getAdminByPosition(position);
        String accountName = admin.getAccountName();
        String name = admin.getAdminName();
        if (accountName.equals(mAdmin.getAccountName())) {
            if (name.equals(mAdmin.getAdminName())) {
                return true;
            }
        }
        return false;
    }

    private void editAdmin(int position) {
        Admin admin = getAdminByPosition(position);
        AdminModifyActivity.startAsModifyAdminActivity(this, admin);
    }

    private void deleteAdmin(final int position) {
        showAlert("Warning", "Do you want to delete this admin?",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Admin admin = getAdminByPosition(position);
                        DbLet.deleteAdmin(admin);
                        refreshAdminData();
                        dialog.dismiss();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    private void onAdminItemClick(int position) {
        Admin admin = getAdminByPosition(position);
        if (isCurrentAdminPosition(position)) {
            AdminModifyActivity.startAsShowAdminActivityForResult(this, admin, ACTIVITY_REQUEST_ADMIN);
        } else {
            AdminModifyActivity.startAsShowAdminActivity(this, admin);
        }
    }

    private Admin getAdminByPosition(int position) {
        Map<String, Object> map = mAdminMaps.get(position);
        return Admin.createByMap(map);
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
