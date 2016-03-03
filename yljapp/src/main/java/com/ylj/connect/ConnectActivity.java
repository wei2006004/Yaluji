package com.ylj.connect;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;

import com.ylj.R;
import com.ylj.common.BaseActivity;
import com.ylj.connect.fragment.BtConnectFragment;
import com.ylj.connect.fragment.ConnectFragment;
import com.ylj.connect.fragment.ConnectWaitFragment;
import com.ylj.connect.fragment.DeviceInfoFragment;
import com.ylj.connect.fragment.WifiConnectFragment;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_connect)
public class ConnectActivity extends BaseActivity implements BtConnectFragment.OnBluetoothConnectListener,
        WifiConnectFragment.OnWifiConnectListener,
        DeviceInfoFragment.OnDeviceActionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().add(R.id.layout_container,new ConnectFragment()).commit();
    }

    private void switchToConnectFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, new ConnectFragment()).commit();
    }

    private void switchToConncetWaitFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, new ConnectWaitFragment()).commit();
    }

    private void switchToDeviceInfoFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, new DeviceInfoFragment()).commit();
    }

    @Override
    public void onWifiConnect() {
        switchToConncetWaitFragment();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switchToDeviceInfoFragment();
            }
        },1000);
    }

    @Override
    public void onBluetoothConnect(Uri uri) {

    }

    @Override
    public void onReGetDeviceInfo() {

    }

    @Override
    public void onDeviceDisconnect() {
        switchToConnectFragment();
    }
}
