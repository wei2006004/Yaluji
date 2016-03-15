package com.ylj.daemon;



public class YljService extends BaseService {
    public final static String ACTION_SERVICE_DESTROY = "ACTION_SERVICE_DESTROY";

    public YljService() {
    }

    @Override
    public void onDestroy() {
        super.onCreate();
        if(mClient.isConnect()){
            mClient.disconnect();
        }

        sendBroadcastAction(ACTION_SERVICE_DESTROY);
    }
}
