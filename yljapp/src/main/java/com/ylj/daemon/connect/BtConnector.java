package com.ylj.daemon.connect;

import android.bluetooth.BluetoothDevice;

import com.ylj.daemon.config.ConnectState;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public class BtConnector extends BaseConnector {

    BluetoothChatService mService;
    BluetoothDevice mDevice;

    public BtConnector() {
        mService = new BluetoothChatService();
        mService.setIOLister(new BluetoothChatService.IOListener() {
            @Override
            public void read(String msg) {
                onMessageArrive(msg);
            }

            @Override
            public void write(String msg) {
            }
        });
    }

    public void setBluetoothDevice(BluetoothDevice device) {
        mDevice = device;
    }

    @Override
    public void connect() {
        if (mDevice == null)
            return;
        mService.connect(mDevice, true);
    }

    @Override
    public void disconnect() {
        mService.stop();
    }

    @Override
    public boolean isConnect() {
        return mService.getState() == ConnectState.STATE_CONNECTED;
    }

    @Override
    public void sendMessage(String msg) {
        if (isConnect()) {
            mService.write(msg.getBytes());
        }
    }

    @Override
    public void setOnStateChangeListener(OnStateChangeListener listener) {
        mService.setOnStateChangeListener(listener);
    }
}
