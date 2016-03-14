package com.ylj.common.utils;

import com.ylj.common.bean.Task;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public class TaskDbFileUitl {

    public final static String FILENAME_SEPARATOR = "_";
    public final static String FILENAME_DB_POSTFIX = ".db";

    public static String getTaskDbFileName(Task task) {
        return task.getTaskName() + FILENAME_SEPARATOR + task.getRoadName() + FILENAME_SEPARATOR + task.getId() + FILENAME_DB_POSTFIX;
    }
}
