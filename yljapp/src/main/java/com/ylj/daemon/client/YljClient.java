package com.ylj.daemon.client;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ylj.common.bean.Task;
import com.ylj.common.bean.Test;
import com.ylj.connect.bean.DeviceInfo;
import com.ylj.daemon.bean.TaskResult;
import com.ylj.daemon.config.ConnectState;
import com.ylj.daemon.config.ServiceAction;
import com.ylj.daemon.connect.BtConnector;
import com.ylj.daemon.connect.DebugConnector;
import com.ylj.daemon.connect.IConnector;
import com.ylj.daemon.connect.TcpConnector;
import com.ylj.daemon.ftp.FtpManagerImpl;
import com.ylj.daemon.ftp.FtpState;
import com.ylj.task.ftp.IFtpCtrlListener;
import com.ylj.daemon.ftp.IFtpManager;
import com.ylj.daemon.manager.ITaskStateManager;
import com.ylj.daemon.manager.TaskStateManager;
import com.ylj.daemon.msghandler.IMessageHandler;
import com.ylj.daemon.msghandler.MessageHandlerImpl;
import com.ylj.daemon.bean.DeviceData;
import com.ylj.task.bean.ColorData;
import com.ylj.task.bean.DrawData;
import com.ylj.task.bean.TraceData;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/3/15 0015.
 */
public class YljClient extends BaseClient implements IConnector.OnStateChangeListener,
        IMessageHandler.OnHandleListener, ITaskStateManager.OnTaskHandleListener, IFtpManager.OnFtpStateChangeListener {

    public static final int MODE_ADJUST = 0;
    public static final int MODE_TEST = 1;

    private int mState = ConnectState.STATE_NONE;

    private int mMode = MODE_ADJUST;

    IConnector mConnector;
    IMessageHandler mMessageHandler;
    ITaskStateManager mTaskStateManager;
    IFtpManager mFtpManager;

    public YljClient(Context context) {
        super(context);
        mMessageHandler = new MessageHandlerImpl(this);
    }

    @Override
    public void connectToBluetooth(BluetoothDevice device) {
        disconnect();

        BtConnector connector = new BtConnector();
        connector.setBluetoothDevice(device);
        mConnector = connector;
        mConnector.setOnStateChangeListener(this);
        mConnector.setMessageHandler(mMessageHandler);
        mConnector.connect();
    }

    @Override
    public void connectToWifi(String ip, int port) {
        disconnect();

        TcpConnector connector = new TcpConnector();
        connector.setAddress(ip, port);
        mConnector = connector;
        mConnector.setOnStateChangeListener(this);
        mConnector.setMessageHandler(mMessageHandler);
        mConnector.connect();
    }

    @Override
    public void connectToDebug() {
        disconnect();

        DebugConnector connector = new DebugConnector();
        mConnector = connector;
        mConnector.setOnStateChangeListener(this);
        mConnector.setMessageHandler(mMessageHandler);
        mConnector.connect();
    }

    @Override
    public void disconnect() {
        if (mConnector != null) {
            mConnector.disconnect();
            sendBroadcast(ServiceAction.ACTION_DISCONNECTED);
        }
    }

    @Override
    public void reconnect() {
        if (mConnector != null) {
            mConnector.connect();
        }
    }

    @Override
    public boolean isConnect() {
        return (mConnector != null) && (mState == ConnectState.STATE_CONNECTED);
    }

    @Override
    public void requestDeviceInfo() {
        if (isConnect()) {
            mConnector.sendDeviceMessage();
        }
    }

    @Override
    public void startAdjust() {
        if (isConnect()) {
            mMode = MODE_ADJUST;
            mConnector.sendStartMessage();
            sendBroadcast(ServiceAction.ACTION_SAMPLE_CTRL_STATE_CHANGE, ServiceAction.CTRL_FLAG_START);
        }
    }

    @Override
    public void stopAdjust() {
        if (isConnect()) {
            mConnector.sendStopMessage();
            sendBroadcast(ServiceAction.ACTION_SAMPLE_CTRL_STATE_CHANGE, ServiceAction.CTRL_FLAG_STOP);
        }
    }

    @Override
    public void loadTask(Task task) {
        mTaskStateManager = new TaskStateManager();
        mTaskStateManager.setOnTaskHandleListener(this);
        mTaskStateManager.loadTask(task);
    }

    @Override
    public void finishTask(Test test) {
        if (mTaskStateManager != null) {
            mTaskStateManager.finishTask(test);
        }
    }

    @Override
    public void finishTest(Test test) {
        if (mTaskStateManager != null) {
            mTaskStateManager.finishTest(test);
        }
    }

    @Override
    public void ftpLogin(String address, int port, String user, String passwd) {
        mFtpManager = new FtpManagerImpl();
        mFtpManager.setOnFtpStateChangeListener(this);
        mFtpManager.login(address, port, user, passwd);
    }

    @Override
    public void logout() {
        if (mFtpManager != null)
            mFtpManager.logout();
    }

    @Override
    public void upload(String filePath) {
        if (mFtpManager != null)
            mFtpManager.upload(filePath);
    }

    @Override
    public void cancelUpload() {
        if (mFtpManager != null)
            mFtpManager.cancel();
    }

    @Override
    public void startTest() {
        if (!isConnect())
            return;
        mMode = MODE_TEST;
        mConnector.sendStartMessage();
        mTaskStateManager.startTest();
        sendBroadcast(ServiceAction.ACTION_SAMPLE_CTRL_STATE_CHANGE, ServiceAction.CTRL_FLAG_START);
    }

    @Override
    public void puaseTest() {
        if (isConnect()) {
            mConnector.sendStopMessage();
            mTaskStateManager.pauseTest();
            sendBroadcast(ServiceAction.ACTION_SAMPLE_CTRL_STATE_CHANGE, ServiceAction.CTRL_FLAG_STOP);
        }
    }

    @Override
    public void onConnectStateChange(int state) {
        mState = state;
        sendBroadcast(ServiceAction.ACTION_CONNECT_STATE_CHANGE, state);

        if (state == ConnectState.STATE_CONNECTED) {
            mConnector.sendDeviceMessage();
        }
    }

    @Override
    public void onHandleDeviceInfo(DeviceInfo info) {
        Log.d("yljclient", "info:" + info.getDeviceId());
        sendBroadcast(ServiceAction.ACTION_DEVICE_INFO, ServiceAction.EXTRA_DEVICE_INFO, info);
    }

    @Override
    public void onHandleDeviceData(DeviceData data) {
        if (mMode == MODE_ADJUST) {
            Log.d("yljclient", "data:" + data.toString());
            sendBroadcast(ServiceAction.ACTION_ADJUST_DATA, ServiceAction.EXTRA_ADJUST_DATA, data);
        }
        if (mMode == MODE_TEST && mTaskStateManager != null) {
            mTaskStateManager.addTestData(data);
        }
    }

    @Override
    public void onHandleWrongMessage(String msg) {
        Log.e("yljclient", "message:" + msg);
    }

    @Override
    public void onLoadTaskStart() {
        sendBroadcast(ServiceAction.ACTION_START_LOAD_TASK);
    }

    @Override
    public void onLoadTaskFinish(ArrayList<TraceData> traceDatas, ArrayList<ColorData> colorDatas, TaskResult result) {
        Intent intent = new Intent();
        intent.setAction(ServiceAction.ACTION_LOAD_TASK_FINISH);
        intent.putExtra(ServiceAction.EXTRA_TASK_RESULT, result);
        intent.putParcelableArrayListExtra(ServiceAction.EXTRA_TRACE_DATA_LIST, traceDatas);
        intent.putParcelableArrayListExtra(ServiceAction.EXTRA_COLOR_DATA_LIST, colorDatas);
        getContext().sendBroadcast(intent);
    }

    @Override
    public void onTaskResultCreated(TaskResult result) {
        sendBroadcast(ServiceAction.ACTION_TASK_RESULT_CREATED, ServiceAction.EXTRA_TASK_RESULT, result);
    }

    @Override
    public void onTestFinished() {
        sendBroadcast(ServiceAction.ACTION_TEST_FINISHED);
    }

    @Override
    public void onDrawDataRefresh(DrawData data) {
        sendBroadcast(ServiceAction.ACTION_DRAW_DATA, ServiceAction.EXTRA_DRAW_DATA, data);
    }

    @Override
    public void onFtpStateChange(int state) {
        switch (state) {
            case FtpState.STATE_LOGIN_FAIL:
            case FtpState.STATE_LOGIN_SUCCESS:
            case FtpState.STATE_LOGOUT:
            case FtpState.STATE_SERVER_CONNECT_FAIL:
            case FtpState.STATE_CONNECT_LOST:
                sendBroadcast(ServiceAction.ACTION_FTP_STATE_CREATED, ServiceAction.EXTRA_ACTION_FLAG, state);
                break;
            case FtpState.STATE_UPLOAD_START:
                sendBroadcast(ServiceAction.ACTION_FTP_UPLOAD_START);
                break;
            case FtpState.STATE_UPLOAD_FINISH:
                sendBroadcast(ServiceAction.ACTION_FTP_UPLOAD_FINISH);
                break;
            case FtpState.STATE_UPLOAD_CANCEL:
                sendBroadcast(ServiceAction.ACTION_FTP_UPLOAD_CANCEL);
                break;
            case FtpState.STATE_UPLOAD_ERROR:
                sendBroadcast(ServiceAction.ACTION_FTP_UPLOAD_ERROR);
                break;
        }
    }

    @Override
    public void onUploadProgress(int progress) {
        sendBroadcast(ServiceAction.ACTION_FTP_UPLOAD_PROGRESS, ServiceAction.EXTRA_PROGRESS, progress);
    }
}
