package com.ylj.task.ftp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;

import com.ylj.R;
import com.ylj.common.BaseActivity;
import com.ylj.daemon.ftp.FtpState;

/**
 * Created by Administrator on 2016/4/7 0007.
 */
public abstract class AbstractFtpActivity extends BaseActivity implements IFtpCtrlListener {

    private FtpControler mFtpControler;
    private ProgressDialog mProgressDialog;

    private String mUploadFile;

    protected IFtpCtrl getFtpCtrl() {
        if (mFtpControler == null)
            mFtpControler = FtpControler.newInstance(this);
        return mFtpControler;
    }

    protected void uploadFile(String address, int port, String user, String passwd, String filePath) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("upload task");
        mProgressDialog.setMessage("login...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgress(0);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.alert_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setProgressDialogText("cancel upload file...");
                mFtpControler.cancel();
            }
        });
        mProgressDialog.show();

        mUploadFile = filePath;
        mFtpControler.login(address,port,user,passwd);
    }

    private void setProgressDialogText(String text){
        mProgressDialog.setMessage(text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mFtpControler == null)
            mFtpControler = FtpControler.newInstance(this);
        mFtpControler.setFtpCtrlListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFtpControler.release();
    }

    @Override
    public void onFtpLoginFail(int error) {
        switch (error){
            case FtpState.STATE_SERVER_CONNECT_FAIL:
                setProgressDialogText("cannot connect to server");
                break;
            case FtpState.STATE_LOGIN_FAIL:
                setProgressDialogText("login fail");
                break;
            default:
                setProgressDialogText("server conncet fail");
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
            }
        },1000);
        showToast(R.string.toast_upload_fail);
    }

    @Override
    public void onFtpLoginSucess() {
        setProgressDialogText("login sucess");
        mFtpControler.upload(mUploadFile);
    }

    @Override
    public void onFtpConnectLost() {
        setProgressDialogText("server conncet lost");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
            }
        }, 1000);
        showToast(R.string.toast_upload_fail);
    }

    @Override
    public void onUploadStart() {
        setProgressDialogText("start upload");
    }

    @Override
    public void onUploadFinish() {
        setProgressDialogText("upload sucess");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
            }
        }, 500);
        showToast(R.string.toast_upload_success);
    }

    @Override
    public void onUploadCancel() {
        setProgressDialogText("upload cancel");
        mProgressDialog.dismiss();
        mFtpControler.logout();
    }

    @Override
    public void onUploadFail() {
        setProgressDialogText("upload error");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
            }
        }, 1000);
        showToast(R.string.toast_upload_fail);
    }

    @Override
    public void onUploadProgress(int progress) {
        setProgressDialogText("uploading...");
        mProgressDialog.setProgress(progress);
    }
}
