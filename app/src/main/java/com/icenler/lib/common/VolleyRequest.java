package com.icenler.lib.common;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.icenler.lib.feature.base.App;

import java.util.Map;

/**
 * Created by iCenler - 2015/10/28：
 * Description：Volley 请求封装
 */
public class VolleyRequest {

    private static StringRequest strReq;

    public static void reqGet(String url, String tag, RequestCallback callback) {
        App.getHttpQueues().cancelAll(tag);
        strReq = new StringRequest(Request.Method.GET, url, callback.successListener(), callback.errorListener());
        strReq.setTag(tag);
        App.getHttpQueues().add(strReq);
        App.getHttpQueues().start();
    }

    public static void reqPost(String url, String tag, final Map<String, String> params, RequestCallback callback) {
        App.getHttpQueues().cancelAll(tag);
        strReq = new StringRequest(Request.Method.POST, url, callback.successListener(), callback.errorListener()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        strReq.setTag(tag);
        App.getHttpQueues().add(strReq);
        App.getHttpQueues().start();
    }

}
