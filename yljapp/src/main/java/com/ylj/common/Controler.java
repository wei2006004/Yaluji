package com.ylj.common;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.ylj.daemon.BaseService;
import com.ylj.daemon.client.IClient;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public abstract class Controler {

    protected Activity mActivity;
    private IBinder mIBinder;
    protected IClient mCleint;

    protected IClient getCleint(){
        return mCleint;
    }

    protected Activity getActivity(){
        return mActivity;
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Controler.this.onServiceConnected(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Controler.this.onServiceDisconnected();
        }
    };

    public Controler() {

    }

    public void init(Activity activity, Class<? extends BaseService> cls) {
        mActivity = activity;
        Intent intent = new Intent(activity, cls);
        activity.bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
    }

    protected boolean isBind() {
        return mIBinder.isBinderAlive();
    }

    protected void onServiceConnected(IBinder binder) {
        mIBinder = binder;
        mCleint = (IClient) binder;
    }

    protected void onServiceDisconnected() {

    }

    public void release() {
        mActivity.unbindService(mServiceConnection);
    }
}
