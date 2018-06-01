package com.tangzy.mvpframe.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.tangzy.mvpframe.R;
import com.tangzy.mvpframe.activity.base.BaseActivity;
import com.tangzy.mvpframe.fragment.BannerItemFragment;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/31.
 */

public class BannerActivity extends BaseActivity{
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle bundle) {
        // TODO Auto-generated method stub
        super.onCreate(bundle);
        setContentView(R.layout.activity_banner);
        hideTitle();
        initView();
        initAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initAdapter() {
        // TODO Auto-generated method stub
        mViewPager.setAdapter(new ViewPagerAdapter(this.getSupportFragmentManager()));
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void initView() {
        // TODO Auto-generated method stub
        mViewPager = findViewById(R.id.mViewPager);
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int index) {
            // TODO Auto-generated method stub
            Fragment f= new BannerItemFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("index",index);
            f.setArguments(bundle);
            return f;
        }



        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 3;
        }
    }
}
