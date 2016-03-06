package com.ylj.db.manager;

import org.xutils.x;

/**
 * Created by Administrator on 2016/3/6 0006.
 */
public abstract class AbstractManager {

    protected void runInBackground(Runnable runnable){
        x.task().run(runnable);
    }

    protected void postToUi(Runnable runnable){
        x.task().post(runnable);
    }
}
