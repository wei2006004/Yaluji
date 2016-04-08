package com.ylj.setting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.KeyListener;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.ylj.R;
import com.ylj.common.BaseActivity;
import com.ylj.common.config.Config;
import com.ylj.common.config.Global;
import com.ylj.setting.def.SettingDefault;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {

    KeyListener mIpKeyListener = new NumberKeyListener() {
        @Override
        protected char[] getAcceptedChars() {
            return new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.'};
        }

        @Override
        public int getInputType() {
            return InputType.TYPE_CLASS_PHONE;
        }
    };

    @ViewInject(R.id.switch_debug)
    Switch mDebugSwitch;

    @ViewInject(R.id.tv_ftp_ip)
    TextView mFtpIpText;

    @ViewInject(R.id.tv_ftp_port)
    TextView mFtpPortText;

    @ViewInject(R.id.tv_ftp_user)
    TextView mFtpUserText;

    @ViewInject(R.id.tv_ftp_passwd)
    TextView mFtpPasswdText;

    @ViewInject(R.id.tv_wifi_ip)
    TextView mWifiIpText;

    @ViewInject(R.id.tv_wifi_port)
    TextView mWifiPortText;

    @Event(R.id.layout_ftp_ip)
    private void onFtpIpLayoutClick(View view) {
        String ip = mFtpIpText.getText().toString();
        showStringEditDialog(getString(R.string.setting_dialog_ftp_ip), ip, new OnButtonClick<String>() {

            @Override
            public void onConfirm(DialogInterface dialog, String result) {
                Config config = Config.appInstance();
                config.setConfig(Global.PREF_TAG_FTP_IP, result);
                mFtpIpText.setText(result);
                dialog.dismiss();
            }

            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        }, mIpKeyListener);
    }

    @Event(R.id.layout_ftp_port)
    private void onFtpPortLayoutClick(View view) {
        int port = Integer.parseInt(mFtpPortText.getText().toString());
        showIntEditDialog(getString(R.string.setting_dialog_ftp_port), port, new OnButtonClick<Integer>() {
            @Override
            public void onConfirm(DialogInterface dialog, Integer result) {
                Config config = Config.appInstance();
                config.setConfig(Global.PREF_TAG_FTP_PORT, String.valueOf(result));
                mFtpPortText.setText(String.valueOf(result));
                dialog.dismiss();
            }

            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
    }

    @Event(R.id.layout_ftp_user)
    private void onFtpUserLayoutClick(View view) {
        String user = mFtpUserText.getText().toString();
        showStringEditDialog(getString(R.string.setting_dialog_ftp_user), user, new OnButtonClick<String>() {

            @Override
            public void onConfirm(DialogInterface dialog, String result) {
                Config config = Config.appInstance();
                config.setConfig(Global.PREF_TAG_FTP_USER, result);
                mFtpUserText.setText(result);
                dialog.dismiss();
            }

            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
    }

    @Event(R.id.layout_ftp_passwd)
    private void onFtpPasswdLayoutClick(View view) {
        String passwd = mFtpPasswdText.getText().toString();
        showStringEditDialog(getString(R.string.setting_dialog_ftp_passwd), passwd, new OnButtonClick<String>() {

            @Override
            public void onConfirm(DialogInterface dialog, String result) {
                Config config = Config.appInstance();
                config.setConfig(Global.PREF_TAG_FTP_PASSWD, result);
                mFtpPasswdText.setText(result);
                dialog.dismiss();
            }

            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
    }

    @Event(R.id.layout_wifi_ip)
    private void onWifiIpLayoutClick(View view) {
        String ip = mWifiIpText.getText().toString();
        showStringEditDialog(getString(R.string.setting_dialog_wifi_ip), ip, new OnButtonClick<String>() {

            @Override
            public void onConfirm(DialogInterface dialog, String result) {
                Config config = Config.appInstance();
                config.setConfig(Global.PREF_TAG_WIFI_IP, result);
                mWifiIpText.setText(result);
                dialog.dismiss();
            }

            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        }, mIpKeyListener);
    }

    @Event(R.id.layout_wifi_port)
    private void onWifiPortLayoutClick(View view) {
        int port = Integer.parseInt(mWifiPortText.getText().toString());
        showIntEditDialog(getString(R.string.setting_dialog_wifi_port), port, new OnButtonClick<Integer>() {
            @Override
            public void onConfirm(DialogInterface dialog, Integer result) {
                Config config = Config.appInstance();
                config.setConfig(Global.PREF_TAG_WIFI_PORT, String.valueOf(result));
                mWifiPortText.setText(String.valueOf(result));
                dialog.dismiss();
            }

            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
        initSwitch();
        initTexts();
    }

    private void initSwitch() {
        Config config = Config.appInstance();
        mDebugSwitch.setChecked(config.getBoolConfig(Global.PREF_TAG_DEBGU, SettingDefault.DEFAULT_TAG_DEBGU));
        mDebugSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Config config = Config.appInstance();
                config.setBoolConfig(Global.PREF_TAG_DEBGU,isChecked);
            }
        });
    }

    private void initTexts() {
        Config config = Config.appInstance();

        mFtpIpText.setText(config.getConfig(Global.PREF_TAG_FTP_IP, SettingDefault.DEFAULT_TAG_FTP_IP));
        mFtpPortText.setText(config.getConfig(Global.PREF_TAG_FTP_PORT, SettingDefault.DEFAULT_TAG_FTP_PORT));
        mFtpUserText.setText(config.getConfig(Global.PREF_TAG_FTP_USER, SettingDefault.DEFAULT_TAG_FTP_USER));
        mFtpPasswdText.setText(config.getConfig(Global.PREF_TAG_FTP_PASSWD, SettingDefault.DEFAULT_TAG_FTP_PASSWD));

        mWifiIpText.setText(config.getConfig(Global.PREF_TAG_WIFI_IP, SettingDefault.DEFAULT_TAG_WIFI_IP));
        mWifiPortText.setText(config.getConfig(Global.PREF_TAG_WIFI_PORT, SettingDefault.DEFAULT_TAG_WIFI_PORT));
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
