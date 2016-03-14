package com.ylj.task;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.ylj.daemon.YljService;
import com.ylj.task.bean.DeviceData;
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
public class TestActivity extends BaseActivity implements TraceFragment.OnTraceDataLoadListener,
        ColorRunFragment.OnColorDataLoadListener, ITestCtrl.OnCtrlLister,IConnectCtrl.OnConnectListener{

    public static final String EXTRA_MODE = "EXTRA_MODE";
    public static final String EXTRA_TASK = "EXTRA_TASK";

    public static final int MODE_SHOW_RESULT = 0;
    public static final int MODE_TASK_TEST = 1;

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

    TestControler mTestControler;

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
        mTestControler.startTest();
        mRunButton.setEnabled(false);
    }

    @Event(R.id.fab_stop)
    private void onStopClick(View view) {
        mTestControler.stopTest();
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
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mCurrentFragmentIndex = position;
                        getTestFragment(position).refreshPage();
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        initTest();
        initFragments();
    }

    private void initControler() {
        mTestControler = TestControler.newInstance(this, YljService.class);
        mTestControler.addConnectListener(this);
    }

    private void initFragments() {
        mFragments.clear();
        mFragments.add(new TraceFragment());
        mFragments.add(new ColorRunFragment());
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert("Info", "Do you want to leave test?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mTestControler.deleteTestCtrlListener(TestActivity.this);
                        mTestControler.deleteConnectListener(TestActivity.this);
                        mTestControler.release();
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

    @Override
    public void onColorDataLoadFinish() {
        mIsColorLoadFinish= true;
        if(mCurrentFragmentIndex == FRAGMENT_INDEX_COLOR){
            getColorFragment().showTestPage();
        }
    }

    @Override
    public void onTraceDataLoadFinish() {
        mIsTraceLoadFinish= true;
        if(mCurrentFragmentIndex == FRAGMENT_INDEX_TRACE){
            getTraceFragment().showTestPage();
        }
    }

    private AbstractTestFragment getColorFragment(){
        return mFragments.get(FRAGMENT_INDEX_COLOR);
    }

    private AbstractTestFragment getTraceFragment(){
        return mFragments.get(FRAGMENT_INDEX_TRACE);
    }

    private AbstractTestFragment getTestFragment(int index){
        return mFragments.get(index);
    }

    @Override
    public void onConnected(DeviceInfo info) {
        AppStatus.instance().setCurrentDevice(info);
        showToast("device has connected");
    }

    @Override
    public void onConnectError(int error) {

    }

    @Override
    public void onTestStart() {
        showToast("test start");
        mTestControler.addTestCtrlListener(this);
    }

    @Override
    public void onTestStop() {
        showToast("test pause");
        mTestControler.deleteTestCtrlListener(this);
    }

    @Override
    public void onTestRefresh(DeviceData data) {

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
                    return "TRACE";
                case 1:
                    return "COLOR PLOT";
                case 2:
                    return "QUAKE";
                case 3:
                    return "TEMP";
            }
            return null;
        }
    }

}
