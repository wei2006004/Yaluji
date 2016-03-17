package com.ylj.daemon.manager;

import android.util.Pair;

import com.ylj.common.bean.Task;
import com.ylj.common.bean.Test;
import com.ylj.common.utils.DataConvertor;
import com.ylj.common.utils.RoadUtils;
import com.ylj.daemon.bean.DeviceData;
import com.ylj.daemon.bean.Record;
import com.ylj.daemon.bean.TaskResult;
import com.ylj.db.DbLet;
import com.ylj.db.task.RecordManager;
import com.ylj.task.bean.ColorData;
import com.ylj.task.bean.DrawData;
import com.ylj.task.bean.TraceData;

import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/17 0017.
 */
public class TaskStateManager implements ITaskStateManager {

    public static final int TRACE_DRAW_STEP_NUM = 4;

    RecordManager mRecordManager;
    IColorCalculator mColorCalculator;
    IColorConvertor mColorConvertor;

    Task mTask = new Task();
    Record mRecord = new Record();


    public TaskStateManager() {
        mColorCalculator = new ColorCalculatorImpl();
    }

    @Override
    public void loadTask(Task task) {
        initData(task);

        if (!task.isTest()) {
            mOnTaskHandleListener.onLoadTaskFinish(null, null, null);
            return;
        }
        x.task().post(new Runnable() {
            @Override
            public void run() {
                loadTaskData();
            }
        });
    }

    private void initData(Task task) {
        mTask = task;
        mOnTaskHandleListener.onLoadTaskStart();
        mRecordManager = DbLet.getRecordManager(task);

        mColorConvertor = new ColorConvertorImpl(mTask.getVCV());

        mColorCalculator.setRoad(mTask.getRoadWidth(), mTask.getRoadLength());
        Pair<Integer, Integer> pair = RoadUtils.getRoadGrid(mTask.getRoadWidth(), mTask.getRoadLength(), mTask.getRollerWidth());
        mColorCalculator.setGrid(pair.first, pair.second);
        mColorCalculator.setColorConvertor(mColorConvertor);
    }

    private void loadTaskData() {
        ArrayList<TraceData> traceDatas = mRecordManager.getTraceDataList();
        ArrayList<ColorData> colorDatas = mRecordManager.getColorDataList();
        TaskResult taskResult = mRecordManager.getTaskResult();

        mOnTaskHandleListener.onLoadTaskFinish(traceDatas, colorDatas, taskResult);

        mColorCalculator.addColorDatas(colorDatas);
    }

    @Override
    public void finishTask() {
        if (mTask == null)
            return;

    }

    @Override
    public void addTestData(DeviceData data) {
        if (mTask == null)
            return;
        DrawData drawData = new DrawData();
        mRecord = convertToRecord(data);
        drawData.setRecord(mRecord);
        mRecordManager.saveRecord(mRecord);

        TraceData traceData = createTraceData();
        drawData.setTraceData(traceData);
        if(traceData!=null){
            mRecordManager.saveTraceData(traceData);
        }

        ColorData colorData = mColorCalculator.addData(
                mRecord.getPositionX(),
                mRecord.getPositionY(),
                mRecord.getQuake());
        drawData.setColorData(colorData);

        mOnTaskHandleListener.onDrawDataRefresh(drawData);
    }

    int mTraceStep = TRACE_DRAW_STEP_NUM;
    double mTempSum = 0;

    private TraceData createTraceData() {
        mTraceStep--;
        if (mTraceStep >= 0)
            return null;

        TraceData traceData = new TraceData();
        mTempSum += mRecord.getQuake();
        traceData.setPostionX(mRecord.getPositionX());
        traceData.setPostionY(mRecord.getPositionY());
        traceData.setDirection(mRecord.getDirection());
        int color = mColorConvertor.convertToColor(mTempSum / TRACE_DRAW_STEP_NUM);
        traceData.setColor(color);
        mTraceStep = TRACE_DRAW_STEP_NUM;
        mTempSum = 0;
        return traceData;
    }

    private Record convertToRecord(DeviceData data) {
        return DataConvertor.covertDeviceData2Record(
                mRecord.getPositionX(),
                mRecord.getPositionY(),
                mTask.getStep(), data);
    }

    @Override
    public void finishTest(Test test) {
        if (mTask == null)
            return;
    }

    OnTaskHandleListener mOnTaskHandleListener;

    @Override
    public void setOnTaskHandleListener(OnTaskHandleListener listener) {
        mOnTaskHandleListener = listener;
    }
}
