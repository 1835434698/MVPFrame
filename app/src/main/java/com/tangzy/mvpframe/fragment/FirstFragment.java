package com.tangzy.mvpframe.fragment;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tangzy.mvpframe.R;
import com.tangzy.mvpframe.adapter.HomeAdapter;
import com.tangzy.mvpframe.fragment.base.BaseFragment;
import com.tangzy.mvpframe.util.Logger;
import com.tangzy.mvpframe.view.CanForbidViewPager;
import com.tangzy.mvpframe.view.CustomListView;
import com.tangzy.mvpframe.view.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by Administrator on 2017/10/19.
 */

public class FirstFragment extends BaseFragment {

    private static final String TAG = "FirstFragment";

    @BindView(R.id.main_home_viewpager)
    CanForbidViewPager main_home_viewpager;
    @BindView(R.id.rl_viewpager_text)
    TextView rl_viewpager_text;
    @BindView(R.id.lv_home)
    CustomListView lv_home;
    private HomeAdapter homeAdapter;
    private List<String> homes = new ArrayList<>();

    /**
     * 需要进行检测的权限数组
     */
    private  String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };


    private List<String> adBeans = new ArrayList<>();//广告列表
    private int count = 0;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    startCicle();
                    break;
            }
        }
    };


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Logger.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        setView(R.layout.fragment_first);
        init();
        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Logger.d("tangzy", "onRefresh001");
                getData(0);
            }
        });
        setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getData(1);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void init() {
        main_home_viewpager.setAdapter(viewPagerAdapter);
        homeAdapter = new HomeAdapter(getActivity(), homes);
        lv_home.setAdapter(homeAdapter);

        getData(0);

        adBeans.add("1");
        adBeans.add("1");
        adBeans.add("1");
        viewPagerAdapter.notifyDataSetChanged();
        startPicCicle();
    }

    private void getData(int ik){
        if (ik ==0){
            homes.clear();
        }
        for (int i=0; i<15; i++){
            homes.add(""+i);
        }
        finishRefresh();
        finishLoadmore();
        homeAdapter.notifyDataSetChanged();
        if (ik ==0){
            getScrollView().smoothScrollTo(0,0);
        }
    }

    private void startPicCicle() {
        count = 0;
        if (viewPagerAdapter.getCount() > 0){
            startCicle();
        }
    }

    private void startCicle() {
        int i = count % viewPagerAdapter.getCount();
        main_home_viewpager.setCurrentItem(i);
        count++;
        mHandler.sendEmptyMessageDelayed(0, 3000);
    }


    private int max = 20000;
    private PagerAdapter viewPagerAdapter = new PagerAdapter() {

        private int resourceNo;

        @Override
        public int getCount() {
            return adBeans.size() == 0 ? 0 : max;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(getActivity(), R.layout.item_viewpager, null);
            ImageView image = view.findViewById(R.id.viewpager_imageview);
            if (adBeans.size() == 0) {
                return view;
            }
//            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.viewpager_item_progressbar);
            resourceNo = position % adBeans.size();
            rl_viewpager_text.setText((resourceNo+1)+"/"+adBeans.size());
//            PictureBean bean = adBeans.get(resourceNo);
//            view.setTag(resourceNo);
//            String imageUrl = bean.getImageUrl();
//            Logger.d(TAG, "imageUrl = "+imageUrl);
//
//            Glide.with(getActivity()).load(imageUrl).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(image);
            container.addView(view);
            return view;
        }
    };



}
