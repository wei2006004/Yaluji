package com.ylj.db;

import com.ylj.common.bean.Admin;
import com.ylj.common.bean.Staff;
import com.ylj.common.bean.Task;
import com.ylj.common.bean.Test;
import com.ylj.common.utils.TaskDbFileUitl;
import com.ylj.daemon.bean.TaskResult;
import com.ylj.db.account.AccountManager;
import com.ylj.db.task.RecordManager;
import com.ylj.db.task.TaskManager;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/7 0007.
 */
public class DbLet {
    public static void addStaff(Staff staff) {
        AccountManager accountManager = ManagerFactory.getAccountManager();
        accountManager.addStaff(staff);
    }

    public static void addAdmin(Admin admin) {
        AccountManager accountManager = ManagerFactory.getAccountManager();
        accountManager.addAdmin(admin);
    }

    public static List<Admin> getAdminList() {
        AccountManager accountManager = ManagerFactory.getAccountManager();
        return accountManager.getAllAdminFromDb();
    }

    public static List<Staff> getStaffList() {
        AccountManager accountManager = ManagerFactory.getAccountManager();
        return accountManager.getAllStaffFromDb();
    }

    public static Admin getAdminById(int adminId) {
        AccountManager accountManager = ManagerFactory.getAccountManager();
        return accountManager.getAdminById(adminId);
    }

    public static Staff getStaffById(int staffId) {
        AccountManager accountManager = ManagerFactory.getAccountManager();
        return accountManager.getStaffById(staffId);
    }

    public static void deleteAdmin(Admin admin) {
        AccountManager accountManager = ManagerFactory.getAccountManager();
        accountManager.deleteAdmin(admin);
    }

    public static void deleteStaff(Staff staff) {
        AccountManager accountManager = ManagerFactory.getAccountManager();
        accountManager.deleteStaff(staff);
    }

    public static void updateStaff(Staff staff) {
        AccountManager accountManager = ManagerFactory.getAccountManager();
        accountManager.updateStaff(staff);
    }

    public static void updateAdmin(Admin admin) {
        AccountManager accountManager = ManagerFactory.getAccountManager();
        accountManager.updateAdmin(admin);
    }

    public static void saveOrUpdateAdmin(Admin admin) {
        AccountManager accountManager = ManagerFactory.getAccountManager();
        accountManager.saveOrUpdate
                (admin);
    }

    public static void saveOrUpdateTask(Task task) {
        TaskManager taskManager = ManagerFactory.getTaskManager();
        taskManager.saveOrUpadate(task);
    }

    public static void deleteTask(Task task) {
        TaskManager taskManager = ManagerFactory.getTaskManager();
        taskManager.delete(task);
    }

    public static List<Task> getNotFinishTaskList() {
        TaskManager taskManager = ManagerFactory.getTaskManager();
        return taskManager.getNotFinishTaskList();
    }

    public static RecordManager getRecordManager(String dbname) {
        return new RecordManager(dbname);
    }

    public static RecordManager getRecordManager(String dir,String dbname) {
        return new RecordManager(dir,dbname);
    }

    public static List<Test> getAllTestByTask(Task task){
        TaskManager taskManager = ManagerFactory.getTaskManager();
        return taskManager.getAllTestByTask(task);
    }

    public static List<Task> getFinishTaskList() {
        TaskManager taskManager = ManagerFactory.getTaskManager();
        return taskManager.getFinishTaskList();
    }

    public static void saveOrUpdateTest(Test test) {
        TaskManager taskManager = ManagerFactory.getTaskManager();
        taskManager.saveOrUpadate(test);
    }

    public static void saveTaskResult(TaskResult result) {
        TaskManager taskManager = ManagerFactory.getTaskManager();
        taskManager.saveResult(result);
    }

    public static TaskResult getTaskResult(Task task) {
        TaskManager taskManager = ManagerFactory.getTaskManager();
        return taskManager.getTaskResult(task);
    }
}
