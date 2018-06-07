package com.tangzy.mvpframe.manager;

import org.json.JSONObject;

/**
 * Created by tangzy on 17/11/10.
 */

public class MessgeEvent {

    private String mMsg;
    private JSONObject json;
    public MessgeEvent(String msg){
        this.mMsg = msg;
    }

    public String getMsg(){
        return mMsg;
    }

    public String getmMsg() {
        return mMsg;
    }

    public void setmMsg(String mMsg) {
        this.mMsg = mMsg;
    }

    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }
}
