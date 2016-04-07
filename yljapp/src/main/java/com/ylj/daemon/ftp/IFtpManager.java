package com.ylj.daemon.ftp;

import com.ylj.task.ftp.IFtpCtrlListener;

/**
 * Created by Administrator on 2016/4/7 0007.
 */
public interface IFtpManager {

    void login(String address,int port,String user,String passwd);
    void logout();
    void upload(String filePath);
    void cancel();

    void setOnFtpStateChangeListener(OnFtpStateChangeListener listener);

    interface OnFtpStateChangeListener{
        void onFtpStateChange(int state);
        void onUploadProgress(int progress);
    }

}
