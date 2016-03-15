package com.ylj.daemon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ylj.daemon.client.BaseClient;
import com.ylj.daemon.client.ClientFactory;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public class BaseService extends Service {

    protected BaseClient mClient = ClientFactory.createClient(ClientFactory.CLIENT_TYPE_YLJ);

    public BaseService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mClient;
    }
}
