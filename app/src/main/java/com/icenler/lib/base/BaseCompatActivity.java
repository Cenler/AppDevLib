package com.icenler.lib.base;

import android.annotation.TargetApi;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.icenler.lib.R;
import com.icenler.lib.receiver.ExitAppReceiver;
import com.icenler.lib.utils.manager.SystemBarTintManager;

/**
 * Created by iCenler - 2015/9/15：
 * Description：不支持沉浸式状态栏 具体问题待排查
 */
public class BaseCompatActivity extends AppCompatActivity {

    private ExitAppReceiver mExitAppReceiver = new ExitAppReceiver();
    private SystemBarTintManager tintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceivers();
        initSystemBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceivers();
    }

    /**
     * 沉浸式状态栏设置 （19 版本以下不支持，但不会产生额外影响）
     */
    protected void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.color_status_bar_translucence);
    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * @return SystemBarTintManager 对象
     */
    protected SystemBarTintManager getTintManager() {
        // 可通过以下方法对状态栏颜色动态调整以及实现渐变效果
        // tintManager.setTintAlpha();
        // tintManager.setTintColor();……等
        return tintManager;
    }

    /**
     * 注册广播
     */
    protected void registerReceivers() {
        registerReceiver(mExitAppReceiver, new IntentFilter(ExitAppReceiver.ACTION_EXIT_APP));
    }

    /**
     * 注销广播
     */
    protected void unregisterReceivers() {
        unregisterReceiver(mExitAppReceiver);
    }

}
