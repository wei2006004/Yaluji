package com.ylj.daemon.client;

import android.bluetooth.BluetoothDevice;

import com.ylj.common.bean.Task;
import com.ylj.common.bean.Test;

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

    void loadTask(Task task);
    void finishTask(Test test);

    void startTest();
    void puaseTest();
    void finishTest(Test test);

    void ftpLogin(String address,int port,String user,String passwd);
    void logout();
    void upload(String filePath);
    void cancelUpload();
}
