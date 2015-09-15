package com.icenler.lib.base;

import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);    //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//透明导航栏
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.color_status_bar_translucence);
        }
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
