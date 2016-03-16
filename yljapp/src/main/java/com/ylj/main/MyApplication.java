package com.ylj.main;

import android.app.Application;
import android.content.Intent;

import com.ylj.BuildConfig;
import com.ylj.daemon.YljService;

import org.xutils.x;

/**
 * Created by wyouflf on 15/10/28.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);

        startService(new Intent(this, YljService.class));
    }
}
