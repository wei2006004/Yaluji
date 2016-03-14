package com.ylj.daemon;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public interface ICleint {

    void connectToBluetooth(BluetoothDevice device);
    void connectToWifi(String ip, int port);

    void disconnect();
    void reconnect();

    void requestDeviceInfo();

    void startAdjust();
    void stopAdjust();

    void startTest();
    void stopTest();
}
