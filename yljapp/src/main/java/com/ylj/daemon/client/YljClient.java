package com.ylj.daemon.client;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.ylj.common.bean.Task;
import com.ylj.common.bean.Test;
import com.ylj.connect.bean.DeviceInfo;
import com.ylj.daemon.config.ConnectState;
import com.ylj.daemon.config.ServiceAction;
import com.ylj.daemon.connect.BtConnector;
import com.ylj.daemon.connect.DebugConnector;
import com.ylj.daemon.connect.IConnector;
import com.ylj.daemon.connect.TcpConnector;
import com.ylj.daemon.msghandler.IMessageHandler;
import com.ylj.daemon.msghandler.MessageHandlerImpl;
import com.ylj.daemon.bean.DeviceData;


/**
 * Created by Administrator on 2016/3/15 0015.
 */
public class YljClient extends BaseClient implements IConnector.OnStateChangeListener,
        IMessageHandler.OnHandleListener {
    
    private int mState = ConnectState.STATE_NONE;

    IConnector mConnector;
    IMessageHandler mMessageHandler;

    public YljClient(Context context) {
        super(context);
        mMessageHandler = new MessageHandlerImpl(this);
    }

    @Override
    public void connectToBluetooth(BluetoothDevice device) {
        disconnect();

        BtConnector connector = new BtConnector();
        connector.setBluetoothDevice(device);
        mConnector = connector;
        mConnector.setOnStateChangeListener(this);
        mConnector.setMessageHandler(mMessageHandler);
        mConnector.connect();
    }

    @Override
    public void connectToWifi(String ip, int port) {
        disconnect();

        TcpConnector connector = new TcpConnector();
        connector.setAddress(ip, port);
        mConnector = connector;
        mConnector.setOnStateChangeListener(this);
        mConnector.setMessageHandler(mMessageHandler);
        mConnector.connect();
    }

    @Override
    public void connectToDebug() {
        disconnect();

        DebugConnector connector = new DebugConnector();
        mConnector = connector;
        mConnector.setOnStateChangeListener(this);
        mConnector.setMessageHandler(mMessageHandler);
        mConnector.connect();
    }

    @Override
    public void disconnect() {
        if (mConnector != null) {
            mConnector.disconnect();
            sendBroadcast(ServiceAction.ACTION_DISCONNECTED);
        }
    }

    @Override
    public void reconnect() {
        if (mConnector != null) {
            mConnector.connect();
        }
    }

    @Override
    public boolean isConnect() {
        return (mConnector != null) && (mState == ConnectState.STATE_CONNECTED);
    }

    @Override
    public void requestDeviceInfo() {
        if (isConnect()) {
            mConnector.sendDeviceMessage();
        }
    }

    @Override
    public void startAdjust() {
        if (isConnect()) {
            mConnector.sendStartMessage();
            sendBroadcast(ServiceAction.ACTION_SAMPLE_CTRL_STATE_CHANGE, ServiceAction.CTRL_FLAG_START);
        }
    }

    @Override
    public void stopAdjust() {
        if (isConnect()) {
            mConnector.sendStopMessage();
            sendBroadcast(ServiceAction.ACTION_SAMPLE_CTRL_STATE_CHANGE, ServiceAction.CTRL_FLAG_STOP);
        }
    }

    @Override
    public void loadTask(Task task) {

    }

    @Override
    public void finishTest(Test test) {

    }

    @Override
    public void startTest() {

    }

    @Override
    public void puaseTest() {

    }

    @Override
    public void onStateChange(int state) {
        mState = state;
        sendBroadcast(ServiceAction.ACTION_CONNECT_STATE_CHANGE, state);

        if(state == ConnectState.STATE_CONNECTED){
            mConnector.sendDeviceMessage();
        }
    }

    @Override
    public void onHandleDeviceInfo(DeviceInfo info) {
        Log.d("yljclient", "info:" + info.getDeviceId());
        sendBroadcast(ServiceAction.ACTION_DEVICE_INFO, ServiceAction.EXTRA_DEVICE_INFO, info);
    }

    @Override
    public void onHandleDeviceData(DeviceData data) {
        Log.d("yljclient", "data:" + data.toString());
        sendBroadcast(ServiceAction.ACTION_ADJUST_DATA, ServiceAction.EXTRA_ADJUST_DATA, data);
    }

    @Override
    public void onHandleWrongMessage(String msg) {
        Log.e("yljclient", "message:" + msg);
    }
}
