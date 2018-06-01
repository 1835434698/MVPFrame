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

public class FiveFragment extends BaseFragment {

    private static final String TAG = "FiveFragment";
    @BindView(R.id.tv_tab2)
    TextView tv_tab2;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Logger.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        setView(R.layout.fragment_five);
        init();
        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Logger.d("tangzy", "onRefresh005");
                tv_tab2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Logger.d("tangzy", "finishRefresh005");
                        finishRefresh();
                    }
                }, 10000);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void init() {
    }
}
