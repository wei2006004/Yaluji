package com.ylj.task;

import android.os.Bundle;

import com.ylj.common.BaseActivity;
import com.ylj.connect.ConnectCtrlActivity;
import com.ylj.connect.IConnectCtrl;

/**
 * Created by Administrator on 2016/3/17 0017.
 */
public abstract class AbstractTestActivity extends ConnectCtrlActivity{
    private TestControler mTestControler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mTestControler == null)
            mTestControler = TestControler.newInstance(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mTestControler.release();
    }

    @Override
    public IConnectCtrl getConnectCtrl(){
        if(mTestControler == null)
            mTestControler = TestControler.newInstance(this);
        return mTestControler;
    }

    public ITestCtrl getTestCtrl(){
        if(mTestControler == null)
            mTestControler = TestControler.newInstance(this);
        return mTestControler;
    }
}
