package com.icenler.lib.feature.base;

import android.annotation.TargetApi;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.icenler.lib.R;
import com.icenler.lib.receiver.ExitAppReceiver;
import com.icenler.lib.utils.manager.SystemBarTintManager;

/**
 * Created by iCenler - 2015/9/15.
 * Description：FragmentActivity 基类
 */
public class BaseFragmentActivity extends FragmentActivity {

    private ExitAppReceiver mExitAppReceiver = new ExitAppReceiver();

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
     * 沉浸式状态栏设置
     */
    protected void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
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
     * 注册广播
     */
    private void registerReceivers() {
        registerReceiver(mExitAppReceiver, new IntentFilter(ExitAppReceiver.ACTION_EXIT_APP));
    }

    /**
     * 注销广播
     */
    private void unregisterReceivers() {
        unregisterReceiver(mExitAppReceiver);
    }

}
