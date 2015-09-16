package com.icenler.lib.utils.manager;

import android.support.design.widget.Snackbar;
import android.view.ViewGroup;

/**
 * Created by iCenler - 2015/7/15.
 * Description：自定义全局 Snackbar
 * 1、 配合 CoordinatorLayout 实现滑动移除
 * 2、 TODO 待完善添加
 */
public class SnackbarManager {

    private SnackbarManager() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void show(ViewGroup parent, String msg) {
        Snackbar.make(parent, msg, Snackbar.LENGTH_LONG).show();
    }

    public static void show(ViewGroup parent, int strRes) {
        show(parent, parent.getContext().getString(strRes));
    }

}
