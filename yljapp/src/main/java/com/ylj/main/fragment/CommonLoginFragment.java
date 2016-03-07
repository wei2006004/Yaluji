package com.ylj.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ylj.R;
import com.ylj.common.BaseFragment;
import com.ylj.common.bean.Staff;
import com.ylj.common.utils.BeanUtils;
import com.ylj.db.DbLet;
import com.ylj.main.MenuActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;
import java.util.Map;

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart(){
        super.onStart();
        fillStaffListViewData();
    }

    SimpleAdapter mStaffAdapter;
    List<Staff> mStaffs;

    private void fillStaffListViewData() {
        x.task().run(new Runnable() {
            @Override
            public void run() {
                mStaffs = DbLet.getStaffList();
                if (mStaffs == null)
                    return;
                List<Map<String, String>> maps = BeanUtils.convertStaffs2Maps(mStaffs);
                mStaffAdapter = new SimpleAdapter(x.app(), maps,
                        R.layout.listview_login_staff,
                        new String[]{Staff.TAG_STAFF_NAME, Staff.TAG_COMPANY, Staff.TAG_GROUP},
                        new int[]{R.id.tv_name, R.id.tv_company, R.id.tv_group});
                x.task().post(new Runnable() {
                    @Override
                    public void run() {
                        mUserListView.setAdapter(mStaffAdapter);
                        mUserListView.setSelection(0);
                    }
                });
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSwitchToAdminLoginListener) activity;
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

    public interface OnSwitchToAdminLoginListener {
        public void onSwitchToAdminLogin();
    }

}
