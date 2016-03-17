package com.ylj.daemon.client;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Parcelable;

import com.ylj.daemon.client.IClient;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public abstract class BaseClient extends Binder implements IClient {

    public final static String EXTRA_ACTION_FLAG = "EXTRA_ACTION_FLAG";

    private Context mContext;

    public BaseClient(Context context) {
        mContext = context;
    }

    protected Context getContext(){
        return mContext;
    }

    protected void sendBroadcast(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        mContext.sendBroadcast(intent);
    }

    protected void sendBroadcast(String action, int flag) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(EXTRA_ACTION_FLAG, flag);
        mContext.sendBroadcast(intent);
    }

    protected void sendBroadcast(String action, String flag_tag, int flag) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(flag_tag, flag);
        mContext.sendBroadcast(intent);
    }

    protected void sendBroadcast(String action, String data_tag, Parcelable data) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(data_tag, data);
        mContext.sendBroadcast(intent);
    }
}
