package com.ylj.adjust;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import com.ylj.R;
import com.ylj.adjust.bean.AdjustResult;
import com.ylj.adjust.fragment.AdjustFragment;
import com.ylj.common.BaseActivity;
import com.ylj.common.bean.Task;
import com.ylj.common.config.AppStatus;
import com.ylj.connect.ConnectControler;
import com.ylj.connect.bean.DeviceInfo;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_adjust)
public class AdjustActivity extends BaseActivity implements AdjustFragment.OnAdjustFinishListener,
        ConnectControler.OnConnectListener {

    public static final int ADJUST_TIME = 3;

    public static final String EXTRA_TASK = "EXTRA_TASK";
    public static final String EXTRA_LIGHT_ADJUST = "EXTRA_LIGHT_ADJUST";
    public static final String EXTRA_MIDDLE_ADJUST = "EXTRA_MIDDLE_ADJUST";
    public static final String EXTRA_HEAVY_ADJUST = "EXTRA_HEAVY_ADJUST";

    public static void startNewActivity(Context context, Task task) {
        Intent intent = new Intent(context, AdjustActivity.class);
        intent.putExtra(EXTRA_TASK, task);
        context.startActivity(intent);
    }

    public static final int MODE_LIGHT_ADJUST = 0;
    public static final int MODE_MIDDLE_ADJUST = 1;
    public static final int MODE_HEAVY_ADJUST = 2;

    int mMode = MODE_LIGHT_ADJUST;

    Task mTask;

    AdjustControler mAdjustControler;

    SectionsPagerAdapter mSectionsPagerAdapter;

    List<AdjustFragment> mFragments = new ArrayList<>();

    AdjustResult[] mResults = new AdjustResult[ADJUST_TIME];

    @ViewInject(R.id.container)
    ViewPager mViewPager;

    @ViewInject(R.id.fab)
    FloatingActionButton mFabButton;

    @Event(R.id.fab)
    private void onFabClick(View view) {
        AdjustResult result = getAdjustResult(mMode);
        if (result == null) {
            return;
        }
        mResults[mMode] = result;
        switch (mMode) {
            case MODE_LIGHT_ADJUST:
                addAndSwitchNewTab();
                mMode = MODE_MIDDLE_ADJUST;
                break;
            case MODE_MIDDLE_ADJUST:
                addAndSwitchNewTab();
                mMode = MODE_HEAVY_ADJUST;
                break;
            case MODE_HEAVY_ADJUST:
                switchToAdjustResultActivity();
                finish();
                return;
        }
        setFabVisible(false);
    }

    private void addAndSwitchNewTab() {
        mFragments.add(new AdjustFragment());
        mSectionsPagerAdapter.notifyDataSetChanged();
        initTabs();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(mFragments.size() - 1, true);
            }
        });
    }

    private AdjustResult getAdjustResult(int pos) {
        return mFragments.get(pos).requestAdjustResult();
    }

    private void switchToAdjustResultActivity() {
        Intent intent = new Intent(this, AdjustResultActivity.class);
        intent.putExtra(EXTRA_TASK, mTask);
        intent.putExtra(EXTRA_LIGHT_ADJUST, mResults[0]);
        intent.putExtra(EXTRA_MIDDLE_ADJUST, mResults[1]);
        intent.putExtra(EXTRA_HEAVY_ADJUST, mResults[2]);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        initExtraData();
        initToolbar();
        initViewPager();
        initTabs();
        setFabVisible(false);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mAdjustControler.deleteConnectListener(this);
        mAdjustControler.release();
    }

    private void initData() {
        mAdjustControler = AdjustControler.newInstance(this);
        mAdjustControler.addConnectListener(this);

        AdjustFragment fragment = new AdjustFragment();
        fragment.setAdjustCtrl(mAdjustControler);
        mFragments.add(fragment);
    }

    private void setFabVisible(boolean isVisible) {
        if (isVisible) {
            mFabButton.setVisibility(View.VISIBLE);
        } else {
            mFabButton.setVisibility(View.GONE);
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
                        mFragments.get(position).refreshPlot();
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert("Info", "Do you want to leave adjust?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AdjustActivity.this.finish();
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
        mTask = intent.getParcelableExtra(EXTRA_TASK);
    }

    @Override
    public void onAdjustFinish() {
        switch (mMode) {
            case MODE_LIGHT_ADJUST:
                showToast("light adjust finish");
                break;
            case MODE_MIDDLE_ADJUST:
                showToast("middle adjust finish");
                break;
            case MODE_HEAVY_ADJUST:
                showToast("heavy adjust finish");
                break;
        }
        setFabVisible(true);
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
                    return getString(R.string.adjust_light);
                case 1:
                    return getString(R.string.adjust_middle);
                case 2:
                    return getString(R.string.adjust_middle);
            }
            return null;
        }
    }

}
