package com.ylj.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ylj.R;
import com.ylj.adjust.AdjustActivity;
import com.ylj.common.BaseActivity;
import com.ylj.common.bean.Admin;
import com.ylj.common.bean.Staff;
import com.ylj.common.bean.Task;
import com.ylj.common.bean.Test;
import com.ylj.common.config.ConfigLet;
import com.ylj.common.config.Global;
import com.ylj.common.config.StatusLet;
import com.ylj.common.utils.BeanUtils;
import com.ylj.common.utils.TaskDbFileUitl;
import com.ylj.db.DbLet;
import com.ylj.task.ftp.AbstractFtpActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_task)
public class TaskActivity extends AbstractFtpActivity {

    public static final String EXTRA_TASK = "EXTRA_TASK";

    public static final String TAG_TEST_NAME = "TAG_TEST_NAME";
    public static final String TAG_USER = "TAG_USER";

    public static final int ACTIVITY_REQUEST_TASK = 1;

    Task mTask = new Task();
    boolean mIsAdjust = false;
    boolean mIsFinish = false;
    boolean mIsTest = false;

    public static void startNewActivity(Context context, Task task) {
        Intent intent = new Intent(context, TaskActivity.class);
        intent.putExtra(EXTRA_TASK, task);
        context.startActivity(intent);
    }

    @ViewInject(R.id.tv_task_name)
    TextView mTaskNameText;

    @ViewInject(R.id.tv_road_name)
    TextView mRoadNameText;

    @ViewInject(R.id.tv_road_width)
    TextView mRoadWidthText;

    @ViewInject(R.id.tv_road_length)
    TextView mRoadLengthText;

    @ViewInject(R.id.tv_status)
    TextView mStatusText;

    @ViewInject(R.id.lv_test)
    ListView mTestListView;

    @ViewInject(R.id.layout_task_edit)
    RelativeLayout mEditLayout;

    @ViewInject(R.id.layout_reslut)
    RelativeLayout mResultLayout;

    @ViewInject(R.id.layout_adjust)
    LinearLayout mAdjustLayout;

    @ViewInject(R.id.layout_test)
    LinearLayout mTestLayout;

    @ViewInject(R.id.layout_no_test)
    LinearLayout mNoTestLayout;

    @ViewInject(R.id.layout_enter_test)
    RelativeLayout mEnterTestLayout;

    @Event(R.id.btn_task_edit)
    private void onTaskEditClick(View view) {
        TaskModifyActivity.startAsModifyTaskActivityForResult(this, mTask, ACTIVITY_REQUEST_TASK);
    }

    @Event(R.id.btn_upload)
    private void onUploadClick(View view) {
        String address = ConfigLet.getFtpIp();
        int port = ConfigLet.getFtpPort();
        String user = ConfigLet.getFtpUser();
        String passwd = ConfigLet.getFtpPasswd();
        String file = Global.getRecordStorgeDir() + TaskDbFileUitl.getTaskDbFileName(mTask);
        uploadFile(address, port, user, passwd, file);
    }

    @Event(R.id.rl_task_info)
    private void onTaskInfoLayoutClick(View view) {
        if (mIsFinish) {
            TaskModifyActivity.startAsShowOnlyTaskActivity(this, mTask);
        } else {
            TaskModifyActivity.startAsShowTaskActivityForResult(this, mTask, ACTIVITY_REQUEST_TASK);
        }
    }

    @Event(R.id.btn_enter_result)
    private void onEnterResultButtonClick(View view) {
        TestActivity.startAsShowResultActivity(this, mTask);
    }

    @Event(R.id.btn_enter_adjust)
    private void onEnterAdjustButtonClick(View view) {
        if (!StatusLet.isConnect()) {
            showToast("no connect");
            return;
        }
        AdjustActivity.startNewActivity(this, mTask);
        finish();
    }

    @Event(R.id.btn_enter_test)
    private void onEnterTestButtonClick(View view) {
        if (!StatusLet.isConnect()) {
            showToast("no connect");
            return;
        }
        TestActivity.startAsTaskTestActivity(this, mTask);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case ACTIVITY_REQUEST_TASK:
                initExtraData(data);
                initLayout();
                initInfoView();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initExtraData(getIntent());
        initToolbar();
        initLayout();
        initInfoView();
        initTestListView();
        refreshTestData();
    }

    SimpleAdapter mTestAdapter;
    List<Map<String, Object>> mTestMaps = new ArrayList<>();

    private void refreshTestData() {
        if (!mIsAdjust)
            return;
        if (!mIsTest)
            return;
        x.task().run(new Runnable() {
            @Override
            public void run() {
                mTestMaps.clear();
                List<Test> list = DbLet.getAllTestByTask(mTask);
                Map<String, Object> map;
                int n = 1;
                if (list == null) {
                    return;
                }
                for (Test test : list) {
                    map = test.convertToMap();
                    map.put(TAG_TEST_NAME, "Test" + n);
                    map.put(TAG_USER, getUserNameByTest(test));
                    mTestMaps.add(map);
                    n++;
                }
                x.task().post(new Runnable() {
                    @Override
                    public void run() {
                        mTestAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private String getUserNameByTest(Test test) {
        String name = getString(R.string.task_anonymous);
        if (!test.isLogin()) {
            return name;
        }

        if (test.isAdmin()) {
            Admin admin = DbLet.getAdminById(test.getAdminId());
            name = admin.getAdminName();
        } else {
            Staff staff = DbLet.getStaffById(test.getStaffId());
            name = staff.getStaffName();
        }
        return name;
    }

    private void initTestListView() {
        if (!mIsAdjust)
            return;
        if (!mIsTest)
            return;
        mTestAdapter = new SimpleAdapter(this, mTestMaps,
                R.layout.listview_task_test_list,
                new String[]{TAG_TEST_NAME,
                        TAG_USER,
                        Test.TAG_START_DATE,
                        Test.TAG_START_TIME,
                        Test.TAG_DISTANCE,
                        Test.TAG_TOTAL_TIME_TEXT},
                new int[]{R.id.tv_test_name,
                        R.id.tv_user,
                        R.id.tv_start_date,
                        R.id.tv_start_time,
                        R.id.tv_distance,
                        R.id.tv_total_time});
        mTestListView.setAdapter(mTestAdapter);
        mTestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //onTestItemClick(position);
            }
        });
    }

    private void onTestItemClick(int position) {
        Test test = getTestByPosition(position);
        TestInfoActivity.startNewActivity(this, test);
    }

    private Test getTestByPosition(int position) {
        Map<String, Object> map = mTestMaps.get(position);
        Test test = Test.createByMap(map);
        return test;
    }

    private void initInfoView() {
        mTaskNameText.setText(mTask.getTaskName());
        mRoadNameText.setText(mTask.getRoadName());
        mRoadLengthText.setText(String.format("%.2f", mTask.getRoadLength()));
        mRoadWidthText.setText(String.format("%.2f", mTask.getRoadWidth()));
        if (mIsAdjust) {
            if (mIsFinish) {
                mStatusText.setText(R.string.task_finish);
            } else {
                mStatusText.setText(R.string.task_not_finish);
            }
        } else {
            mStatusText.setText(R.string.task_no_adjust);
        }
    }

    private void initLayout() {
        if (!mIsAdjust) {
            mAdjustLayout.setVisibility(View.VISIBLE);
            mNoTestLayout.setVisibility(View.GONE);
            mEnterTestLayout.setVisibility(View.GONE);
            mTestLayout.setVisibility(View.GONE);
            mResultLayout.setVisibility(View.GONE);
            return;
        }

        if (!mIsTest) {
            mAdjustLayout.setVisibility(View.GONE);
            mNoTestLayout.setVisibility(View.VISIBLE);
            mEnterTestLayout.setVisibility(View.VISIBLE);
            mTestLayout.setVisibility(View.GONE);
            mResultLayout.setVisibility(View.GONE);
            return;
        }

        mNoTestLayout.setVisibility(View.GONE);
        mAdjustLayout.setVisibility(View.GONE);
        mTestLayout.setVisibility(View.VISIBLE);
        if (mIsFinish) {
            mEnterTestLayout.setVisibility(View.GONE);
            mResultLayout.setVisibility(View.VISIBLE);
            mEditLayout.setVisibility(View.GONE);
        } else {
            mEnterTestLayout.setVisibility(View.VISIBLE);
            mResultLayout.setVisibility(View.GONE);
            mEditLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskActivity.this.finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initExtraData(Intent intent) {
        mTask = intent.getParcelableExtra(EXTRA_TASK);
        mIsAdjust = mTask.isAdjust();
        mIsFinish = mTask.isFinish();
        mIsTest = mTask.isTest();
    }

}
