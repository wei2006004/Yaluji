package com.ylj.task;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import com.ylj.R;
import com.ylj.common.BaseActivity;
import com.ylj.common.bean.Task;
import com.ylj.common.bean.Test;
import com.ylj.common.config.AppStatus;
import com.ylj.connect.IConnectCtrl;
import com.ylj.connect.bean.DeviceInfo;
import com.ylj.daemon.bean.DeviceData;
import com.ylj.daemon.bean.TaskResult;
import com.ylj.db.DbLet;
import com.ylj.task.fragment.AbstractTestFragment;
import com.ylj.task.fragment.ColorRunFragment;
import com.ylj.task.fragment.PlotFragment;
import com.ylj.task.fragment.TraceFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_test)
public class TestActivity extends AbstractTestActivity implements ITestCtrl.OnTestCtrlListener,
        IConnectCtrl.OnConnectListener ,AbstractTestFragment.OnDataLoadListener{

    public static final String EXTRA_MODE = "EXTRA_MODE";
    public static final String EXTRA_TASK = "EXTRA_TASK";

    public static final int MODE_SHOW_RESULT = 0;
    public static final int MODE_TASK_TEST = 1;

    public static final int FRAGMENT_FLAG_TRACE = 0;
    public static final int FRAGMENT_FLAG_COLOR = 1;

    public static final int FRAGMENT_INDEX_TRACE = 0;
    public static final int FRAGMENT_INDEX_COLOR = 1;
    public static final int FRAGMENT_INDEX_QUAKE = 2;
    public static final int FRAGMENT_INDEX_TEMP = 3;

    public static final int TEST_STATUS_STOP = 0;
    public static final int TEST_STATUS_RUN = 1;

    private int mStatus = TEST_STATUS_STOP;

    private int mCurrentFragmentIndex = FRAGMENT_INDEX_TRACE;

    private int mMode = MODE_SHOW_RESULT;
    private Task mTask = new Task();
    private Test mTest = new Test();

    boolean mIsTest = false;

    public static void startAsTaskTestActivity(Context context, Task task) {
        Intent intent = new Intent(context, TestActivity.class);
        intent.putExtra(EXTRA_MODE, MODE_TASK_TEST);
        intent.putExtra(EXTRA_TASK, task);
        context.startActivity(intent);
    }

    public static void startAsShowResultActivity(Context context, Task task) {
        Intent intent = new Intent(context, TestActivity.class);
        intent.putExtra(EXTRA_MODE, MODE_SHOW_RESULT);
        intent.putExtra(EXTRA_TASK, task);
        context.startActivity(intent);
    }

    List<AbstractTestFragment> mFragments = new ArrayList<>();

    SectionsPagerAdapter mSectionsPagerAdapter;

    boolean mIsTraceLoadFinish = false;
    boolean mIsColorLoadFinish = false;

    @ViewInject(R.id.container)
    ViewPager mViewPager;

    @ViewInject(R.id.layout_bottom)
    RelativeLayout mButtomLayout;

    @ViewInject(R.id.fab_run)
    FloatingActionButton mRunButton;

    @ViewInject(R.id.fab_stop)
    FloatingActionButton mStopButton;

    @Event(R.id.fab_run)
    private void onRunClick(View view) {
        getTestCtrl().startTest();
        mRunButton.setEnabled(false);
    }

    @Event(R.id.fab_stop)
    private void onStopClick(View view) {
        getTestCtrl().pauseTest();
        mStopButton.setEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initExtraData();
        initData();
        initControler();
        initToolbar();
        initDrawerLayout();
        initViewPager();
        initTabs();
        setLayoutVisiable();
        loadDatas();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mIsTest){
            mTask.setIsTest(true);
            DbLet.saveOrUpdateTask(mTask);
            getTestCtrl().finishTest(mTest);
        }
    }

    private void loadDatas() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getTestCtrl().loadTask(mTask);
            }
        });
    }

    private void setLayoutVisiable() {
        if (mMode == MODE_SHOW_RESULT) {
            mButtomLayout.setVisibility(View.GONE);
        }
    }

    private void initTabs() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void initViewPager() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {
                onFragmentSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void onFragmentSelected(final int position) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mCurrentFragmentIndex = position;
                switch (mCurrentFragmentIndex) {
                    case FRAGMENT_INDEX_QUAKE:
                    case FRAGMENT_INDEX_TEMP:
                        if (getCurrentFragment().isWaitPage()) {
                            getCurrentFragment().showTestPage();
                        }
                        getTestFragment(position).refreshPage();
                        break;
                    case FRAGMENT_INDEX_TRACE:
                        if (mIsTraceLoadFinish) {
                            getTraceFragment().showTestPage();
                            getTraceFragment().refreshPage();
                        }
                        break;
                    case FRAGMENT_INDEX_COLOR:
                        if (mIsColorLoadFinish) {
                            getColorFragment().showTestPage();
                            getColorFragment().refreshPage();
                        }
                        break;
                }
            }
        });
    }

    private void initData() {
        initTest();
        initFragments();
    }

    private void initControler() {
        getConnectCtrl().addConnectListener(this);
        getTestCtrl().addTestCtrlListener(this);
    }

    private void initFragments() {
        mFragments.clear();
        mFragments.add(TraceFragment.newInstance(mTask));
        mFragments.add(ColorRunFragment.newInstance(mTask, mMode));
        if (mMode == MODE_TASK_TEST) {
            mFragments.add(PlotFragment.newQuakePlotFragment());
            mFragments.add(PlotFragment.newTempPlotFragment());
        }
    }

    private void initTest() {
        mTest = new Test();
        AppStatus appStatus = AppStatus.instance();
        mTest.setTaskId(getTaskId());
        mTest.setIsLogin(appStatus.isLogin());
        mTest.setIsAdmin(appStatus.isAdmin());
        mTest.setStaffId(appStatus.getCurrentStaff().getId());
        mTest.setAdminId(appStatus.getCurrentAdmin().getId());
    }

    private void initDrawerLayout() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = LayoutInflater.from(this).inflate(R.layout.nav_header_test, null);

        navigationView.addHeaderView(view);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMode == MODE_SHOW_RESULT)
                    return;
                showAlert("Info",  getString(R.string.alert_leave_test), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getTestCtrl().deleteTestCtrlListener(TestActivity.this);
                        getConnectCtrl().deleteConnectListener(TestActivity.this);
                        TestActivity.this.finish();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initExtraData() {
        Intent intent = getIntent();
        mMode = intent.getIntExtra(EXTRA_MODE, MODE_SHOW_RESULT);
        mTask = intent.getParcelableExtra(EXTRA_TASK);
    }

    private AbstractTestFragment getColorFragment(){
        return mFragments.get(FRAGMENT_INDEX_COLOR);
    }

    private AbstractTestFragment getTraceFragment(){
        return mFragments.get(FRAGMENT_INDEX_TRACE);
    }

    private AbstractTestFragment getCurrentFragment(){
        return mFragments.get(mCurrentFragmentIndex);
    }

    private AbstractTestFragment getTestFragment(int index){
        return mFragments.get(index);
    }

    private boolean isTestRun(){
        return mStatus == TEST_STATUS_RUN;
    }

    @Override
    public void onConnected(DeviceInfo info) {
        showToast("connected");
        setAppConnectStatus(true);
        AppStatus.instance().setCurrentDevice(info);
    }

    @Override
    public void onDisconnected() {
        showToast("disconnected");
        setAppConnectStatus(false);
    }

    @Override
    public void onConnectFail(int error) {
        showToast("connect fail");
        setAppConnectStatus(false);
    }

    @Override
    public void onConnectLost() {
        showToast("connect lost");
        setAppConnectStatus(false);
    }

    private void setAppConnectStatus(boolean isConnect){
        AppStatus appstatus = AppStatus.instance();
        appstatus.setIsConnect(isConnect);
    }

    @Override
    public void onTestStart() {
        showToast("test start");
        mIsTest = true;
    }

    @Override
    public void onTestPasue() {

    }

    @Override
    public void onTestFinish() {

    }

    @Override
    public void onLoadTaskStart() {

    }

    @Override
    public void onLoadTaskFinish() {

    }

    @Override
    public void onTaskResultCreated(TaskResult result) {

    }

    @Override
    public void onDataLoadFinish(int flag) {
        if(flag == FRAGMENT_FLAG_TRACE){
            mIsTraceLoadFinish= true;
        }else if(flag == FRAGMENT_FLAG_COLOR){
            mIsColorLoadFinish= true;
        }
        if(mCurrentFragmentIndex == flag){
            getCurrentFragment().showTestPage();
            getCurrentFragment().refreshPage();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.test_trace);
                case 1:
                    return getString(R.string.test_color);
                case 2:
                    return getString(R.string.test_quake);
                case 3:
                    return getString(R.string.test_temp);
            }
            return null;
        }
    }

}
