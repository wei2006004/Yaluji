package com.ylj.connect;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;

import com.ylj.common.Controler;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public class ConnectControler extends Controler implements IConnectCtrl {

    public static ConnectControler newInstance(Activity activity, Class<?> cls) {
        return new ConnectControler(activity,cls);
    }

    public ConnectControler(Activity activity, Class<?> cls) {
        super(activity, cls);
    }

    @Override
    protected void onReceive(Bundle data) {

    }

    @Override
    public void connectToBluetooth(BluetoothDevice device, OnConnectListener listener) {

    }

    @Override
    public void connectToWifi(String ip, int port, OnConnectListener listener) {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void reconnect() {

    }

    @Override
    public void addConnectListener(OnConnectListener listener) {

    }

    @Override
    public void deleteConnectListener(OnConnectListener listener) {

    }
}
