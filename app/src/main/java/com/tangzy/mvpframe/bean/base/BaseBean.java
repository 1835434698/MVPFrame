package com.tangzy.mvpframe.bean.base;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/31.
 */

public class BaseBean implements Serializable {
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
