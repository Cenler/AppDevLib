package com.icenler.lib.utils.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.icenler.lib.base.BaseApplication;

import java.util.Map;
import java.util.Set;

/**
 * Created by iCenler - 2015/7/17.
 * Description：SharedPreferences 工具类
 * 1、 封装常用 保存、获取、删除、清空、匹配等方法
 * 2、 可初始化全局默认文件存储 or 指定文件进行存储
 */
public class SharedPrefsHelper {

    /**
     * 默认保存文件名称
     */
    private static String DEFAULT_CONFIG = "config";

    private SharedPrefsHelper() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 初始化默认保存配置文件名称
     *
     * @param configName
     */
    public static void initPrefsConfig(String configName) {
        if (StringHelper.notNull(configName))
            DEFAULT_CONFIG = configName;
    }

    /**
     * 保存数据到默认配置文件
     *
     * @param key
     * @param val
     * @param <T>
     */
    public static <T> void put(String key, T val) {
        put(null, key, val);
    }

    /**
     * 保存数据到指定配置文件
     *
     * @param configName
     * @param key
     * @param val
     * @param <T>
     */
    public static <T> void put(String configName, String key, T val) {
        SharedPreferences prefs = getSharedPrefs(configName);
        SharedPreferences.Editor edit = prefs.edit();

        if (val instanceof String) {
            edit.putString(key, (String) val);
        } else if (val instanceof Boolean) {
            edit.putBoolean(key, (Boolean) val);
        } else if (val instanceof Float) {
            edit.putFloat(key, (Float) val);
        } else if (val instanceof Integer) {
            edit.putInt(key, (Integer) val);
        } else if (val instanceof Long) {
            edit.putLong(key, (Long) val);
        } else if (val instanceof Set) {
            edit.putStringSet(key, (Set<String>) val);
        } else {
            new IllegalArgumentException("No matching type value was found");
        }

        edit.apply();// 异步读写
    }

    /**
     * 根据 key 值获取默认配置文件中数据
     *
     * @param key
     * @param defVal
     * @param <T>
     * @return
     */
    public static <T> T get(String key, T defVal) {
        return get(null, key, defVal);
    }

    /**
     * 根据 key 值获取指定配置文件中数据
     *
     * @param configName
     * @param key
     * @param defVal
     * @param <T>
     * @return
     */
    public static <T> T get(String configName, String key, T defVal) {
        SharedPreferences prefs = getSharedPrefs(configName);

        if (defVal instanceof String) {
            return (T) prefs.getString(key, (String) defVal);
        } else if (defVal instanceof Boolean) {
            return (T) Boolean.valueOf(prefs.getBoolean(key, (Boolean) defVal));
        } else if (defVal instanceof Float) {
            return (T) Float.valueOf(prefs.getFloat(key, (Float) defVal));
        } else if (defVal instanceof Integer) {
            return (T) Integer.valueOf(prefs.getInt(key, (Integer) defVal));
        } else if (defVal instanceof Long) {
            return (T) Long.valueOf(prefs.getLong(key, (Long) defVal));
        } else if (defVal instanceof Set) {
            return (T) prefs.getStringSet(key, (Set<String>) defVal);
        } else {
            throw new IllegalArgumentException("No matching type defVal was found");
        }
    }

    /**
     * @param key
     * @return 查询默认配置文件对应 key 值是否存在
     */
    public static boolean contains(String key) {
        return getSharedPrefs().contains(key);
    }

    /**
     * @param configName
     * @param key
     * @return 查询指定配置文件对应 key 值是否存在
     */
    public static boolean contains(String configName, String key) {
        return getSharedPrefs(configName).contains(key);
    }

    /**
     * 删除默认配置文件下 key 对应 value 值
     *
     * @param key
     */
    public static void remove(String key) {
        getSharedPrefs().edit().remove(key).apply();
    }

    /**
     * 删除指定配置文件下 key 对应 value 值
     *
     * @param configName
     * @param key
     */
    public static void remove(String configName, String key) {
        getSharedPrefs(configName).edit().remove(key).apply();
    }

    /**
     * @return 默认配置文件中所有数据
     */
    public static Map<String, ?> getAll() {
        return getSharedPrefs().getAll();
    }

    /**
     * @param configName
     * @return 指定配置文件中所有数据
     */
    public static Map<String, ?> getAll(String configName) {
        return getSharedPrefs(configName).getAll();
    }

    /**
     * 清空默认配置文件下所有数据
     */
    public static void clear() {
        getSharedPrefs().edit().clear().apply();
    }

    /**
     * 清空指定配置文件下所有数据
     *
     * @param configName
     */
    public static void clear(String configName) {
        getSharedPrefs(configName).edit().clear().apply();
    }

    private static SharedPreferences getSharedPrefs() {
        return getSharedPrefs(null);
    }

    /**
     * （Ps: BaseApplication 继承自 Application 创建时保存应用实例引用，
     * 并通过 getInstance 方法获取 App 全局上下文.）
     *
     * @param configName
     * @return SharedPreferences 实例
     */
    private static SharedPreferences getSharedPrefs(String configName) {
        SharedPreferences prefs;
        if (StringHelper.notNull(configName))
            prefs = BaseApplication.getInstance().getSharedPreferences(configName, Context.MODE_PRIVATE);
        else
            prefs = BaseApplication.getInstance().getSharedPreferences(DEFAULT_CONFIG, Context.MODE_PRIVATE);

        return prefs;
    }

}
