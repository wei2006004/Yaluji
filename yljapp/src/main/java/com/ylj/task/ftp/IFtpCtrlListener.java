package com.ylj.task.ftp;

/**
 * Created by Administrator on 2016/4/7 0007.
 */
public interface IFtpCtrlListener {

    void onFtpLoginFail(int error);
    void onFtpLoginSucess();
    void onFtpConnectLost();

    void onUploadStart();
    void onUploadFinish();
    void onUploadCancel();
    void onUploadProgress(int progress);
}
