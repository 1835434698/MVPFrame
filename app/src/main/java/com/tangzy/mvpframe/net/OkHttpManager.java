package com.tangzy.mvpframe.net;


import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.tangzy.mvpframe.bean.base.BaseBean;
import com.tangzy.mvpframe.manager.Constant;
import com.tangzy.mvpframe.util.Logger;
import com.tangzy.mvpframe.util.Toasts;

import org.json.JSONObject;
import org.lalic.base.AES;
import org.lalic.base.SA;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okio.BufferedSink;

/**
 * Created by Administrator on 2017/10/26.
 */

public class OkHttpManager{

    private final String TAG = "OkHttpManager";
    private String url;
    private OkHttpClient okHttpClient;

    private PersistentCookieStore cookieStore;
    private CookieJarImpl cookieJarImpl = new CookieJarImpl(cookieStore);
    private Handler mDeliverHandler;

    private static OkHttpManager okHttpManager = null;
    private Map<Integer, ResponseListener> map = new HashMap<>();

    public static  OkHttpManager getInstance(){
        if (okHttpManager == null){
            okHttpManager = new OkHttpManager();
        }
        return okHttpManager;
    }

    private OkHttpManager(){}

    private OkHttpClient getClient(){
        if (okHttpClient == null){
            okHttpClient = new OkHttpClient();
            okHttpClient.newBuilder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).cookieJar(cookieJarImpl).build();
        }
        return okHttpClient;
    }
    public void asyncRequest(final String uri, final JSONObject httpParams, final ResponseListener listener, final boolean isPost, final boolean isEnc) {
        if (!NetUtil.checkNetType(Constant.app)){
            Toasts.showToastLong(Constant.MESSSAGENET);
            listener.onErr(Constant.ERRORCODE,Constant.MESSSAGENET,uri);
            return;
        }
        mDeliverHandler = new Handler(Looper.getMainLooper());
        map.put(uri.hashCode(), listener);
        getResult(uri, httpParams, isPost, isEnc);
    }

    private void getResult(final String uri, JSONObject httpParams, boolean isPost, boolean isEnc) {
        final ResponseListener listener = map.get(uri.hashCode());
        map.remove(uri.hashCode());

        url = Constant.url+uri;
        if (cookieStore == null){
            cookieStore = new PersistentCookieStore(Constant.app);
        }
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        Request request = null;
        Logger.d(TAG, "onReq = "+httpParams);
        try {
            if (isPost){
                request = httpPost(httpParams, url, isEnc);
            }else {
                request = httpGet(httpParams, url, isEnc);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Logger.e(TAG, uri+"error = "+e.getMessage());
            if (listener != null){
                listener.onErr(Constant.ERRORCODE, e.getMessage(), uri);
            }
        }
        getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mDeliverHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onErr(Constant.ERRORCODE, e.getMessage(), uri);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String strResult=response.body().string();
                Logger.d(TAG, uri+"response = "+strResult);
                Gson gson = new Gson();
                final BaseBean netBean = gson.fromJson(strResult, BaseBean.class);
                mDeliverHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        switch (netBean.getCode()){
                            case 200:
                                if (netBean != null ){
                                    if (listener != null) {
                                        listener.onResp(strResult,uri);
                                    }
                                }
                                break;
//                            case 401:
//                                if (listener != null) {
//                                    listener.onErr(netBean.getCode(), netBean.getMsg(), uri);
//                                }
//                                    EventBus.getDefault().post(new MessgeEvent(Constant.Bus_logout));
//                                break;
                            default:
                                Toasts.showToastLong(netBean.getMsg());
                                if (listener != null) {
                                    listener.onErr(netBean.getCode(), netBean.getMsg(), uri);
                                }
                                break;
                        }
                    }
                });
            }
        });
    }

    private Request httpPost(JSONObject httpParams, String url, boolean isEnc) throws UnsupportedEncodingException {
        Request request;
        if (isEnc){
            FormBody.Builder  builder = new FormBody.Builder();
            String aes = "";
            if (httpParams != null && httpParams.length()>0) {
                aes = AES.encryptAES(httpParams.toString(), SA.AESK);
//                Logger.d(TAG, "onReq aes = "+aes);
                builder.add("key",aes);
            }
//            Logger.d(TAG, "token = "+UserInfo.getInstance().getTokenBean().getToken());
            Request.Builder builder1 = new Request.Builder().url(url);
//            builder1.addHeader("token", UserInfo.getInstance().getTokenBean().getToken());
            request = builder1.post(builder.build()).build();
        }else {
            FormBody.Builder  builder = new FormBody.Builder();
            if (httpParams != null && httpParams.length()>0) {
                Iterator<String> sIterator = httpParams.keys();
                String key;
                String value;
                while(sIterator.hasNext()){
                    // 获得key
                    key = sIterator.next();
                    // 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可
                    value = httpParams.optString(key);
                    builder.add(key,value);
                }
            }
            request =  new Request.Builder().url(url).post(builder.build()).build();
        }
        return request;
    }

    private Request httpGet(JSONObject httpParams, String url, boolean isEnc) throws UnsupportedEncodingException {
        Request request = null;
        StringBuilder sb = new StringBuilder();
        if (isEnc){
            String aes = "";
            if (httpParams != null && httpParams.length()>0) {
                aes = AES.encryptAES(httpParams.toString(), SA.AESK);
                Logger.d(TAG, "onReq aes = "+aes);
                sb.append("key");
                sb.append("=");
                sb.append(URLEncoder.encode(aes, "UTF-8"));
            }
            if(url.indexOf("?") >= 0){
                if(sb.length() > 0){
                    url = url + "&" + sb.substring(0, sb.length() - 1);
                }
            } else if(sb.length() > 0){
                url = url + "?" + sb.substring(0, sb.length() - 1);
            }
            Request.Builder builder1 = new Request.Builder().url(url);
//            builder1.addHeader("token", UserInfo.getInstance().getTokenBean().getToken());
            request = builder1.build();

        }else {
            if (httpParams != null && httpParams.length()>0) {
                Iterator<String> sIterator = httpParams.keys();
                String key;
                String value;
                while(sIterator.hasNext()){
                    // 获得key
                    key = sIterator.next();
                    // 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可
                    value = httpParams.optString(key);
                    sb.append(key);
                    sb.append("=");
                    sb.append(URLEncoder.encode(value, "UTF-8"));
                    sb.append("&");
                }
                if(url.indexOf("?") >= 0){
                    if(sb.length() > 0){
                        url = url + "&" + sb.substring(0, sb.length() - 1);
                    }
                } else if(sb.length() > 0){
                    url = url + "?" + sb.substring(0, sb.length() - 1);
                }
                request = new Request.Builder().url(url).build();
            }
        }
        return request;
    }


    public interface ResponseListener {
        public void onResp(String respons, String uri);

        public void onErr(int errorCode, String respons, String uri);
    }

    /** Returns a new request body that transmits {@code content}. */
    public RequestBody create(final MediaType contentType, final byte[] content) {
        return create(contentType, content, 0, content.length);
    }

    /** Returns a new request body that transmits {@code content}. */
    public RequestBody create(final MediaType contentType, final byte[] content,
                                     final int offset, final int byteCount) {
        if (content == null) throw new NullPointerException("content == null");
        Util.checkOffsetAndCount(content.length, offset, byteCount);
        return new RequestBody() {
            @Override public MediaType contentType() {
                return contentType;
            }

            @Override public long contentLength() {
                return byteCount;
            }

            @Override public void writeTo(BufferedSink sink) throws IOException {
                sink.write(content, offset, byteCount);
            }
        };
    }
}
