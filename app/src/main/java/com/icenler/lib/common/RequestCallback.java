package com.icenler.lib.common;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by iCenler - 2015/10/28：
 * Description：
 */
public abstract class RequestCallback {

    private Context mContext;
    private Response.Listener mListener;
    private Response.ErrorListener mErrorListener;

    public RequestCallback(Context context) {
        this.mContext = context;
    }

    public Response.Listener<String> successListener() {
        mListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // TODO 全局成功处理
                onSuccess(response);
            }
        };

        return mListener;
    }

    public Response.ErrorListener errorListener() {
        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO 全局错误处理
                onError(error);
            }
        };

        return mErrorListener;
    }

    /**
     * 成功回调
     */
    public abstract void onSuccess(String result);

    /**
     * 失败回调
     */
    public abstract void onError(VolleyError error);

}
