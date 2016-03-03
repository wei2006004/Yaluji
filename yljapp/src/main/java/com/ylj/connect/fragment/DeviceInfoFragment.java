package com.ylj.connect.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.ylj.R;
import com.ylj.common.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.fragment_device_info)
public class DeviceInfoFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnDeviceActionListener mListener;

    public static DeviceInfoFragment newInstance(String param1, String param2) {
        DeviceInfoFragment fragment = new DeviceInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DeviceInfoFragment() {
        // Required empty public constructor
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
