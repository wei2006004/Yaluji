package com.ylj.daemon;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.ylj.daemon.client.BaseClient;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public class YljClient extends BaseClient {

    public YljClient(Context context) {
        super(context);
    }

    @Override
    public void connectToBluetooth(BluetoothDevice device) {

    }

    @Override
    public void connectToWifi(String ip, int port) {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void reconnect() {

    }

    @Override
    public boolean isConnect() {
        return false;
    }

    @Override
    public void requestDeviceInfo() {

    }

    @Override
    public void startTest() {

    }

    @Override
    public void stopTest() {

    }
}
