package com.ylj.connect;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.ylj.common.Controler;
import com.ylj.connect.bean.DeviceInfo;
import com.ylj.connect.config.ConnectError;
import com.ylj.daemon.YljService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public class ConnectControler extends Controler implements IConnectCtrl {

    public final static String EXTRA_ERROR = "extra_error";
    public final static String EXTRA_DEVICE_INFO = "extra_device_info";

    public final static String ACTION_CONNECT_ERROR = "action_connect_error";
    public final static String ACTION_CONNECTED = "action_connected";
    public final static String ACTION_DISCONNECTED = "action_disconnected";

    private BroadcastReceiver mConnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_CONNECT_ERROR)) {
                int error = intent.getIntExtra(EXTRA_ERROR, ConnectError.ERROR_NOT_KNOW);
                if (error == ConnectError.ERROR_CONNECT_LOST) {
                    for (OnConnectListener listener : mConnectListeners) {
                        listener.onConnectLost();
                    }
                } else {
                    for (OnConnectListener listener : mConnectListeners) {
                        listener.onConnectFail(error);
                    }
                }
            } else if (action.equals(ACTION_CONNECTED)) {
                DeviceInfo info = intent.getParcelableExtra(EXTRA_DEVICE_INFO);
                for (OnConnectListener listener : mConnectListeners) {
                    listener.onConnected(info);
                }
            } else if (action.equals(ACTION_DISCONNECTED)) {
                for (OnConnectListener listener : mConnectListeners) {
                    listener.onDisconnected();
                }
            }
        }
    };

    public static ConnectControler newInstance(Activity activity) {
        ConnectControler controler = new ConnectControler();
        controler.init(activity, YljService.class);
        return controler;
    }

    @Override
    protected void onServiceConnected(IBinder binder) {
        super.onServiceConnected(binder);

        IntentFilter filter = new IntentFilter(ACTION_CONNECT_ERROR);
        mActivity.registerReceiver(mConnectReceiver, filter);
        filter = new IntentFilter(ACTION_CONNECTED);
        mActivity.registerReceiver(mConnectReceiver, filter);
        filter = new IntentFilter(ACTION_DISCONNECTED);
        mActivity.registerReceiver(mConnectReceiver, filter);
    }

    @Override
    public void release() {
        super.release();
        mActivity.unregisterReceiver(mConnectReceiver);
        mConnectListeners.clear();
    }

    @Override
    public void connectToBluetooth(BluetoothDevice device) {
        if (mCleint == null)
            return;
        mCleint.connectToBluetooth(device);
    }

    @Override
    public void connectToWifi(String ip, int port) {
        if (mCleint == null)
            return;
        mCleint.connectToWifi(ip, port);
    }

    @Override
    public void disconnect() {
        if (mCleint == null)
            return;
        mCleint.disconnect();
    }

    @Override
    public void reconnect() {
        if (mCleint == null)
            return;
        mCleint.reconnect();
    }

    @Override
    public boolean isConnect() {
        if (mCleint == null)
            return false;
        return mCleint.isConnect();
    }

    public void requestDeviceInfo() {
        if (isConnect()) {
            mCleint.requestDeviceInfo();
        }
    }

    private List<OnConnectListener> mConnectListeners = new ArrayList<>();

    @Override
    public void addConnectListener(OnConnectListener listener) {
        if (mConnectListeners.contains(listener))
            return;
        mConnectListeners.add(listener);
    }

    @Override
    public void deleteConnectListener(OnConnectListener listener) {
        if (mConnectListeners.contains(listener)) {
            mConnectListeners.remove(listener);
        }
    }
}
