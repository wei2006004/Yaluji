package com.ylj.daemon.connect;

import com.ylj.daemon.config.ConnectState;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public class TcpConnector extends BaseConnector {

    private TcpService mService;

    TcpConnector(){
        mService = new TcpService();
        mService.setIOLister(new TcpService.IOListener() {
            @Override
            public void read(String msg) {
                onMessageArrive(msg);
            }

            @Override
            public void write(String msg) {

            }
        });
    }

    public void setAddress(String ip,int port){
        mService.setAddress(ip,port);
    }

    @Override
    public void connect() {
        mService.connect();
    }

    @Override
    public void disconnect() {
        mService.stop();
    }

    @Override
    public boolean isConnect() {
        return mService.getState() == ConnectState.STATE_CONNECTED;
    }

    @Override
    public void sendMessage(String msg) {
        if (isConnect()) {
            mService.write(msg);
        }
    }

    @Override
    public void setOnStateChangeListener(OnStateChangeListener listener) {
        mService.setOnStateChangeListener(listener);
    }
}
