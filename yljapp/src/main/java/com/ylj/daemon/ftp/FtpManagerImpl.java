package com.ylj.daemon.ftp;

import org.xutils.x;

import java.io.File;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

/**
 * Created by Administrator on 2016/4/7 0007.
 */
public class FtpManagerImpl implements IFtpManager {

    FTPClient mFtpClient;

    @Override
    public void login(final String address, final int port, final String user, final String passwd) {
        mFtpClient = new FTPClient();
        x.task().run(new Runnable() {
            @Override
            public void run() {
                try {
                    mFtpClient.connect(address, port);
                } catch (Exception e) {
                    if (mListener != null) {
                        mListener.onFtpStateChange(FtpState.STATE_SERVER_CONNECT_FAIL);
                    }
                    return;
                }
                try {
                    mFtpClient.login(user, passwd);
                } catch (Exception e) {
                    if (mListener != null) {
                        mListener.onFtpStateChange(FtpState.STATE_LOGIN_FAIL);
                    }
                    return;
                }
                mListener.onFtpStateChange(FtpState.STATE_LOGIN_SUCCESS);
            }
        });

    }

    @Override
    public void logout() {
        if (mFtpClient == null)
            return;
        x.task().run(new Runnable() {
            @Override
            public void run() {
                try {
                    mFtpClient.logout();
                    mFtpClient.disconnect(false);
                    mFtpClient = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mListener.onFtpStateChange(FtpState.STATE_LOGOUT);
            }
        });
    }

    @Override
    public void upload(final String filePath) {
        if (mFtpClient == null)
            return;
        x.task().run(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(filePath);
                    final long size = file.length();
                    mFtpClient.upload(file, new FTPDataTransferListener() {
                        @Override
                        public void started() {
                            if (mListener != null)
                                mListener.onFtpStateChange(FtpState.STATE_UPLOAD_START);
                        }

                        @Override
                        public void transferred(int i) {
                            int progress = (int)((double) i / size * 100);
                            if (mListener != null)
                                mListener.onUploadProgress(progress);
                        }

                        @Override
                        public void completed() {
                            if (mListener != null)
                                mListener.onFtpStateChange(FtpState.STATE_UPLOAD_FINISH);
                        }

                        @Override
                        public void aborted() {
                            if (mListener != null)
                                mListener.onFtpStateChange(FtpState.STATE_UPLOAD_CANCEL);
                        }

                        @Override
                        public void failed() {
                            if (mListener != null)
                                mListener.onFtpStateChange(FtpState.STATE_UPLOAD_ERROR);
                        }
                    });
                } catch (Exception e) {
                    if (mListener != null)
                        mListener.onFtpStateChange(FtpState.STATE_UPLOAD_ERROR);
                }
            }
        });
    }

    @Override
    public void cancel() {
        try {
            mFtpClient.abortCurrentDataTransfer(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    OnFtpStateChangeListener mListener;

    @Override
    public void setOnFtpStateChangeListener(OnFtpStateChangeListener listener) {
        mListener = listener;
    }

}
