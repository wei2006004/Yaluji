package com.ylj.daemon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ylj.daemon.client.BaseClient;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public class BaseService extends Service {

    protected BaseClient mClient;

    public BaseService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mClient = ClientFactory.createClient(this, ClientFactory.CLIENT_TYPE_YLJ);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mClient;
    }

    protected void sendBroadcastAction(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        sendBroadcast(intent);
    }
}
