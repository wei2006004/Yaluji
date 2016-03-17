package com.ylj.task;

import android.os.Bundle;

import com.ylj.common.BaseActivity;
import com.ylj.connect.IConnectCtrl;

/**
 * Created by Administrator on 2016/3/17 0017.
 */
public abstract class AbstractTestActivity extends BaseActivity{
    private TestControler mTestControler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTestControler = TestControler.newInstance(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        mTestControler.release();
    }

    public IConnectCtrl getConnectCtrl(){
        return mTestControler;
    }

    public ITestCtrl getTestCtrl(){
        return mTestControler;
    }
}
