package com.ylj.connect;

import android.bluetooth.BluetoothDevice;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.ylj.R;
import com.ylj.common.BaseActivity;
import com.ylj.common.config.AppStatus;
import com.ylj.common.config.Config;
import com.ylj.common.config.ConfigLet;
import com.ylj.common.config.StatusLet;
import com.ylj.connect.bean.DeviceInfo;
import com.ylj.connect.fragment.BtConnectFragment;
import com.ylj.connect.fragment.ConnectFragment;
import com.ylj.connect.fragment.ConnectWaitFragment;
import com.ylj.connect.fragment.DeviceInfoFragment;
import com.ylj.connect.fragment.WifiConnectFragment;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_connect)
public class ConnectActivity extends BaseActivity implements BtConnectFragment.OnBluetoothConnectListener,
        WifiConnectFragment.OnWifiConnectListener,
        DeviceInfoFragment.OnDeviceActionListener,
        ConnectControler.OnConnectListener {

    ConnectControler mConnectControler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();
        initData();
        initContentView();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mConnectControler.deleteConnectListener(this);
        mConnectControler.release();
    }

    private void initContentView() {
        if(StatusLet.isConnect()){
            DeviceInfo info = AppStatus.instance().getCurrentDevice();
            DeviceInfoFragment fragment = DeviceInfoFragment.newInstance(info);
            getSupportFragmentManager().beginTransaction().add(R.id.layout_container, fragment).commit();
        }else {
            getSupportFragmentManager().beginTransaction().add(R.id.layout_container, new ConnectFragment()).commit();
        }
    }

    private void initData() {
        mConnectControler = ConnectControler.newInstance(this);
        mConnectControler.addConnectListener(this);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void switchToConnectFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, new ConnectFragment()).commit();
    }

    private void switchToConncetWaitFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, new ConnectWaitFragment()).commit();
    }

    private void switchToDeviceInfoFragment(DeviceInfo info) {
        DeviceInfoFragment fragment = DeviceInfoFragment.newInstance(info);
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment).commit();
    }

    @Override
    public void onWifiConnect() {
        switchToConncetWaitFragment();
        if (ConfigLet.isDebug()) {
            mConnectControler.connectToDebug();
        } else {
            String ip = ConfigLet.getWifiIp();
            int port = ConfigLet.getWifiPort();
            mConnectControler.connectToWifi(ip, port);
        }
    }

    @Override
    public void onBluetoothConnect(BluetoothDevice device) {
        mConnectControler.connectToBluetooth(device);
    }

    @Override
    public void onReGetDeviceInfo() {
        mConnectControler.requestDeviceInfo();
    }

    @Override
    public void onDeviceDisconnect() {
        mConnectControler.disconnect();
    }

    @Override
    public void onConnected(DeviceInfo info) {
        showToast("connected");
        switchToDeviceInfoFragment(info);
        setAppConnectStatus(true);
        AppStatus.instance().setCurrentDevice(info);
    }


    @Override
    public void onDisconnected() {
        showToast("disconnected");
        switchToConnectFragment();
        setAppConnectStatus(false);
    }

    @Override
    public void onConnectFail(int error) {
        showToast("connect fail");
        switchToConnectFragment();
        setAppConnectStatus(false);
    }

    @Override
    public void onConnectLost() {
        showToast("connect lost");
        switchToConnectFragment();
        setAppConnectStatus(false);
    }

    private void setAppConnectStatus(boolean isConnect){
        AppStatus appstatus = AppStatus.instance();
        appstatus.setIsConnect(isConnect);
    }
}
