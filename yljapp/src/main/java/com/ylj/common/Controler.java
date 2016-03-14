package com.ylj.common;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public abstract class Controler {
    public Controler(Activity activity,Class<?> cls){

    }

    protected abstract void onReceive(Bundle data);

    protected boolean isBound(){
        return false;
    }

    protected void onServiceConnected(IBinder binder){

    }

    protected void onServiceDisconnected(){

    }

    public void release(){

    }
}
