package com.ylj.daemon.client;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.ylj.connect.bean.DeviceInfo;
import com.ylj.daemon.config.ConnectState;
import com.ylj.daemon.connect.BtConnector;
import com.ylj.daemon.connect.DebugConnector;
import com.ylj.daemon.connect.IConnector;
import com.ylj.daemon.connect.TcpConnector;
import com.ylj.daemon.msghandler.IMessageHandler;
import com.ylj.daemon.msghandler.MessageHandlerImpl;
import com.ylj.task.bean.DeviceData;


/**
 * Created by Administrator on 2016/3/15 0015.
 */
public class YljClient extends BaseClient implements IConnector.OnStateChangeListener,
        IMessageHandler.OnHandleListener {

    public final static String ACTION_CONNECT_STATE_CHANGE = "ACTION_CONNECT_STATE_CHANGE";
    public final static String ACTION_DISCONNECTED = "ACTION_DISCONNECTED";
    public final static String ACTION_DEVICE_INFO = "ACTION_DEVICE_INFO";
    public final static String ACTION_TEST_DATA = "ACTION_TEST_DATA";
    public final static String ACTION_TEST_CTRL = "ACTION_TEST_CTRL";

    public final static String EXTRA_TEST_DATA = "EXTRA_TEST_INFO";
    public final static String EXTRA_DEVICE_INFO = "EXTRA_DEVICE_INFO";

    public final static int CTRL_FLAG_STOP = 0;
    public final static int CTRL_FLAG_START = 1;


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
        connector.setOnStateChangeListener(this);
        connector.setMessageHandler(mMessageHandler);
        connector.connect();
        mConnector = connector;
    }

    @Override
    public void connectToWifi(String ip, int port) {
        disconnect();

        TcpConnector connector = new TcpConnector();
        connector.setAddress(ip, port);
        connector.setOnStateChangeListener(this);
        connector.setMessageHandler(mMessageHandler);
        connector.connect();
        mConnector = connector;
    }

    @Override
    public void connectToDebug() {
        disconnect();

        DebugConnector connector = new DebugConnector();
        connector.setOnStateChangeListener(this);
        connector.setMessageHandler(mMessageHandler);
        connector.connect();
        mConnector = connector;
    }

    @Override
    public void disconnect() {
        if (mConnector != null) {
            mConnector.disconnect();
            sendBroadcast(ACTION_DISCONNECTED);
        }
    }

    @Override
    public void reconnect() {
        if (mConnector != null) {
            mConnector.disconnect();
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
    public void startTest() {
        if (isConnect()) {
            mConnector.sendStartMessage();
            sendBroadcast(ACTION_TEST_CTRL, CTRL_FLAG_START);
        }
    }

    @Override
    public void stopTest() {
        if (isConnect()) {
            mConnector.sendStopMessage();
            sendBroadcast(ACTION_TEST_CTRL, CTRL_FLAG_STOP);
        }
    }

    @Override
    public void onStateChange(int state) {
        mState = state;
        sendBroadcast(ACTION_CONNECT_STATE_CHANGE, state);
    }

    @Override
    public void onHandleDeviceInfo(DeviceInfo info) {
        sendBroadcast(ACTION_DEVICE_INFO, EXTRA_DEVICE_INFO, info);
    }

    @Override
    public void onHandleDeviceData(DeviceData data) {
        sendBroadcast(ACTION_TEST_DATA, EXTRA_TEST_DATA, data);
    }

    @Override
    public void onHandleWrongMessage(String msg) {
        Log.e("onHandleWrongMessage", "message:" + msg);
    }
}
