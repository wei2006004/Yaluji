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
public class TestActivity extends BaseActivity {

    public static final String EXTRA_MODE = "EXTRA_MODE";
    public static final String EXTRA_TASK = "EXTRA_TASK";

    public static final int MODE_SHOW_RESULT = 0;
    public static final int MODE_TASK_TEST = 1;

    private int mMode = MODE_SHOW_RESULT;
    private Task mTask = new Task();

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

    @ViewInject(R.id.container)
    ViewPager mViewPager;

    @ViewInject(R.id.layout_bottom)
    RelativeLayout mButtomLayout;

    @Event(R.id.fab_run)
    private void onRunClick(View view) {

    }

    @Event(R.id.fab_stop)
    private void onStopClick(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initExtraData();
        initData();
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
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        mFragments.clear();
        mFragments.add(new TraceFragment());
        mFragments.add(new ColorRunFragment());
        if (mMode == MODE_TASK_TEST) {
            mFragments.add(PlotFragment.newQuakePlotFragment());
            mFragments.add(PlotFragment.newTempPlotFragment());
        }
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
