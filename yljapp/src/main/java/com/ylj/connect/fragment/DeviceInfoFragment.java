package com.ylj.connect.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ylj.R;
import com.ylj.common.BaseFragment;
import com.ylj.connect.bean.DeviceInfo;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.fragment_device_info)
public class DeviceInfoFragment extends BaseFragment {

    private static final String ARG_DEVICE_INFO = "ARG_DEVICE_INFO";

    @ViewInject(R.id.tv_name)
    TextView mNameText;

    @ViewInject(R.id.tv_version)
    TextView mVersionText;

    @ViewInject(R.id.tv_soft_version)
    TextView mSoftVersionText;

    @ViewInject(R.id.tv_temp)
    TextView mTempText;

    @ViewInject(R.id.tv_quake)
    TextView mQuakeText;

    @ViewInject(R.id.tv_direction)
    TextView mDirText;

    @ViewInject(R.id.tv_huoer)
    TextView mHuoerText;

    private DeviceInfo mDeviceInfo = new DeviceInfo();

    private OnDeviceActionListener mListener;

    public static DeviceInfoFragment newInstance(DeviceInfo info) {
        DeviceInfoFragment fragment = new DeviceInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_DEVICE_INFO, info);
        fragment.setArguments(args);
        return fragment;
    }

    public DeviceInfoFragment() {
    }

    @Event(R.id.bt_resent)
    private void onResentButtonClick(View view){
        if (mListener != null) {
            mListener.onReGetDeviceInfo();
        }
    }

    @Event(R.id.bt_disconnect)
    private void onDisconnectButtonClick(View view){
        if (mListener != null) {
            mListener.onDeviceDisconnect();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDeviceInfo = getArguments().getParcelable(ARG_DEVICE_INFO);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInfoView();
    }

    private void initInfoView() {
        mNameText.setText(mDeviceInfo.getDeviceId());
        mVersionText.setText(mDeviceInfo.getVersion());
        mSoftVersionText.setText(mDeviceInfo.getSoftVersion());

        setDeviceStatus(mTempText, mDeviceInfo.isTempState());
        setDeviceStatus(mQuakeText,mDeviceInfo.isQuakeState());
        setDeviceStatus(mDirText,mDeviceInfo.isDirState());
        setDeviceStatus(mHuoerText,mDeviceInfo.isHuoerState());
    }

    void setDeviceStatus(TextView view,boolean status){
        if(status){
            view.setText("normal");
        }else {
            view.setText("error");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnDeviceActionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDeviceActionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDeviceActionListener {
        public void onReGetDeviceInfo();
        public void onDeviceDisconnect();
    }

}
