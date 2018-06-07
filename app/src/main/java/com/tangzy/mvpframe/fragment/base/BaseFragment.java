package com.tangzy.mvpframe.fragment.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tangzy.mvpframe.R;
import com.tangzy.mvpframe.activity.base.BaseActivity;
import com.tangzy.mvpframe.core.presenter.MvpPresenterIml;
import com.tangzy.mvpframe.util.Logger;
import com.tangzy.mvpframe.view.CustomListViewHeader;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/5/31.
 */

public class BaseFragment extends Fragment {
    protected String mPageName = "BaseFragment";
    private Unbinder unbinder;
    public View root;
    //    private LoadingView mLoadingView = null;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mPageName = getClass().getSimpleName();
        root = inflater.inflate(R.layout.fragment_base, null);
        return root;
    }

    public void addPresenter(MvpPresenterIml presenterIml){
        Logger.d(mPageName, "addPresenter");
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).addPresenter(presenterIml);
        }
    }
    public void setView(int layoutResID) {
        LinearLayout content_linear = (LinearLayout) findViewById(R.id.fragment_content_view);
        content_linear.addView(View.inflate(getActivity(), layoutResID, null), new LinearLayout.LayoutParams(-1, -1));
        unbinder = ButterKnife.bind(this,root);
//        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.load_more_list_view_ptr_frame);
//        customHead = (CustomListViewHeader) findViewById(R.id.customHead);
//        scrollView = (ScrollView) findViewById(R.id.scrollView);
//        customHead.setStyle(2);
//        setEnableRefresh(false);
//        setEnableLoadmore(false);
    }

    public View findViewById(int viewID){
        return root.findViewById(viewID);
    }

    @Override
    public void onDestroyView() {
        if(unbinder != null)
            unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
