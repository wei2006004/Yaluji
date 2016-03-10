package com.ylj.db.task;

import com.ylj.common.bean.Task;
import com.ylj.db.AbstractDbManager;

import org.xutils.ex.DbException;

/**
 * Created by Administrator on 2016/3/6 0006.
 */
public class TaskManager extends AbstractDbManager{

    public TaskManager(String dbName) {
        super(dbName);
    }

    public void saveOrUpadate(Task task) {
        try {
            db.saveOrUpdate(task);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
