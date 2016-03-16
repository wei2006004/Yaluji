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
import com.ylj.common.bean.Admin;
import com.ylj.db.DbLet;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_admin_modify)
public class AdminModifyActivity extends BaseActivity {

    public static final String EXTRA_MODE = "EXTRA_MODE";
    public static final String EXTRA_ADMIN = "EXTRA_ADMIN";

    public static final int MODE_NEW_ADMIN = 0;
    public static final int MODE_MODIFY_ADMIN = 1;
    public static final int MODE_SHOW_INFO = 2;

    private int mMode = MODE_NEW_ADMIN;
    private Admin mCurrentAdmin = new Admin();

    public static void startAsNewAdminActivity(Context context) {
        Intent intent = new Intent(context, AdminModifyActivity.class);
        intent.putExtra(EXTRA_MODE, MODE_NEW_ADMIN);
        context.startActivity(intent);
    }

    public static void startAsModifyAdminActivity(Context context, Admin admin) {
        Intent intent = new Intent(context, AdminModifyActivity.class);
        intent.putExtra(EXTRA_MODE, MODE_MODIFY_ADMIN);
        intent.putExtra(EXTRA_ADMIN, admin);
        context.startActivity(intent);
    }

    public static void startAsShowAdminActivity(Context context, Admin admin) {
        Intent intent = new Intent(context, AdminModifyActivity.class);
        intent.putExtra(EXTRA_MODE, MODE_SHOW_INFO);
        intent.putExtra(EXTRA_ADMIN, admin);
        context.startActivity(intent);
    }

    public static void startAsModifyAdminActivityForResult(Activity context, Admin admin, int requestCode) {
        Intent intent = new Intent(context, AdminModifyActivity.class);
        intent.putExtra(EXTRA_MODE, MODE_MODIFY_ADMIN);
        intent.putExtra(EXTRA_ADMIN, admin);
        context.startActivityForResult(intent, requestCode);
    }

    public static void startAsShowAdminActivityForResult(Activity context, Admin admin, int requestCode) {
        Intent intent = new Intent(context, AdminModifyActivity.class);
        intent.putExtra(EXTRA_MODE, MODE_SHOW_INFO);
        intent.putExtra(EXTRA_ADMIN, admin);
        context.startActivityForResult(intent, requestCode);
    }

    @ViewInject(R.id.et_name)
    EditText mNameEdit;

    @ViewInject(R.id.et_account_name)
    EditText mAccountNameEdit;

    @ViewInject(R.id.et_company)
    EditText mCompanyEdit;

    @ViewInject(R.id.et_group)
    EditText mGroupEdit;

    @ViewInject(R.id.tv_account_name)
    TextView mAccountNameText;

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
                mMode = MODE_MODIFY_ADMIN;
                break;
            case MODE_MODIFY_ADMIN:
                updateAdmin();
                setResultAndFinish();
                break;
            case MODE_NEW_ADMIN:
                turnToSetPasswd();
                finish();
            default:
                break;
        }
    }

    private void setResultAndFinish() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ADMIN, mCurrentAdmin);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void updateAdmin() {
        fillAdminByEdit();
        DbLet.updateAdmin(mCurrentAdmin);
    }

    private void turnToSetPasswd() {
        //// TODO: 2016/3/8 0008 名字是否为空
        fillAdminByEdit();
        PasswdModifyActivity.startAsNewPasswdActivity(this, mCurrentAdmin);
    }

    private void fillAdminByEdit() {
        mCurrentAdmin.setAdminName(mNameEdit.getText().toString());
        mCurrentAdmin.setAccountName(mAccountNameEdit.getText().toString());
        mCurrentAdmin.setCompany(mCompanyEdit.getText().toString());
        mCurrentAdmin.setGroup(mGroupEdit.getText().toString());
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
            case MODE_MODIFY_ADMIN:
                showModifyView();
                break;
            case MODE_NEW_ADMIN:
                showNewAdminView();
            default:
                break;
        }
    }

    private void showNewAdminView() {
        setEditLayout();
        //// TODO: 2016/3/8 0008 tab样式
    }

    private void showModifyView() {
        setEditLayout();
        fillEditText();
    }

    private void fillEditText() {
        mNameEdit.setText(mCurrentAdmin.getAdminName());
        mCompanyEdit.setText(mCurrentAdmin.getCompany());
        mGroupEdit.setText(mCurrentAdmin.getGroup());
        mAccountNameEdit.setText(mCurrentAdmin.getAccountName());
    }

    private void showInfoView() {
        setInfoLayout();
        fillInfoText();
    }

    private void fillInfoText() {
        mNameText.setText(mCurrentAdmin.getAdminName());
        mCompanyText.setText(mCurrentAdmin.getCompany());
        mGroupText.setText(mCurrentAdmin.getGroup());
        mAccountNameText.setText(mCurrentAdmin.getAccountName());
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
                mToolbar.setTitle("admin info");
                break;
            case MODE_MODIFY_ADMIN:
                mToolbar.setTitle("modify admin");
                break;
            case MODE_NEW_ADMIN:
                mToolbar.setTitle("new admin");
            default:
                break;
        }

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminModifyActivity.this.finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initExtraData() {
        Intent intent = getIntent();
        mMode = intent.getIntExtra(EXTRA_MODE, MODE_NEW_ADMIN);
        if (mMode != MODE_NEW_ADMIN) {
            mCurrentAdmin = intent.getParcelableExtra(EXTRA_ADMIN);
        }
    }

}
