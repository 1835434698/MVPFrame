package com.tangzy.mvpframe.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class MineFragment extends BaseFragment {

    private static final String TAG = "MineFragment";
    @BindView(R.id.tv_order_all)
    TextView tv_order_all;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logger.d(TAG, "onActivityCreated");
        setView(R.layout.fragment_mine);
//        setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                Logger.d("tangzy", "onRefresh003");
//                tv_order_all.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Logger.d("tangzy", "finishRefresh003");
//                        finishRefresh();
//                    }
//                }, 10000);
//            }
//        });
        initData();
    }

    private void initData() {

    }

}
