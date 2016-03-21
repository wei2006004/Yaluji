package com.ylj.db.task;

import com.ylj.common.bean.Task;
import com.ylj.common.bean.Test;
import com.ylj.db.AbstractDbManager;
import com.ylj.db.config.Constant;

import org.xutils.ex.DbException;

import java.util.List;

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

    public void delete(Task task) {
        try {
            db.delete(task);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public List<Task> getNotFinishTaskList() {
        List<Task> list = null;
        try {
            list = db.selector(Task.class)
                    .where(Task.TAG_IS_FINISH, Constant.SQL_OP_EQUAL,Constant.SQL_FALSE)
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Task> getFinishTaskList() {
        List<Task> list = null;
        try {
            list = db.selector(Task.class)
                    .where(Task.TAG_IS_FINISH, Constant.SQL_OP_EQUAL,Constant.SQL_TRUE)
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void saveOrUpadate(Test test) {
        try {
            db.saveOrUpdate(test);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public List<Test> getAllTestByTask(Task task) {
        List<Test> list = null;
        try {
            list = db.findAll(Test.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }
}
