package com.ylj.connect.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Switch;

import com.ylj.R;
import com.ylj.common.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.fragment_bt_connect)
public class BtConnectFragment extends BaseFragment {

    @ViewInject(R.id.lv_device)
    private ListView mDeviceListView;

    @ViewInject(R.id.switch_bluetooth)
    private Switch mBtSwitch;
    
    private OnBluetoothConnectListener mListener;

    public BtConnectFragment() {
        // Required empty public constructor
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onBluetoothConnect(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnBluetoothConnectListener) activity;
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

    public interface OnBluetoothConnectListener {
        public void onBluetoothConnect(Uri uri);
    }

}
