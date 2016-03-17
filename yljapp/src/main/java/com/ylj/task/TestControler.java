package com.ylj.task;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.ylj.common.bean.Task;
import com.ylj.common.bean.Test;
import com.ylj.connect.ConnectControler;
import com.ylj.daemon.YljService;
import com.ylj.daemon.bean.DeviceData;
import com.ylj.daemon.bean.Record;
import com.ylj.daemon.bean.TaskResult;
import com.ylj.daemon.config.ServiceAction;
import com.ylj.task.bean.ColorData;
import com.ylj.task.bean.DrawData;
import com.ylj.task.bean.TraceData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public class TestControler extends ConnectControler implements ITestCtrl {

    public static TestControler newInstance(Activity activity) {
        TestControler controler = new TestControler();
        controler.init(activity, YljService.class);
        return controler;
    }

    private BroadcastReceiver mTestReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(ServiceAction.ACTION_SAMPLE_CTRL_STATE_CHANGE)) {
                onActionCtrlStateChange(intent);
            } else if (action.equals(ServiceAction.ACTION_DRAW_DATA)) {
                onActionDrawDataRefresh(intent);
            } else if (action.equals(ServiceAction.ACTION_START_LOAD_TASK)) {
                onActionStartLoadTask(intent);
            } else if (action.equals(ServiceAction.ACTION_LOAD_TASK_FINISH)) {
                onActionLoadTaskFinish(intent);
            } else if (action.equals(ServiceAction.ACTION_TASK_RESULT_CREATED)) {
                onActionTaskResultCreated(intent);
            }
        }
    };

    private void onActionCtrlStateChange(Intent intent) {
        int flag = intent.getIntExtra(ServiceAction.EXTRA_ACTION_FLAG, ServiceAction.CTRL_FLAG_STOP);
        if (flag == ServiceAction.CTRL_FLAG_STOP) {
            for (OnTestCtrlListener listener : mTestCtrlListeners) {
                listener.onTestPasue();
            }
        } else if (flag == ServiceAction.CTRL_FLAG_START) {
            for (OnTestCtrlListener listener : mTestCtrlListeners) {
                listener.onTestStart();
            }
        }
    }

    private void onActionDrawDataRefresh(Intent intent) {
        DrawData drawData = intent.getParcelableExtra(ServiceAction.EXTRA_DRAW_DATA);
        TraceData traceData = drawData.getTraceData();
        ColorData colorData = drawData.getColorData();
        Record record = drawData.getRecord();

        if (traceData != null && mOnTraceDrawListener != null) {
            mOnTraceDrawListener.onAddData(traceData);
        }
        if (colorData != null && mOnColorDrawListener != null) {
            mOnColorDrawListener.onAddData(colorData);
        }
        if (record != null) {
            for (OnTestDataRefreshListener listener : mTestDataRefreshListeners) {
                listener.onRefresh(record);
            }
        }
    }

    private void onActionStartLoadTask(Intent intent) {
        for (OnTestCtrlListener listener : mTestCtrlListeners) {
            listener.onLoadTaskStart();
        }
        if (mOnTraceDrawListener != null) {
            mOnTraceDrawListener.onLoadDataStart();
        }
        if (mOnColorDrawListener != null) {
            mOnColorDrawListener.onLoadDataStart();
        }
    }

    private void onActionLoadTaskFinish(Intent intent) {
        List<TraceData> traceDatas = intent.getParcelableArrayListExtra(ServiceAction.EXTRA_TRACE_DATA_LIST);
        List<ColorData> colorDatas = intent.getParcelableArrayListExtra(ServiceAction.EXTRA_COLOR_DATA_LIST);
        TaskResult taskResult = intent.getParcelableExtra(ServiceAction.EXTRA_TASK_RESULT);

        for (OnTestCtrlListener listener : mTestCtrlListeners) {
            listener.onLoadTaskFinish();
        }
        if (mOnTraceDrawListener != null) {
            mOnTraceDrawListener.onLoadDataFinish(traceDatas, taskResult);
        }
        if (mOnColorDrawListener != null) {
            mOnColorDrawListener.onLoadDataFinish(colorDatas, taskResult);
        }
    }

    private void onActionTaskResultCreated(Intent intent) {
        TaskResult taskResult = intent.getParcelableExtra(ServiceAction.EXTRA_TASK_RESULT);
        for (OnTestCtrlListener listener : mTestCtrlListeners) {
            listener.onTaskResultCreated(taskResult);
        }
    }

    @Override
    protected void onServiceConnected(IBinder binder) {
        super.onServiceConnected(binder);

        registerAction(ServiceAction.ACTION_SAMPLE_CTRL_STATE_CHANGE);
        registerAction(ServiceAction.ACTION_DRAW_DATA);
        registerAction(ServiceAction.ACTION_START_LOAD_TASK);
        registerAction(ServiceAction.ACTION_LOAD_TASK_FINISH);
        registerAction(ServiceAction.ACTION_TASK_RESULT_CREATED);
    }

    private void registerAction(String action) {
        IntentFilter filter = new IntentFilter(action);
        mActivity.registerReceiver(mTestReceiver, filter);
    }

    @Override
    public void release() {
        super.release();

        mActivity.unregisterReceiver(mTestReceiver);
        mTestCtrlListeners.clear();
        mTestDataRefreshListeners.clear();
        mOnColorDrawListener = null;
        mOnTraceDrawListener = null;
    }

    @Override
    public void startTest() {
        if (!isConnect())
            return;
        mCleint.startTest();
    }

    @Override
    public void pauseTest() {
        if (!isConnect())
            return;
        mCleint.puaseTest();
    }

    @Override
    public void finishTest(Test test) {
        if (mCleint != null) {
            mCleint.finishTest(test);
        }
    }

    @Override
    public void loadTask(Task task) {
        if (mCleint != null) {
            mCleint.loadTask(task);
        }
    }

    @Override
    public void finishTask() {
        if (mCleint != null) {
            mCleint.finishTask();
        }
    }

    private List<OnTestCtrlListener> mTestCtrlListeners = new ArrayList<>();

    @Override
    public void addTestCtrlListener(OnTestCtrlListener listener) {
        if (mTestCtrlListeners.contains(listener))
            return;
        mTestCtrlListeners.add(listener);
    }

    @Override
    public void deleteTestCtrlListener(OnTestCtrlListener listener) {
        if (mTestCtrlListeners.contains(listener)) {
            mTestCtrlListeners.remove(listener);
        }
    }

    private OnDrawListener<TraceData> mOnTraceDrawListener;

    @Override
    public void setOnTraceDrawListener(OnDrawListener<TraceData> listener) {
        mOnTraceDrawListener = listener;
    }

    private OnDrawListener<ColorData> mOnColorDrawListener;

    @Override
    public void setOnColorDrawListener(OnDrawListener<ColorData> listener) {
        mOnColorDrawListener = listener;
    }

    private List<OnTestDataRefreshListener> mTestDataRefreshListeners = new ArrayList<>();

    @Override
    public void addOnTestDataRefreshListener(OnTestDataRefreshListener listener) {
        if (mTestDataRefreshListeners.contains(listener))
            return;
        mTestDataRefreshListeners.add(listener);
    }

    @Override
    public void deleteOnTestDataRefreshListener(OnTestDataRefreshListener listener) {
        if (mTestDataRefreshListeners.contains(listener)) {
            mTestDataRefreshListeners.remove(listener);
        }
    }
}
