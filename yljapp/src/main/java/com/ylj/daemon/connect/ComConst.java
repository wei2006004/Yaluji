package com.ylj.daemon.connect;

public final class ComConst {
    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    public static final int STATE_CONNECT_FAIL = 4;
    public static final int STATE_CONNECT_LOST = 5;
    
	public static final char START_FLAG=0x02;
	public static final char END_FLAG=0x04;
	
	public static final int COM_DEVICE_INFO=0;
	public static final int COM_READ_ERROR=2;

}
