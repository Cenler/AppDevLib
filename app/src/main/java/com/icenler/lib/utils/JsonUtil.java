package com.icenler.lib.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * JsonUtils工具类：
 * 采用 Gson and JsonObject 工具实现对 Json 数据的解析封装及互转.
 */
public class JsonUtil {

    /**
     * @param jsonData
     * @param clazz
     * @param <T>
     * @return 解析 Json 中的数据封装到指定对象
     */
    public static <T> T parseJson2Obj(String jsonData, Class<T> clazz) {
        T obj = null;
        Gson gson = null;

        if (!TextUtils.isEmpty(jsonData)) {
            gson = new Gson();
            obj = gson.fromJson(jsonData, clazz);
        }

        return obj;
    }


    /**
     * @param obj
     * @return 解析指定对象为 json 格式
     */
    public static String parseObj2Json(Object obj) {
        String jsonData = null;
        Gson gson = null;

        if (obj != null) {
            gson = new Gson();
            jsonData = gson.toJson(obj);
        }

        return jsonData;
    }


    /**
     * @param jsonData
     * @param clazz
     * @param <T>
     * @return 解析 Json 中的数据封装到集合中
     */
    public static <T> List<T> parseJson2List(String jsonData, Class<T> clazz) {
        Gson gson = null;
        Type type = null;
        List<T> list = null;

        if (!TextUtils.isEmpty(jsonData)) {
            gson = new Gson();
            type = new TypeToken<ArrayList<T>>() {
            }.getType();
            list = gson.fromJson(jsonData, type);
        }

        return list;
    }

    /**
     * @param jsonData
     * @param key
     * @return 获取 Json 中指定 key 值对应的 json 数据
     */
    public static String getJson2String(String jsonData, String key) {
        String val = null;
        JSONObject jObj = null;

        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(jsonData)) {
            try {
                jObj = new JSONObject(jsonData);
                val = jObj.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return val;
    }

}

