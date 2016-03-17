package com.ylj.task;

import com.ylj.common.bean.Task;
import com.ylj.common.bean.Test;
import com.ylj.daemon.bean.Record;
import com.ylj.daemon.bean.TaskResult;
import com.ylj.task.bean.ColorData;
import com.ylj.task.bean.TraceData;

import java.util.List;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public interface ITestCtrl {

    void startTest();
    void pauseTest();
    void finishTest(Test test);

    void loadTask(Task task);
    void finishTask();

    void addTestCtrlListener(OnTestCtrlListener listener);
    void deleteTestCtrlListener(OnTestCtrlListener listener);

    interface OnTestCtrlListener {
        void onTestStart();
        void onTestPasue();
        void onTestFinish();

        void onLoadTaskStart();
        void onLoadTaskFinish();
        void onTaskResultCreated(TaskResult result);
    }

    void setOnTraceDrawListener(OnDrawListener<TraceData> listener);
    void setOnColorDrawListener(OnDrawListener<ColorData> listener);

    interface OnDrawListener<T>{
        void onLoadDataStart();
        void onLoadDataFinish(List<T> datas , TaskResult result);
        void onAddData(T data);
    }

    void addOnTestDataRefreshListener(OnTestDataRefreshListener listener);
    void deleteOnTestDataRefreshListener(OnTestDataRefreshListener listener);

    interface OnTestDataRefreshListener{
        void onRefresh(Record data);
    }
}
