package com.ylj.daemon.ftp;

/**
 * Created by Administrator on 2016/4/7 0007.
 */
public class FtpManagerImpl implements IFtpManager {
    @Override
    public void login(String address, int port, String user, String passwd) {

    }

    @Override
    public void logout() {

    }

    @Override
    public void upload(String filePath) {

    }

    @Override
    public void cancel() {

    }

    OnFtpStateChangeListener mListener;

    @Override
    public void setOnFtpStateChangeListener(OnFtpStateChangeListener listener) {
        mListener = listener;
    }

}
