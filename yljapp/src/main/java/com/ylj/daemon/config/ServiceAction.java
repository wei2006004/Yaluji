package com.ylj.daemon.config;

/**
 * Created by Administrator on 2016/3/17 0017.
 */
public class ServiceAction {

    //连接状态改变 send：state
    public final static String ACTION_CONNECT_STATE_CHANGE = "ACTION_CONNECT_STATE_CHANGE";

    //断开连接 send：none
    public final static String ACTION_DISCONNECTED = "ACTION_DISCONNECTED";

    //获取硬件信息 send：deviceinfo
    public final static String ACTION_DEVICE_INFO = "ACTION_DEVICE_INFO";

    //校验数据 send：devicedata
    public final static String ACTION_ADJUST_DATA = "ACTION_ADJUST_DATA";

    //获取绘图数据 send：drawdata
    public final static String ACTION_DRAW_DATA = "ACTION_DRAW_DATA";

    //采集控制状态改变 send：ctrl_flag
    public final static String ACTION_SAMPLE_CTRL_STATE_CHANGE = "ACTION_SAMPLE_CTRL_STATE_CHANGE";

    //载入task数据开始 send：none
    public final static String ACTION_START_LOAD_TASK = "ACTION_START_LOAD_TASK";

    //载入task数据结束 send：tracedatas,colordatas,taskresult
    public final static String ACTION_LOAD_TASK_FINISH = "ACTION_LOAD_TASK_FINISH";

    //test结束 send：none
    public final static String ACTION_TEST_FINISHED = "ACTION_TEST_FINISHED";

    //生成task结果完成 send：taskresult
    public final static String ACTION_TASK_RESULT_CREATED = "ACTION_TASK_RESULT_CREATED";

    //FTP登录状态改变 send:state
    public final static String ACTION_FTP_STATE_CREATED = "ACTION_FTP_STATE_CREATED";

    //FTP上传数据开始 send：none
    public final static String ACTION_FTP_UPLOAD_START = "ACTION_FTP_UPLOAD_START";

    //FTP上传数据结束 send：none
    public final static String ACTION_FTP_UPLOAD_FINISH = "ACTION_FTP_UPLOAD_FINISH";

    //FTP上传数据取消 send：none
    public final static String ACTION_FTP_UPLOAD_CANCEL = "ACTION_FTP_UPLOAD_CANCEL";

    //FTP上传数据出错 send：none
    public final static String ACTION_FTP_UPLOAD_ERROR = "ACTION_FTP_UPLOAD_ERROR";

    //FTP上传数据进度 send：progress
    public final static String ACTION_FTP_UPLOAD_PROGRESS = "ACTION_FTP_UPLOAD_PROGRESS";

    public final static String EXTRA_ACTION_FLAG = "EXTRA_ACTION_FLAG";

    public final static String EXTRA_PROGRESS = "EXTRA_PROGRESS";

    public final static String EXTRA_ADJUST_DATA = "EXTRA_ADJUST_DATA";
    public final static String EXTRA_DEVICE_INFO = "EXTRA_DEVICE_INFO";
    public final static String EXTRA_DRAW_DATA = "EXTRA_DRAW_DATA";

    public final static String EXTRA_TRACE_DATA_LIST = "EXTRA_TRACE_DATA_LIST";
    public final static String EXTRA_COLOR_DATA_LIST = "EXTRA_COLOR_DATA_LIST";
    public final static String EXTRA_TASK_RESULT = "EXTRA_TASK_RESULT";

    public final static int CTRL_FLAG_STOP = 0;
    public final static int CTRL_FLAG_START = 1;

}
