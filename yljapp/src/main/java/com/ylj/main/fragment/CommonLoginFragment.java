package com.ylj.main.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ylj.R;
import com.ylj.common.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.fragment_common_login)
public class CommonLoginFragment extends BaseFragment {

    @ViewInject(R.id.tv_switch_admin_login)
    private TextView mSwitchAdminLoginView;

    @ViewInject(R.id.et_user)
    private EditText mUserText;

    @ViewInject(R.id.et_passwd)
    private EditText mPasswdText;

    @ViewInject(R.id.bt_login)
    private Button mLoginButton;

    @ViewInject(R.id.bt_annoy_login)
    private Button mAnnoyLoginButton;

    private OnSwitchToAdminLoginListener mListener;

    public CommonLoginFragment() {
        // Required empty public constructor
    }

    @Event(R.id.bt_login)
    private void onLoginButtonClick(View view){

    }

    @Event(R.id.bt_annoy_login)
    private void onAnnoyLoginButtonClick(View view){

    }

    @Event(R.id.tv_switch_admin_login)
    private void onSwitchAdminLogin(View view) {
        if (mListener != null) {
            mListener.onSwitchToAdminLogin();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSwitchToAdminLoginListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSwitchToAdminLoginListener {
        // TODO: Update argument type and name
        public void onSwitchToAdminLogin();
    }

}
