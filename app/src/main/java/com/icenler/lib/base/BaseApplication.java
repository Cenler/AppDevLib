package com.icenler.lib.base;

import android.app.Application;

import com.icenler.lib.receiver.ExitAppBroadcast;
import com.icenler.lib.utils.LogUtil;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by iCenler - 2015/7/13：
 * Description：
 */
public class BaseApplication extends Application {

    private static BaseApplication mInstance;
    private static RefWatcher mRefWatcher;

    /**
     * @return App 全局上下文
     */
    public static BaseApplication getInstance() {
        return mInstance;
    }

    /**
     * @return 内存泄露检测工具
     */
    public static RefWatcher getRefWatcher() {
        return mRefWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mInstance = BaseApplication.this;
        LogUtil.i(this.getClass().getSimpleName());

        initAll();
    }

    private void initAll() {
        installLeakCanary();
    }

    /**
     * 内存泄露检测工具：release 版本下使用 RefWatcher.DISABLED // TODO 待引入环境自动检测控制
     */
    private void installLeakCanary() {
        mRefWatcher = LeakCanary.install(this);
        // mRefWatcher = RefWatcher.DISABLED;
    }

    /**
     * 退出应用
     */
    public void exitApp() {
        ExitAppBroadcast.exitApp(this);
        Runtime.getRuntime().exit(0);
        // android.os.Process.killProcess(android.os.Process.myPid());
    }

}
