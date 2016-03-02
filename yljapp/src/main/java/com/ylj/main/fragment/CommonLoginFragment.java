package com.ylj.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ylj.R;
import com.ylj.common.BaseFragment;
import com.ylj.main.MenuActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.fragment_common_login)
public class CommonLoginFragment extends BaseFragment {

    @ViewInject(R.id.tv_switch_admin_login)
    private TextView mSwitchAdminLoginView;

    @ViewInject(R.id.lv_user)
    private ListView mUserListView;

    @ViewInject(R.id.bt_login)
    private Button mLoginButton;

    @ViewInject(R.id.bt_annoy_login)
    private Button mAnnoyLoginButton;

    private OnSwitchToAdminLoginListener mListener;

    public CommonLoginFragment() {
        // Required empty public constructor
    }

    @Event(R.id.bt_login)
    private void onLoginButtonClick(View view) {

    }

    @Event(R.id.bt_annoy_login)
    private void onAnnoyLoginButtonClick(View view) {
        Intent intent = new Intent(x.app(), MenuActivity.class);
        startActivity(intent);
    }

    @Event(R.id.tv_switch_admin_login)
    private void onSwitchAdminLoginClick(View view) {
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
        public void onSwitchToAdminLogin();
    }

}
