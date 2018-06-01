package com.tangzy.mvpframe.util;

import android.text.TextUtils;
import android.util.Log;

import com.tangzy.mvpframe.manager.Constant;


/**
 * Created by Administrator on 2017/10/26.
 */

public class Logger {
//    private static int LOGLEVEL = 6;
//    private static int VERBISE = 1;
//    private static int DEBUG = 2;
//    private static int INFO = 3;
//    private static int WARN = 4;
//    private static int ERROR = 5;

    public static void v(String tag, String msg) {
        if (!Constant.isProduction) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (!Constant.isProduction) {
            int max_str_length = 2001;
            if (!TextUtils.isEmpty(tag)){
                max_str_length = max_str_length - tag.length();
            }
            //大于4000时
            while (msg.length() > max_str_length) {
                Log.d(tag, msg.substring(0, max_str_length));
                msg = msg.substring(max_str_length);
            }
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (!Constant.isProduction) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (!Constant.isProduction) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }


//    //根据需要将Log存放到SD卡中
////	private static String path = Constant.path;
//    private static File file;
//    private static FileOutputStream outputStream;
//    private static String pattern = "yyyy-MM-dd HH:mm:ss";
//
//    static {
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            File directory = new File(Constant.logPath);
//            if (!directory.exists()) {
//                directory.mkdirs();
//            }
//
//            file = new File(new File(Constant.logPath), "logException.txt");
//            Log.i("SDCAEDTAG", Constant.logPath);
//            try {
//                outputStream = new FileOutputStream(file, true);
//            } catch (FileNotFoundException e) {
//
//            }
//        }
//    }
//
//
//    /**
//     * 将错误信息保存到SD卡中去！可选的操作！
//     * @param msg
//     */
//    public static void save2Sd(String msg) {
//        Date date = new Date();
//        String time = DateFormatUtils.format(date, pattern);
//        if (Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)) {
//            if (outputStream != null) {
//                try {
//                    outputStream.write(time.getBytes());
//
//                    outputStream.write(msg.getBytes());
//                    outputStream.write("\r\n".getBytes());
//                    outputStream.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Log.i("SDCAEDTAG", "file is null");
//            }
//        }
//    }
}
