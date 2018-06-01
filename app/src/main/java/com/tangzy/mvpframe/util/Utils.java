package com.tangzy.mvpframe.util;

import android.app.Activity;

/**
 * Created by Administrator on 2018/5/31.
 */

public class Utils {


    public static long exitTime = 0;
    public static void exitApp(Activity activity) {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toasts.showToastShort("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            activity.finish();
        }
    }

}
