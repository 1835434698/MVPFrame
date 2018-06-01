package com.tangzy.mvpframe.core.view;


import android.app.Activity;
import android.content.Context;

/**
 * Created by Administrator on 2018/5/31.
 * 所有View基类
 */

public interface MvpView {

    Context obtainContext();

    Activity obtainActivity();

    /**
     * only show loading view
     */
    void showLoading(String waitMessage);
    void hideLoading();

}
