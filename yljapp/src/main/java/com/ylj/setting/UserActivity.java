package com.ylj.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ylj.R;
import com.ylj.common.BaseActivity;
import com.ylj.common.bean.Admin;
import com.ylj.common.bean.Staff;
import com.ylj.common.config.AppStatus;
import com.ylj.connect.bean.DeviceInfo;
import com.ylj.db.LoginLet;
import com.ylj.main.LoginActivity;
import com.ylj.staff.AdminModifyActivity;
import com.ylj.staff.StaffModifyActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_user)
public class UserActivity extends BaseActivity {

    public static final int MODE_ANONYMOUS_LOGIN = 0;
    public static final int MODE_STAFF_LOGIN = 1;
    public static final int MODE_ADMIN_LOGIN = 2;

    public static final int CONNECT_MODE_NONE = 0;
    public static final int CONNECT_MODE_BLUETOOTH = 1;
    public static final int CONNECT_MODE_WIFI = 2;

    public static final int ACTIVITY_REQUEST_ADMIN = 1;
    public static final int ACTIVITY_REQUEST_STAFF = 2;

    public static final String EXTRA_ADMIN = "EXTRA_ADMIN";
    public static final String EXTRA_STAFF = "EXTRA_STAFF";

    int mMode = MODE_ANONYMOUS_LOGIN;
    Staff mStaff;
    Admin mAdmin;

    boolean mIsConnect = false;
    DeviceInfo mCurrentDevice;
    boolean mIsDeviceNormal;
    int mConnectMode = CONNECT_MODE_NONE;

    @ViewInject(R.id.tv_name)
    TextView mNameText;

    @ViewInject(R.id.tv_admin_info)
    TextView mAdminInfoText;

    @ViewInject(R.id.tv_company)
    TextView mCompanyText;

    @ViewInject(R.id.tv_conncet_status)
    TextView mConnectStatusText;

    @ViewInject(R.id.tv_conncet_mode)
    TextView mConnectModeText;

    @ViewInject(R.id.tv_device_status)
    TextView mDeviceStatusText;

    @ViewInject(R.id.tv_conncet_device)
    TextView mConnectDeviceText;

    @ViewInject(R.id.layout_connceted)
    LinearLayout mConnectedLayout;

    @ViewInject(R.id.layout_anonymous)
    RelativeLayout mAnonymousLayout;

    @ViewInject(R.id.layout_user_info)
    LinearLayout mUserInfoLayout;

    @ViewInject(R.id.fab)
    FloatingActionButton mFabButtion;

    @Event(R.id.fab)
    private void onFabClick(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @Event(R.id.image_user)
    private void onUserImageClick(View view) {
        if (mMode == MODE_ADMIN_LOGIN) {
            AdminModifyActivity.startAsShowAdminActivityForResult(this, mAdmin, ACTIVITY_REQUEST_ADMIN);
        }
        if (mMode == MODE_STAFF_LOGIN) {
            StaffModifyActivity.startAsShowStaffActivityForResult(this, mStaff, ACTIVITY_REQUEST_STAFF);
        }
    }

    @Event(R.id.btn_user_edit)
    private void onUserEditClick(View view) {
        if (mMode == MODE_ADMIN_LOGIN) {
            AdminModifyActivity.startAsModifyAdminActivityForResult(this, mAdmin, ACTIVITY_REQUEST_ADMIN);
        }
        if (mMode == MODE_STAFF_LOGIN) {
            StaffModifyActivity.startAsModifyStaffActivityForResult(this, mStaff, ACTIVITY_REQUEST_STAFF);
        }
    }

    @Event(R.id.btn_logout)
    private void onLogoutClick(View view) {
        LoginLet.doLogout();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case ACTIVITY_REQUEST_ADMIN:
                mAdmin = data.getParcelableExtra(EXTRA_ADMIN);
                refreshViews();
                AppStatus.instance().setCurrentAdmin(mAdmin);
                break;
            case ACTIVITY_REQUEST_STAFF:
                mStaff = data.getParcelableExtra(EXTRA_STAFF);
                refreshViews();
                AppStatus.instance().setCurrentStaff(mStaff);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        initToolbar();
        initContentView();
    }

    private void initData() {
        AppStatus appStatus = AppStatus.instance();
        mMode = appStatus.getLoginMode();
        if (appStatus.isLogin()) {
            if (appStatus.isAdmin()) {
                mAdmin = appStatus.getCurrentAdmin();
            } else {
                mStaff = appStatus.getCurrentStaff();
            }
        }
        mIsConnect = appStatus.isConnect();
        if (mIsConnect) {
            mConnectMode = appStatus.getConnectMode();
            mCurrentDevice = appStatus.getCurrentDevice();
            mIsDeviceNormal = appStatus.isDeviceNormal();
        }
    }

    private void initContentView() {
        adjustLayout();
        refreshViews();
    }

    private void refreshViews() {
        switch (mMode) {
            case MODE_ANONYMOUS_LOGIN:
                break;
            case MODE_STAFF_LOGIN:
                mNameText.setText(mStaff.getStaffName());
                mCompanyText.setText(mStaff.getCompany());
                break;
            case MODE_ADMIN_LOGIN:
                mNameText.setText(mAdmin.getAdminName());
                mCompanyText.setText(mAdmin.getCompany());
                break;
        }

        if (mIsConnect) {
            mConnectStatusText.setText("connected");
            mConnectDeviceText.setText(mCurrentDevice.getDeviceId());
            if (mConnectMode == CONNECT_MODE_BLUETOOTH) {
                mConnectModeText.setText("bluetooth");
            } else {
                mConnectModeText.setText("WIFI");
            }
            if (mIsDeviceNormal) {
                mDeviceStatusText.setText("nomal");
            } else {
                mDeviceStatusText.setText("error");
            }

        } else {
            mConnectStatusText.setText("not connect");
        }
    }

    private void adjustLayout() {
        if (mMode == MODE_ADMIN_LOGIN) {
            mFabButtion.setVisibility(View.VISIBLE);
            mAdminInfoText.setVisibility(View.VISIBLE);
        } else {
            mAdminInfoText.setVisibility(View.GONE);
            mFabButtion.setVisibility(View.GONE);
        }
        if (mMode == MODE_ANONYMOUS_LOGIN) {
            mAnonymousLayout.setVisibility(View.VISIBLE);
            mUserInfoLayout.setVisibility(View.GONE);
        } else {
            mUserInfoLayout.setVisibility(View.VISIBLE);
            mAnonymousLayout.setVisibility(View.GONE);
        }
        if (mIsConnect) {
            mConnectedLayout.setVisibility(View.VISIBLE);
        } else {
            mConnectedLayout.setVisibility(View.GONE);
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
