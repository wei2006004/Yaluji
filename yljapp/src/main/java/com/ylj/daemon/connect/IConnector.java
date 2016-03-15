package com.ylj.daemon.connect;

import com.ylj.daemon.msghandler.IMessageHandler;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public interface IConnector {

    void connect();
    void disconnect();

    boolean isConnect();
    
    void sendMessage(String msg);
    void sendDeviceMessage();
    void sendStartMessage();
    void sendStopMessage();

    void setMessageHandler(IMessageHandler handler);

    void setOnStateChangeListener(OnStateChangeListener listener);

    interface OnStateChangeListener{
        void onStateChange(int state);
    }
}
