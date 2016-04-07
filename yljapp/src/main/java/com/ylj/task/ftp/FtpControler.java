package com.ylj.task.ftp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.ylj.common.Controler;
import com.ylj.daemon.YljService;
import com.ylj.daemon.config.ServiceAction;
import com.ylj.daemon.ftp.FtpState;

/**
 * Created by Administrator on 2016/4/7 0007.
 */
public class FtpControler extends Controler implements IFtpCtrl {

    private BroadcastReceiver mFtpReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("FtpControler", "action:" + action);
            if (action.equals(ServiceAction.ACTION_FTP_STATE_CREATED)) {
                int state = intent.getIntExtra(ServiceAction.EXTRA_ACTION_FLAG, FtpState.STATE_NONE);
                switch (state) {
                    case FtpState.STATE_NONE:
                    case FtpState.STATE_LOGIN_FAIL:
                    case FtpState.STATE_SERVER_CONNECT_FAIL:
                        mFtpCtrlListener.onFtpLoginFail(state);
                        break;
                    case FtpState.STATE_LOGIN_SUCCESS:
                        mFtpCtrlListener.onFtpLoginSucess();
                        break;
                    case FtpState.STATE_LOGOUT:
                        break;
                    case FtpState.STATE_CONNECT_LOST:
                        mFtpCtrlListener.onFtpConnectLost();
                        break;
                }
            }else if (action.equals(ServiceAction.ACTION_FTP_UPLOAD_START)) {
                mFtpCtrlListener.onUploadStart();
            }else if (action.equals(ServiceAction.ACTION_FTP_UPLOAD_FINISH)) {
                mFtpCtrlListener.onUploadFinish();
            }else if (action.equals(ServiceAction.ACTION_FTP_UPLOAD_CANCEL)) {
                mFtpCtrlListener.onUploadCancel();
            }else if (action.equals(ServiceAction.ACTION_FTP_UPLOAD_ERROR)) {
                mFtpCtrlListener.onUploadFail();
            }else if (action.equals(ServiceAction.ACTION_FTP_UPLOAD_PROGRESS)) {
                int progress = intent.getIntExtra(ServiceAction.EXTRA_PROGRESS, 0);
                mFtpCtrlListener.onUploadProgress(progress);
            }
        }
    };

    public static FtpControler newInstance(Activity activity) {
        FtpControler controler = new FtpControler();
        controler.init(activity, YljService.class);
        return controler;
    }

    @Override
    protected void onServiceConnected(IBinder binder) {
        super.onServiceConnected(binder);

        IntentFilter filter = new IntentFilter(ServiceAction.ACTION_FTP_STATE_CREATED);
        getActivity().registerReceiver(mFtpReceiver, filter);
        filter = new IntentFilter(ServiceAction.ACTION_FTP_UPLOAD_START);
        getActivity().registerReceiver(mFtpReceiver, filter);
        filter = new IntentFilter(ServiceAction.ACTION_FTP_UPLOAD_FINISH);
        getActivity().registerReceiver(mFtpReceiver, filter);
        filter = new IntentFilter(ServiceAction.ACTION_FTP_UPLOAD_CANCEL);
        getActivity().registerReceiver(mFtpReceiver, filter);
        filter = new IntentFilter(ServiceAction.ACTION_FTP_UPLOAD_ERROR);
        getActivity().registerReceiver(mFtpReceiver, filter);
        filter = new IntentFilter(ServiceAction.ACTION_FTP_UPLOAD_PROGRESS);
        getActivity().registerReceiver(mFtpReceiver, filter);
    }

    @Override
    public void release() {
        super.release();
        getActivity().unregisterReceiver(mFtpReceiver);
        mFtpCtrlListener = null;
    }

    @Override
    public void login(String address, int port, String user, String passwd) {
        if (getCleint() != null) {
            getCleint().ftpLogin(address, port, user, passwd);
        }
    }

    @Override
    public void logout() {
        if (getCleint() != null) {
            getCleint().logout();
        }
    }

    @Override
    public void upload(String filePath) {
        if (getCleint() != null) {
            getCleint().upload(filePath);
        }
    }

    @Override
    public void cancel() {
        if (getCleint() != null) {
            getCleint().cancelUpload();
        }
    }

    private IFtpCtrlListener mFtpCtrlListener;

    @Override
    public void setFtpCtrlListener(IFtpCtrlListener listener) {
        mFtpCtrlListener = listener;
    }
}
