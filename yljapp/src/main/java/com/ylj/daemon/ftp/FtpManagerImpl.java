package com.ylj.daemon.ftp;

import org.xutils.x;

import java.io.IOException;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

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
                    mFtpClient.connect(address,port);
                    mFtpClient.login(user,passwd);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (FTPIllegalReplyException e) {
                    e.printStackTrace();
                } catch (FTPException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void logout() {
        if(mFtpClient == null)
            return;
        x.task().run(new Runnable() {
            @Override
            public void run() {
                try {
                    mFtpClient.logout();
                    mFtpClient.disconnect(false);
                    mFtpClient = null;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (FTPIllegalReplyException e) {
                    e.printStackTrace();
                } catch (FTPException e) {
                    e.printStackTrace();
                }
            }
        });
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
