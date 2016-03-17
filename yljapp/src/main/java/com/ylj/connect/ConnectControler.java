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
import com.ylj.daemon.YljService;
import com.ylj.daemon.config.ConnectState;
import com.ylj.daemon.config.ServiceAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public class ConnectControler extends Controler implements IConnectCtrl {

    private BroadcastReceiver mConnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ServiceAction.ACTION_CONNECT_STATE_CHANGE)) {
                int state = intent.getIntExtra(ServiceAction.EXTRA_ACTION_FLAG, ConnectState.STATE_NONE);
                if (state == ConnectState.STATE_CONNECT_LOST) {
                    for (OnConnectListener listener : mConnectListeners) {
                        listener.onConnectLost();
                    }
                }
                if(state == ConnectState.STATE_CONNECT_FAIL){
                    for (OnConnectListener listener : mConnectListeners) {
                        listener.onConnectFail(0);
                    }
                }
            } else if (action.equals(ServiceAction.ACTION_DEVICE_INFO)) {
                DeviceInfo info = intent.getParcelableExtra(ServiceAction.EXTRA_DEVICE_INFO);
                for (OnConnectListener listener : mConnectListeners) {
                    listener.onConnected(info);
                }
            } else if (action.equals(ServiceAction.ACTION_DISCONNECTED)) {
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

        IntentFilter filter = new IntentFilter(ServiceAction.ACTION_CONNECT_STATE_CHANGE);
        mActivity.registerReceiver(mConnectReceiver, filter);
        filter = new IntentFilter(ServiceAction.ACTION_DEVICE_INFO);
        mActivity.registerReceiver(mConnectReceiver, filter);
        filter = new IntentFilter(ServiceAction.ACTION_DISCONNECTED);
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
    public void connectToDebug() {
        if (mCleint == null)
            return;
        mCleint.connectToDebug();
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
