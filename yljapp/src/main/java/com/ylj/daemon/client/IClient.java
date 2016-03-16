package com.ylj.daemon.client;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public interface IClient {
    void connectToBluetooth(BluetoothDevice device);
    void connectToWifi(String ip, int port);
    void connectToDebug();

    void disconnect();
    void reconnect();

    boolean isConnect();

    void requestDeviceInfo();

    void startAdjust();
    void stopAdjust();

    void startTest();
    void stopTest();
}
