package com.ylj.main.fragment;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ylj.R;
import com.ylj.common.BaseFragment;
import com.ylj.db.LoginLet;
import com.ylj.main.MenuActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.fragment_admin_login)
public class AdminLoginFragment extends BaseFragment {

    public AdminLoginFragment() {
        // Required empty public constructor
    }

    @ViewInject(R.id.tv_switch_common_login)
    private TextView mSwitchCommonLoginView;

    @ViewInject(R.id.et_user)
    private EditText mUserText;

    @ViewInject(R.id.et_passwd)
    private EditText mPasswdText;

    @ViewInject(R.id.bt_login)
    private Button mLoginButton;

    @ViewInject(R.id.tv_message)
    private TextView mMsgText;

    private OnSwitchToCommonLoginListener mListener;

    @Event(R.id.bt_login)
    private void onLoginButtonClick(View view) {
        String user = mUserText.getText().toString();
        String passwd = mPasswdText.getText().toString();
        if (user.isEmpty()) {
            showMessage("user empty!");
            return;
        }
        if (passwd.isEmpty()) {
            showMessage("passwd empty!");
            return;
        }
        doAdminLogin(user, passwd);
    }

    private void doAdminLogin(String user, String passwd) {
        int result= LoginLet.doAdminLogin(user, passwd);
        switch (result){
            case LoginLet.LOGIN_SUCCESS:
                turnToMenuActivity();
                return;
            case LoginLet.LOGIN_ERROR_WRONG_PASSWD:
                showMessage("wrong passwd!");
                return;
            case LoginLet.LOGIN_ERROR_NO_ACCOUNT:
                showMessage("no account!");
                return;
        }
    }

    private void turnToMenuActivity() {
        Intent intent = new Intent(x.app(), MenuActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void showMessage(int resid) {
        showMessage(getString(resid));
    }

    private void showMessage(String text) {
        mMsgText.setVisibility(View.VISIBLE);
        mMsgText.setText(text);
    }

    @Event(R.id.tv_switch_common_login)
    private void onSwitchCommonLoginClick(View view) {
        if (mListener != null) {
            mListener.onSwitchToCommonLogin();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSwitchToCommonLoginListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnWifiConnectListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSwitchToCommonLoginListener {
        public void onSwitchToCommonLogin();
    }


}
