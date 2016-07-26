package com.icenler.lib.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Fangde on 2016/2/23.
 * Description:
 */
@IntDef({0, 1, 2})
@Retention(RetentionPolicy.SOURCE)// 处理场景（源码可用时、编译时、运行时）
@Target(ElementType.PARAMETER)// 注解类型（类、字段、方法、构造参数、构造方法、局部变量、元注解、包）
public @interface Directory {
}
