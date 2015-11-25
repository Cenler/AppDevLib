package com.icenler.lib.utils.helper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Cenler on 2015/11/2.
 * Description: 反射 API 封装帮助类
 */
public class RefInvokerHelper {

    private RefInvokerHelper() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 反射调用静态方法
     *
     * @param className
     * @param methodName
     * @param paramTypes
     * @param paramValues
     * @return
     */
    public static Object invokeStaticMethod(String className, String methodName, Class[] paramTypes, Object[] paramValues) {
        return invokeMethod(null, className, methodName, paramTypes, paramValues);
    }

    public static Object invokeStaticMethod(Class clazz, String methodName, Class[] paramTypes, Object[] paramValues) {
        return invokeMethod(null, clazz, methodName, paramTypes, paramValues);
    }

    /**
     * 放射调用指定对象内方法
     *
     * @param target
     * @param clazz
     * @param methodName
     * @param paramTypes
     * @param paramValues
     * @return
     */
    public static Object invokeMethod(Object target, Class clazz, String methodName, Class[] paramTypes, Object[] paramValues) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);
            return method.invoke(target, paramValues);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object invokeMethod(Object target, String className, String methodName, Class[] paramTypes, Object[] paramValues) {
        try {
            Class clazz = Class.forName(className);
            return invokeMethod(target, clazz, methodName, paramTypes, paramValues);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取指定类静态字段
     *
     * @param className
     * @param fieldName
     * @return
     */
    public static Object getStaticFieldObject(String className, String fieldName) {
        return getFieldObject(null, className, fieldName);
    }

    /**
     * 获取指定指定类字段
     *
     * @param target
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Object getFieldObject(Object target, Class clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // try supper for Miui, Miui has a class named MiuiPhoneWindow
            try {
                Field field = clazz.getSuperclass().getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(target);
            } catch (Exception superE) {
                e.printStackTrace();
                superE.printStackTrace();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object getFieldObject(Object target, String className, String fieldName) {
        Class clazz = null;
        try {
            clazz = Class.forName(className);
            return getFieldObject(target, clazz, fieldName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 为指定静态字段赋值
     *
     * @param className
     * @param fieldName
     * @param fieldValue
     */
    public static void setStaticOjbect(String className, String fieldName, Object fieldValue) {
        setFieldObject(null, className, fieldName, fieldValue);
    }

    /**
     * 为指定对象内字段赋值
     *
     * @param target
     * @param clazz
     * @param fieldName
     * @param fieldValue
     */
    public static void setFieldObject(Object target, Class clazz, String fieldName, Object fieldValue) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, fieldValue);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // try supper for Miui, Miui has a class named MiuiPhoneWindow
            try {
                Field field = clazz.getSuperclass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, fieldValue);
            } catch (Exception superE) {
                e.printStackTrace();
                superE.printStackTrace();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setFieldObject(Object target, String className, String fieldName, Object fieldValue) {
        Class clazz = null;
        try {
            clazz = Class.forName(className);
            setFieldObject(target, clazz, fieldName, fieldValue);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
