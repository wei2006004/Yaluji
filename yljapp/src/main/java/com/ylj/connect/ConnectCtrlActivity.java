package com.ylj.connect;

import android.os.Bundle;

import com.ylj.common.BaseActivity;
import com.ylj.common.config.AppStatus;
import com.ylj.connect.bean.DeviceInfo;

/**
 * Created by Administrator on 2016/4/6 0006.
 */
public abstract class ConnectCtrlActivity extends BaseActivity implements IConnectCtrl.OnConnectListener {

    private IConnectCtrl connectCtrl;

    protected abstract IConnectCtrl getConnectCtrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectCtrl = getConnectCtrl();
        connectCtrl.addConnectListener(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        connectCtrl.deleteConnectListener(this);
    }

    @Override
    public void onConnected(DeviceInfo info) {
        showToast("connected");
        setAppConnectStatus(true);
        AppStatus.instance().setCurrentDevice(info);
    }


    @Override
    public void onDisconnected() {
        showToast("disconnected");
        setAppConnectStatus(false);
    }

    @Override
    public void onConnectFail(int error) {
        showToast("connect fail");
        setAppConnectStatus(false);
    }

    @Override
    public void onConnectLost() {
        showToast("connect lost");
        setAppConnectStatus(false);
    }

    private void setAppConnectStatus(boolean isConnect){
        AppStatus appstatus = AppStatus.instance();
        appstatus.setIsConnect(isConnect);
    }
}
