package com.tangzy.mvpframe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.tangzy.mvpframe.R;
import com.tangzy.mvpframe.activity.base.BaseActivity;
import com.tangzy.mvpframe.manager.Constant;
import com.tangzy.mvpframe.util.PrefUtils;
import com.tangzy.mvpframe.util.Utils;

/**
 * Created by Administrator on 2018/5/31.
 */

public class SplashActivity extends BaseActivity{

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if(!PrefUtils.getPrefBoolean(SplashActivity.this, "isFirst", true)) {
                startPage();
            }else {
                Intent intent = new Intent(SplashActivity.this, BannerActivity.class);
                startActivity(intent);
                finish();
//                startActivityForResult(intent, Constant.BANNER);
            }
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        hideTitle();
        mHandler.sendEmptyMessageDelayed(0, 1500);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Utils.exitApp(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constant.BANNER:
                if (resultCode == Activity.RESULT_OK){
                    startPage();
                }
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    private void startPage() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
