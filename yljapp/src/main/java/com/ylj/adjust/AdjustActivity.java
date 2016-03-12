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

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_adjust)
public class AdjustActivity extends BaseActivity implements AdjustFragment.OnAdjustFinishListener {

    public static final int ADJUST_TIME = 3;

    public static final String EXTRA_TASK = "EXTRA_TASK";

    public static void startNewActivity(Context context, Task task) {
        Intent intent = new Intent(context, AdjustActivity.class);
        intent.putExtra(EXTRA_TASK, task);
        context.startActivity(intent);
    }

    public static final int MODE_LIGHT_ADJUST = 0;
    public static final int MODE_MIDDLE_ADJUST = 1;
    public static final int MODE_HEAVY_ADJUST = 2;

    int mMode = MODE_LIGHT_ADJUST;

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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        initToolbar();
        initViewPager();
        initTabs();
        setFabVisible(false);
    }

    private void initData() {
        mFragments.add(new AdjustFragment());
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
                    return "LIGHT ADJUST";
                case 1:
                    return "MIDDLE ADJUST";
                case 2:
                    return "HEAVY ADJUST";
            }
            return null;
        }
    }

}
