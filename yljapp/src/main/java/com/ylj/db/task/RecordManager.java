package com.ylj.db.task;

import com.ylj.daemon.bean.Record;
import com.ylj.daemon.bean.TaskResult;
import com.ylj.db.AbstractDbManager;
import com.ylj.task.bean.ColorData;
import com.ylj.task.bean.TraceData;

import junit.framework.TestResult;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/6 0006.
 */
public class RecordManager extends AbstractDbManager {
    public RecordManager(String dbName) {
        super(dbName);
    }

    public ArrayList<TraceData> getTraceDataList() {
        ArrayList<TraceData> traceDatas = new ArrayList<>();
        try {
            traceDatas.addAll(getDb().findAll(TraceData.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return traceDatas;
    }

    public ArrayList<ColorData> getColorDataList() {
        ArrayList<ColorData> colorDatas = new ArrayList<>();
        try {
            colorDatas.addAll(getDb().findAll(ColorData.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return colorDatas;
    }

    public TaskResult getTaskResult() {
        TaskResult taskResult = null;
        try {
            taskResult = getDb().findFirst(TaskResult.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return taskResult;
    }

    public void saveTraceData(TraceData traceData) {

    }

    public void saveRecord(Record mRecord) {

    }
}
