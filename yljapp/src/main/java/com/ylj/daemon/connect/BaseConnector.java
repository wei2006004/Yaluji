package com.ylj.daemon.connect;

import org.xutils.x;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public abstract class BaseConnector implements IConnector {

    public final static String MESSAGE_DEVICE = "1";
    public final static String MESSAGE_START = "2";
    public final static String MESSAGE_STOP = "3";

    public static final char START_FLAG = 0x02;
    public static final char END_FLAG = 0x04;

    public void sendDeviceMessage() {
        sendMessage(MESSAGE_DEVICE);
    }

    public void sendStartMessage() {
        sendMessage(MESSAGE_START);
    }

    public void sendStopMessage() {
        sendMessage(MESSAGE_STOP);
    }

    protected synchronized void onMessageArrive(String msg) {
        if (!combineAndTestMessage(msg))
            return;
        final String message = mMessage.toString();
        mMessage = new StringBuilder();
        x.task().run(new Runnable() {
            @Override
            public void run() {
                mMessageHandler.onHandleMessage(message);
            }
        });
    }

    private StringBuilder mMessage = new StringBuilder();

    private boolean combineAndTestMessage(String msg) {
        if (msg.startsWith("" + ComConst.START_FLAG) && msg.endsWith("" + ComConst.END_FLAG)) {
            mMessage.append(msg.substring(1, msg.length() - 1));
            return true;
        }
        if (msg.endsWith("" + ComConst.END_FLAG)) {
            if (msg.contains("" + ComConst.START_FLAG)) {
                mMessage.setLength(0);
                return false;
            }
            mMessage.append(msg.substring(0, msg.length() - 1));
            return true;
        }
        if (msg.startsWith("" + ComConst.START_FLAG)) {
            mMessage = new StringBuilder(msg.substring(1));
            return false;
        }
        mMessage.append(msg);
        return false;
    }

    protected IMessageHandler mMessageHandler;

    @Override
    public void setMessageHandler(IMessageHandler handler) {
        mMessageHandler = handler;
    }

    protected OnStateChangeListener mOnStateChangeListener;

    @Override
    public void setOnStateChangeListener(OnStateChangeListener listener) {
        mOnStateChangeListener = listener;
    }

}