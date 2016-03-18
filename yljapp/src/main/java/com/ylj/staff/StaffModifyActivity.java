package com.ylj.staff;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylj.R;
import com.ylj.common.BaseActivity;
import com.ylj.common.bean.Staff;
import com.ylj.db.DbLet;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_staff_modify)
public class StaffModifyActivity extends BaseActivity {

    public static final String EXTRA_MODE = "EXTRA_MODE";
    public static final String EXTRA_STAFF = "EXTRA_STAFF";

    public static final int MODE_NEW_STAFF = 0;
    public static final int MODE_MODIFY_STAFF = 1;
    public static final int MODE_SHOW_INFO = 2;

    private int mMode = MODE_NEW_STAFF;
    private Staff mCurrentStaff = new Staff();

    public static void startAsNewStaffActivity(Context context) {
        Intent intent = new Intent(context, StaffModifyActivity.class);
        intent.putExtra(EXTRA_MODE, MODE_NEW_STAFF);
        context.startActivity(intent);
    }

    public static void startAsModifyStaffActivity(Context context, Staff staff) {
        Intent intent = new Intent(context, StaffModifyActivity.class);
        intent.putExtra(EXTRA_MODE, MODE_MODIFY_STAFF);
        intent.putExtra(EXTRA_STAFF, staff);
        context.startActivity(intent);
    }

    public static void startAsShowStaffActivity(Context context, Staff staff) {
        Intent intent = new Intent(context, StaffModifyActivity.class);
        intent.putExtra(EXTRA_MODE, MODE_SHOW_INFO);
        intent.putExtra(EXTRA_STAFF, staff);
        context.startActivity(intent);
    }

    public static void startAsModifyStaffActivityForResult(Activity context, Staff staff, int requestCode) {
        Intent intent = new Intent(context, StaffModifyActivity.class);
        intent.putExtra(EXTRA_MODE, MODE_MODIFY_STAFF);
        intent.putExtra(EXTRA_STAFF, staff);
        context.startActivityForResult(intent, requestCode);
    }

    public static void startAsShowStaffActivityForResult(Activity context, Staff staff, int requestCode) {
        Intent intent = new Intent(context, StaffModifyActivity.class);
        intent.putExtra(EXTRA_MODE, MODE_SHOW_INFO);
        intent.putExtra(EXTRA_STAFF, staff);
        context.startActivityForResult(intent, requestCode);
    }

    @ViewInject(R.id.et_name)
    EditText mNameEdit;

    @ViewInject(R.id.et_company)
    EditText mCompanyEdit;

    @ViewInject(R.id.et_group)
    EditText mGroupEdit;

    @ViewInject(R.id.tv_name)
    TextView mNameText;

    @ViewInject(R.id.tv_company)
    TextView mCompanyText;

    @ViewInject(R.id.tv_group)
    TextView mGroupText;

    @ViewInject(R.id.toolbar)
    Toolbar mToolbar;

    @ViewInject(R.id.layout_info)
    LinearLayout mInfoLayout;

    @ViewInject(R.id.layout_edit)
    LinearLayout mEditLayout;

    @Event(R.id.fab)
    private void onFabClick(View view) {
        switch (mMode) {
            case MODE_SHOW_INFO:
                showModifyView();
                mMode = MODE_MODIFY_STAFF;
                break;
            case MODE_MODIFY_STAFF:
                updateStaff();
                setResultAndFinish();
                break;
            case MODE_NEW_STAFF:
                saveNewStaff();
                finish();
            default:
                break;
        }
    }

    private void setResultAndFinish() {
        Intent intent =new Intent();
        intent.putExtra(EXTRA_STAFF,mCurrentStaff);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    private void updateStaff() {
        fillStaffByEdit();
        DbLet.updateStaff(mCurrentStaff);
    }

    private void saveNewStaff() {
        //// TODO: 2016/3/8 0008 名字是否为空
        fillStaffByEdit();
        DbLet.addStaff(mCurrentStaff);
    }

    private void fillStaffByEdit() {
        mCurrentStaff.setStaffName(mNameEdit.getText().toString());
        mCurrentStaff.setCompany(mCompanyEdit.getText().toString());
        mCurrentStaff.setGroup(mGroupEdit.getText().toString());
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
            case MODE_SHOW_INFO:
                showInfoView();
                break;
            case MODE_MODIFY_STAFF:
                showModifyView();
                break;
            case MODE_NEW_STAFF:
                showNewStaffView();
            default:
                break;
        }
    }

    private void showNewStaffView() {
        setEditLayout();
        //// TODO: 2016/3/8 0008 tab样式
    }

    private void showModifyView() {
        setEditLayout();
        fillEditText();
    }

    private void fillEditText() {
        mNameEdit.setText(mCurrentStaff.getStaffName());
        mCompanyEdit.setText(mCurrentStaff.getCompany());
        mGroupEdit.setText(mCurrentStaff.getGroup());
    }

    private void showInfoView() {
        setInfoLayout();
        fillInfoText();
    }

    private void fillInfoText() {
        mNameText.setText(mCurrentStaff.getStaffName());
        mCompanyText.setText(mCurrentStaff.getCompany());
        mGroupText.setText(mCurrentStaff.getGroup());
    }

    private void setEditLayout() {
        mInfoLayout.setVisibility(View.GONE);
        mEditLayout.setVisibility(View.VISIBLE);
    }

    private void setInfoLayout() {
        mEditLayout.setVisibility(View.GONE);
        mInfoLayout.setVisibility(View.VISIBLE);
    }

    private void initToolbar() {
        switch (mMode) {
            case MODE_SHOW_INFO:
                mToolbar.setTitle(R.string.staff_title_info);
                break;
            case MODE_MODIFY_STAFF:
                mToolbar.setTitle(R.string.staff_title_modify);
                break;
            case MODE_NEW_STAFF:
                mToolbar.setTitle(R.string.staff_title_new);
            default:
                break;
        }
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaffModifyActivity.this.finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initExtraData() {
        Intent intent = getIntent();
        mMode = intent.getIntExtra(EXTRA_MODE, MODE_NEW_STAFF);
        if (mMode != MODE_NEW_STAFF) {
            mCurrentStaff = intent.getParcelableExtra(EXTRA_STAFF);
        }
    }

}
