package com.icenler.lib.base;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;

import com.icenler.lib.receiver.ExitAppReceiver;

/**
 * Created by iCenler - 2015/7/14.
 * Description：Activity 基类
 */
public class BaseActivity extends Activity {

    private ExitAppReceiver mExitAppReceiver = new ExitAppReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceivers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceivers();
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
