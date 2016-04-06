package com.ylj.daemon.manager;

import android.util.Pair;

import com.ylj.common.bean.Task;
import com.ylj.common.bean.Test;
import com.ylj.common.config.Global;
import com.ylj.common.utils.DataConvertor;
import com.ylj.common.utils.RoadUtils;
import com.ylj.common.utils.TaskDbFileUitl;
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
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/3/17 0017.
 */
public class TaskStateManager implements ITaskStateManager {

    public static final int TRACE_DRAW_STEP_NUM = 4;

    RecordManager mRecordManager;
    IColorCalculator mColorCalculator;
    IColorConvertor mColorConvertor;
    ILevelConvertor mLevelConvertor;

    Task mTask = new Task();
    Record mRecord = new Record();

    boolean mHasAddData = false;

    public TaskStateManager() {
        mColorCalculator = new ColorCalculatorImpl();
    }

    @Override
    public void loadTask(Task task) {
        mOnTaskHandleListener.onLoadTaskStart();

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
        mHasAddData = false;
        mOnTaskHandleListener.onLoadTaskStart();

        String fileName = TaskDbFileUitl.getTaskDbFileName(task);
        mTask.setRecordFile(fileName);
        DbLet.saveOrUpdateTask(mTask);
        mRecordManager = DbLet.getRecordManager(Global.getRecordStorgeDir(),fileName);

        mLevelConvertor = new LevelConvertorImpl(mTask.getVCV());
        mColorConvertor = new ColorConvertorImpl(mLevelConvertor);

        mColorCalculator.setRoad(mTask.getRoadWidth(), mTask.getRoadLength());
        Pair<Integer, Integer> pair = RoadUtils.getRoadGrid(mTask.getRoadWidth(), mTask.getRoadLength(), mTask.getRollerWidth());
        mColorCalculator.setGrid(pair.first, pair.second);
        mColorCalculator.setColorConvertor(mColorConvertor);
        mColorCalculator.setLeverConvertor(mLevelConvertor);
    }

    private void loadTaskData() {
        ArrayList<TraceData> traceDatas = mRecordManager.getTraceDataList();
        ArrayList<ColorData> colorDatas = mRecordManager.getColorDataList();
        TaskResult taskResult = DbLet.getTaskResult(mTask);

        mRecord.setDistance(0);
        mRecord.setPositionY(0);
        mRecord.setPositionY(0);
        if (mTask.isTest()) {
            List<Test> testList = DbLet.getAllTestByTask(mTask);
            if (testList != null) {
                Test test = testList.get(testList.size() - 1);
                mRecord.setDistance(test.getDistance());
                mRecord.setPositionX(test.getLastPositionX());
                mRecord.setPositionY(test.getLastPositionY());
            }
        }
        mOnTaskHandleListener.onLoadTaskFinish(traceDatas, colorDatas, taskResult);

        mColorCalculator.addColorDatas(colorDatas);
    }

    @Override
    public void finishTask(final Test test) {
        if (mTask == null)
            return;
        if (test == null && !mTask.isTest())
            return;
        x.task().run(new Runnable() {
            @Override
            public void run() {
                TaskResult result = calculateResultAndSave();
                saveTask(true);
                finishTestAndSave(test);
                saveColorDatas();
                if(test != null) {
                    mOnTaskHandleListener.onTestFinished();
                }
                mOnTaskHandleListener.onTaskResultCreated(result);
            }
        });
    }


    @Override
    public void startTest() {
    }

    @Override
    public void pauseTest() {
    }

    private void saveTask(boolean isFinish) {
        mTask.setIsTest(true);
        if (isFinish) {
            mTask.setIsFinish(true);
            mRecordManager.saveOrUpdateTask(mTask);
        }
        DbLet.saveOrUpdateTask(mTask);
    }

    private TaskResult calculateResultAndSave() {
        TaskResult result = mColorCalculator.resultCalculate();
        result.setTaskId(mTask.getId());
        DbLet.saveTaskResult(result);
        mRecordManager.saveTaskResult(result);
        return result;
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
        if (traceData != null) {
            mRecordManager.saveTraceData(traceData);
        }

        ColorData colorData = mColorCalculator.addData(
                mRecord.getPositionX(),
                mRecord.getPositionY(),
                mRecord.getQuake());
        drawData.setColorData(colorData);

        mHasAddData = true;
        mOnTaskHandleListener.onDrawDataRefresh(drawData);
    }

    int mTraceStep = TRACE_DRAW_STEP_NUM;
    double mTempSum = 0;

    private TraceData createTraceData() {
        mTraceStep--;
        mTempSum += mRecord.getQuake();
        if (mTraceStep >= 0)
            return null;

        TraceData traceData = new TraceData();
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
    public void finishTest(final Test test) {
        if (mTask == null)
            return;
        if (!mHasAddData)
            return;
        x.task().run(new Runnable() {
            @Override
            public void run() {
                saveTask(false);
                finishTestAndSave(test);
                saveColorDatas();
                mOnTaskHandleListener.onTestFinished();
            }
        });
    }

    private void saveColorDatas() {
        for (int i = 0; i < mColorCalculator.getRow(); i++) {
            for (int j = 0; j < mColorCalculator.getColumn(); j++) {
                mRecordManager.saveOrUpdateColorData(mColorCalculator.getColorData(i, j));
            }
        }
    }

    private void finishTestAndSave(Test test) {
        if (test == null)
            return;
        if (test.getStartTime() == null)
            return;
        test.setTaskId(mTask.getId());
        test.setEndTime(new Date());
        test.countTotalTime();
        test.setLastPositionX(mRecord.getPositionX());
        test.setLastPositionY(mRecord.getPositionY());
        test.setDistance(mRecord.getDistance());
        DbLet.saveOrUpdateTest(test);

        mRecordManager.saveOrUpadteTest(test);
    }


    OnTaskHandleListener mOnTaskHandleListener;

    @Override
    public void setOnTaskHandleListener(OnTaskHandleListener listener) {
        mOnTaskHandleListener = listener;
    }
}
