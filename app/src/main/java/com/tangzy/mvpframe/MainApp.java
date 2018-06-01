package com.tangzy.mvpframe;

import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

import com.tangzy.mvpframe.manager.Constant;

/**
 * Created by Administrator on 2018/5/31.
 */

public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Constant.app = this;

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Constant.widthScreen = wm.getDefaultDisplay().getWidth();
        Constant.heightScreen = wm.getDefaultDisplay().getHeight();
    }
}
