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

@ContentView(R.layout.activity_passwd_modify)
public class PasswdModifyActivity extends BaseActivity {

    public static final String EXTRA_MODE = "EXTRA_MODE";
    public static final String EXTRA_ADMIN = "EXTRA_ADMIN";

    public static final int MODE_NEW_PASSWD = 0;
    public static final int MODE_MODIFY_PASSWD = 1;

    private int mMode = MODE_NEW_PASSWD;
    private Admin mCurrentAdmin = new Admin();

    public static void startAsNewPasswdActivity(Context context, Admin admin) {
        Intent intent = new Intent(context, PasswdModifyActivity.class);
        intent.putExtra(EXTRA_MODE, MODE_NEW_PASSWD);
        intent.putExtra(EXTRA_ADMIN, admin);
        context.startActivity(intent);
    }

    public static void startAsModifyPasswdActivity(Context context, Admin admin) {
        Intent intent = new Intent(context, PasswdModifyActivity.class);
        intent.putExtra(EXTRA_MODE, MODE_MODIFY_PASSWD);
        intent.putExtra(EXTRA_ADMIN, admin);
        context.startActivity(intent);
    }

    public static void startAsModifyPasswdActivityForResult(Activity context, Admin admin, int requestCode) {
        Intent intent = new Intent(context, PasswdModifyActivity.class);
        intent.putExtra(EXTRA_MODE, MODE_MODIFY_PASSWD);
        intent.putExtra(EXTRA_ADMIN, admin);
        context.startActivityForResult(intent, requestCode);
    }

    @ViewInject(R.id.et_old_passwd)
    EditText mOldEdit;

    @ViewInject(R.id.et_new_passwd)
    EditText mNewEdit;

    @ViewInject(R.id.et_passwd_again)
    EditText mAgainEdit;

    @ViewInject(R.id.tv_message)
    TextView mMsgText;

    @ViewInject(R.id.toolbar)
    Toolbar mToolbar;

    @ViewInject(R.id.layout_old_passwd)
    LinearLayout mOldPasswdLayout;

    @Event(R.id.btn_save_passwd)
    private void onSaveButtonClick(View view) {
        if (mMode == MODE_MODIFY_PASSWD) {
            String passwd = mOldEdit.getText().toString();
            if (passwd.isEmpty()) {
                showMessage("please input passwd");
                return;
            }
            if (!mCurrentAdmin.getPasswd().equals(passwd)) {
                showMessage("wrong passwd");
                return;
            }
        }

        String newPasswd = mNewEdit.getText().toString();
        if (newPasswd.isEmpty()) {
            showMessage("please input new passwd");
            return;
        }
        if (!newPasswd.equals(mAgainEdit.getText().toString())) {
            showMessage("not equal");
            return;
        }
        mCurrentAdmin.setPasswd(newPasswd);
        DbLet.saveOrUpdateAdmin(mCurrentAdmin);

        if (mMode == MODE_MODIFY_PASSWD) {
            setResultAndFinish();
        }
        if (mMode == MODE_NEW_PASSWD) {
            finish();
        }
    }

    private void setResultAndFinish() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ADMIN, mCurrentAdmin);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showMessage(String text) {
        mMsgText.setText(text);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initExtraData();
        initToolbar();
        initContentView();
    }

    private void initToolbar() {
        switch (mMode) {
            case MODE_MODIFY_PASSWD:
                mToolbar.setTitle(R.string.staff_title_passwd_modify);
                break;
            case MODE_NEW_PASSWD:
                mToolbar.setTitle(R.string.staff_title_new_passwd);
            default:
                break;
        }
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswdModifyActivity.this.finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initExtraData() {
        Intent intent = getIntent();
        mMode = intent.getIntExtra(EXTRA_MODE, MODE_NEW_PASSWD);
        mCurrentAdmin = intent.getParcelableExtra(EXTRA_ADMIN);
    }

    private void initContentView() {
        if (mMode == MODE_NEW_PASSWD) {
            mOldPasswdLayout.setVisibility(View.GONE);
        } else {
            mOldPasswdLayout.setVisibility(View.VISIBLE);
        }
    }

}
