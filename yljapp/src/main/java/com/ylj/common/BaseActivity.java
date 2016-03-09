package com.ylj.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.xutils.x;

/**
 * Created by wyouflf on 15/11/4.
 */
public class BaseActivity extends AppCompatActivity implements IAlertable{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

    protected void post(){

    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void showLongToast(String message) {

    }

    @Override
    public void showAlert(String Title, String message) {

    }

    @Override
    public void showAlert(String Title, String message, View.OnClickListener okButtonListener) {

    }

    @Override
    public void showAlert(String Title, String message, View.OnClickListener okButtonListener, View.OnClickListener cancelButtonListener) {

    }
}
