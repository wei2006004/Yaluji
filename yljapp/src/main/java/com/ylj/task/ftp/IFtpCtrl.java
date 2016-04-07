package com.ylj.task.ftp;

/**
 * Created by Administrator on 2016/4/7 0007.
 */
public interface IFtpCtrl {

    void login(String address,int port,String user,String passwd);
    void logout();
    void upload(String filePath);
    void cancel();

    void setOnFtpCtrlListener(OnFtpCtrlListener listener);

    interface OnFtpCtrlListener{
        void onFtpLoginFail(int error);
        void onFtpLoginSucess();
        void onFtpConnectLost();

        void onUploadStart();
        void onUploadFinish();
        void onUploadCancel();
        void onUploadProgress(int progress);
    }
}
