package com.ylj.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ylj.R;
import com.ylj.common.BaseFragment;
import com.ylj.common.bean.Staff;
import com.ylj.common.utils.BeanUtils;
import com.ylj.db.DbLet;
import com.ylj.db.LoginLet;
import com.ylj.main.MenuActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
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

    SimpleAdapter mStaffAdapter;
    List<Map<String, Object>> mStaffMaps = new ArrayList<>();

    private OnSwitchToAdminLoginListener mListener;

    public CommonLoginFragment() {
    }

    @Event(R.id.bt_login)
    private void onLoginButtonClick(View view) {
        int position = mUserListView.getCheckedItemPosition();
        Staff staff = getStaffByPosition(position);
        if(LoginLet.doStaffLogin(staff)){
            turnToMenuActivity();
        }else {
            showToast(R.string.toast_login_fail);
        }
    }

    private void turnToMenuActivity() {
        Intent intent = new Intent(x.app(), MenuActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private Staff getStaffByPosition(int position) {
        Map<String, Object> map = mStaffMaps.get(position);
        return Staff.createByMap(map);
    }

    @Event(R.id.bt_annoy_login)
    private void onAnnoyLoginButtonClick(View view) {
        LoginLet.doAnonymousLogin();
        turnToMenuActivity();
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
        initStaffListView();
    }

    private void initStaffListView() {
        mStaffAdapter = new SimpleAdapter(x.app(), mStaffMaps,
                R.layout.listview_login_staff,
                new String[]{Staff.TAG_STAFF_NAME, Staff.TAG_COMPANY, Staff.TAG_GROUP},
                new int[]{R.id.tv_name, R.id.tv_company, R.id.tv_group});

        mUserListView.setAdapter(mStaffAdapter);
        mUserListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLoginButton.setEnabled(true);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        refreshStaffData();
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
