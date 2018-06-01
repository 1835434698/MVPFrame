package com.tangzy.mvpframe.core.view;

import com.tangzy.mvpframe.bean.LoginBean;

/**
 * Created by Administrator on 2018/5/31.
 */
public interface LoginView extends MvpView {
    void loginSucc(LoginBean content);
}
