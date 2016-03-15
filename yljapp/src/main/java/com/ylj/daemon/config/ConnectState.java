package com.ylj.daemon.config;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public class ConnectState {

    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    public static final int STATE_CONNECT_FAIL = 4;
    public static final int STATE_CONNECT_LOST = 5;

}
