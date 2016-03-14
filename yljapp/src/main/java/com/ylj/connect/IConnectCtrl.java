package com.ylj.connect;

import android.bluetooth.BluetoothDevice;

import com.ylj.connect.bean.DeviceInfo;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public interface IConnectCtrl {

    public final static int ERROR_CONNECT_FAIL = 0;
    public final static int ERROR_CONNECT_LOST = 1;
    public final static int ERROR_SERVER_ERROR = 2;

    void connectToBluetooth(BluetoothDevice device,OnConnectListener listener);

    void connectToWifi(String ip, int port,OnConnectListener listener);

    void disconnect();
    void reconnect();

    void addConnectListener(OnConnectListener listener);
    void deleteConnectListener(OnConnectListener listener);

    interface OnConnectListener {
        void onConnected(DeviceInfo info);
        void onConnectError(int error);
    }
}
