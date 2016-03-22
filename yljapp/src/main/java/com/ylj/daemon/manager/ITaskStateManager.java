package com.ylj.daemon.manager;

import com.ylj.common.bean.Task;
import com.ylj.common.bean.Test;
import com.ylj.daemon.bean.DeviceData;
import com.ylj.daemon.bean.TaskResult;
import com.ylj.task.bean.ColorData;
import com.ylj.task.bean.DrawData;
import com.ylj.task.bean.TraceData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/17 0017.
 */
public interface ITaskStateManager {

    void loadTask(Task task);
    void finishTask(Test test);

    void startTest();
    void pauseTest();

    void addTestData(DeviceData data);
    void finishTest(Test test);

    void setOnTaskHandleListener(OnTaskHandleListener listener);

    interface OnTaskHandleListener{
        void onLoadTaskStart();
        void onLoadTaskFinish(ArrayList<TraceData> traceDatas,ArrayList<ColorData> colorDatas,TaskResult result);

        void onTaskResultCreated(TaskResult result);

        void onTestFinished();

        void onDrawDataRefresh(DrawData data);
    }
}
