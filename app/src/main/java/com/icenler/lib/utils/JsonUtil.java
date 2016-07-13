package com.icenler.lib.utils;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by iCenler - 2015/9/9.
 * Description：JsonUtils工具类
 */
public class JsonUtil {

    private JsonUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * @param json
     * @param clazz
     * @return 解析 Json 中的数据封装到指定对象
     */
    public static <T> T parseJson2Obj(String json, @NonNull Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }


    /**
     * @param obj
     * @return 解析指定对象为 json 格式
     */
    public static String parseObj2Json(Object obj) {
        return JSON.toJSONString(obj);
    }


    /**
     * @param json
     * @param clazz
     * @return 解析 Json 中的数据封装到集合中
     */
    public static <T> List<T> parseJson2List(String json, @NonNull Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    /**
     * @param json
     * @param key
     * @return 获取 Json 中指定 key 值对应的 json 数据
     */
    public static String getJson2String(String json, String key) {
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject == null ? null : jsonObject.getString(key);
    }

}

