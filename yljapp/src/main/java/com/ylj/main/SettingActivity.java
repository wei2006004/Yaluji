package com.ylj.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ylj.R;
import com.ylj.common.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {

    @ViewInject(R.id.tv_ftp_ip)
    TextView mFtpIpText;

    @ViewInject(R.id.tv_ftp_port)
    TextView mFtpPortText;

    @ViewInject(R.id.tv_ftp_user)
    TextView mFtpIUserText;

    @ViewInject(R.id.tv_ftp_passwd)
    TextView mFtpPasswdText;

    @ViewInject(R.id.tv_wifi_ip)
    TextView mWifiIpText;

    @ViewInject(R.id.tv_wifi_port)
    TextView mWifiPortText;

    @Event(R.id.layout_ftp_ip)
    private void onFtpIpLayoutClick(View view){
        mFtpIpText.setText("hhhhhh");
    }

    @Event(R.id.layout_ftp_port)
    private void onFtpPortLayoutClick(View view){

    }

    @Event(R.id.layout_ftp_user)
    private void onFtpUserLayoutClick(View view){

    }

    @Event(R.id.layout_ftp_passwd)
    private void onFtpPasswdLayoutClick(View view){

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
