package com.tangzy.mvpframe.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.tangzy.mvpframe.manager.Constant;


/**
 * Created by Administrator on 2017/10/26.
 */

public class PrefUtils {
    public static final String PrefName = Constant.appName;

    private static final String account = "ACCOUNT";
    private static final String password = "PASSWORD";

    public static void setAccount(Context context, String name){
        setPrefString(context, account, name);
    }
    public static String getAccount(Context context){
        return getPrefString(context, account, "");
    }
    public static void setPassword(Context context, String psd){
        setPrefString(context, password, psd);
    }
    public static String getPassword(Context context){
       return getPrefString(context, password, "");
    }

    public static boolean getPrefBoolean(Context context, String key, boolean v) {
        SharedPreferences settings = context.getSharedPreferences(PrefName, 0);
        return settings.getBoolean(key, v);
    }

    public static void setPrefBoolean(Context context, String key, boolean v) {
        SharedPreferences settings = context.getSharedPreferences(PrefName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, v);
        editor.commit();
    }

    public static long getPrefLong(Context context, String key, long v) {
        SharedPreferences settings = context.getSharedPreferences(PrefName, 0);
        return settings.getLong(key, v);
    }

    public static void setPrefLong(Context context, String key, long v) {
        SharedPreferences settings = context.getSharedPreferences(PrefName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, v);
        editor.commit();
    }

    public static int getPrefInt(Context context, String key, int v) {
        SharedPreferences settings = context.getSharedPreferences(PrefName, 0);
        return settings.getInt(key, v);
    }

    public static void setPrefInt(Context context, String key, int v) {
        SharedPreferences settings = context.getSharedPreferences(PrefName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, v);
        editor.commit();
    }

    public static String getPrefString(Context context, String key, String v) {
        SharedPreferences settings = context.getSharedPreferences(PrefName, 0);
        return settings.getString(key, v);
    }

    public static void setPrefString(Context context, String key, String v) {
        SharedPreferences settings = context.getSharedPreferences(PrefName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, v);
        editor.commit();
    }
}
