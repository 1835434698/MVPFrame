package com.tangzy.mvpframe.presenter;

        import com.tangzy.mvpframe.core.presenter.MvpPresenterIml;
        import com.tangzy.mvpframe.core.view.LoginView;
        import com.tangzy.mvpframe.manager.Constant;
        import com.tangzy.mvpframe.net.OkHttpManager;
        import com.tangzy.mvpframe.util.Logger;

        import org.json.JSONObject;

/**
 * Created by Administrator on 2018/5/31.
 */

public class LoginPresenter extends MvpPresenterIml<LoginView> {

    public LoginPresenter(LoginView context) {
        super(context);
    }

    public void login(JSONObject json){
        getView().showLoading("");
        OkHttpManager.getInstance().asyncRequest(Constant.Api_login, json, new OkHttpManager.ResponseListener() {
            @Override
            public void onResp(String respons, String uri) {
                getView().hideLoading();
                Logger.d("tangzy", "succ");
                getView().loginSucc(null);

            }

            @Override
            public void onErr(int errorCode, String respons, String uri) {
                getView().hideLoading();
                Logger.d("tangzy", "error");
            }
        }, true, true);

    }

}
