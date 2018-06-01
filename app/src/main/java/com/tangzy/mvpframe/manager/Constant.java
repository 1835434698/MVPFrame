package com.tangzy.mvpframe.manager;

import android.content.Context;
import android.os.Environment;

/**
 * Created by Administrator on 2018/5/31.
 */

public class Constant {
    public static boolean isProduction = false;
    public static Context app;
    public static int widthScreen;
    public static int heightScreen;
    public static String MESSSAGENET = "无网络";
    public static String MESSSAGE = "网络错误";
    public static String MESSSAGEJSON = "数据解析错误";
    public static int ERRORCODE = -1;
    public static String path = Environment.getExternalStorageDirectory().toString()+"/MVPFrame";

    public static final String url = "http://120.27.23.38:80/api/";

    //api
    public static final String Api_login = "login";

    public static String appName = "MVPFrame";
    public static final int BANNER = 1003;
}
