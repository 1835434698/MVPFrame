package com.tangzy.mvpframe.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tangzy.mvpframe.R;
import com.tangzy.mvpframe.fragment.base.BaseFragment;
import com.tangzy.mvpframe.util.Logger;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/19.
 */

public class SecondFragment extends BaseFragment {

    private static final String TAG = "SecondFragment";
    @BindView(R.id.tv_tab2)
    TextView tv_tab2;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Logger.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        setView(R.layout.fragment_second);
//        setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                Logger.d("tangzy", "onRefresh002");
//                tv_tab2.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Logger.d("tangzy", "finishRefresh002");
//                        finishRefresh();
//                    }
//                }, 10000);
//            }
//        });
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void init() {
    }
}
