package com.ylj.connect.fragment;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.ylj.R;
import com.ylj.common.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.fragment_wifi_connect)
public class WifiConnectFragment extends BaseFragment {

    @ViewInject(R.id.tv_wifi_status)
    private TextView mStatusText;

    private OnWifiConnectListener mListener;

    public WifiConnectFragment() {
        // Required empty public constructor
    }

    @Event(R.id.bt_open_wifi)
    private void onOpenWifiButtonClick(View view){
        mStatusText.setText("wifi connect");
    }

    @Event(R.id.bt_connect)
    private void onConnectButtonClick(View view) {
        if (mListener != null) {
            mListener.onWifiConnect();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnWifiConnectListener) activity;
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

    public interface OnWifiConnectListener {
        public void onWifiConnect();
    }

}
