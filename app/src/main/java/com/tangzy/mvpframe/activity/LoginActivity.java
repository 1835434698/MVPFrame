package com.tangzy.mvpframe.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tangzy.mvpframe.MainApp;
import com.tangzy.mvpframe.R;
import com.tangzy.mvpframe.activity.base.BaseActivity;
import com.tangzy.mvpframe.bean.LoginBean;
import com.tangzy.mvpframe.core.view.LoginView;
import com.tangzy.mvpframe.listener.NoDoubleClickListener;
import com.tangzy.mvpframe.presenter.LoginPresenter;
import com.tangzy.mvpframe.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/31.
 */

public class LoginActivity extends BaseActivity implements LoginView{

    private LoginPresenter loginPresenter;

    @BindView(R.id.button)
    Button button;

    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.editText2)
    EditText editText2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("登录");
        hideTitleLeft();
        loginPresenter = new LoginPresenter(this);

        button.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        JSONObject json = new JSONObject();
                        try {
                            json.put("username","gaoyuan");
                            json.put("password","gaoyuan");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loginPresenter.login(json);
                    }
                }, R.string.ask_again, Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE);

            }
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Utils.exitApp(this);
    }

    @Override
    public void loginSucc(LoginBean content) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
