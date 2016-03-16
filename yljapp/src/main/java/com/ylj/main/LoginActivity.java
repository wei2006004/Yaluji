package com.ylj.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.ylj.R;
import com.ylj.common.BaseActivity;
import com.ylj.common.config.Config;
import com.ylj.common.config.ConfigLet;
import com.ylj.common.config.Global;
import com.ylj.main.fragment.AdminLoginFragment;
import com.ylj.main.fragment.CommonLoginFragment;
import com.ylj.staff.AdminModifyActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements CommonLoginFragment.OnSwitchToAdminLoginListener,
        AdminLoginFragment.OnSwitchToCommonLoginListener {

    SectionsPagerAdapter mSectionsPagerAdapter;

    @ViewInject(R.id.container)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    @Override
    public void onResume(){
        super.onResume();
        if(ConfigLet.isFirstLaunch()){
            AdminModifyActivity.startAsNewAdminActivity(this);
            Config.appInstance().setBoolConfig(Global.PREF_TAG_FIRST_LAUNCH, false);
        }
    }

    @Override
    public void onSwitchToAdminLogin() {
        mViewPager.setCurrentItem(1, true);
    }

    @Override
    public void onSwitchToCommonLogin() {
        mViewPager.setCurrentItem(0, true);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new CommonLoginFragment();
            } else {
                return new AdminLoginFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
            }
            return null;
        }
    }
}
