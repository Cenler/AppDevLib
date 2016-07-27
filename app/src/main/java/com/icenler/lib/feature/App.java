package com.icenler.lib.feature;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.icenler.lib.receiver.lifecycle.ApplicationLifecycleListener;
import com.icenler.lib.receiver.lifecycle.ExitAppReceiver;
import com.icenler.lib.utils.LogUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.List;

/**
 * Created by iCenler - 2015/7/13：
 * Description：应用启动初始化
 */
public class App extends Application {

    private static App mInstance;
    private static RefWatcher mRefWatcher;
    private static RequestQueue mHttpQueues;

    private ApplicationLifecycleListener mLiftListener;

    /**
     * @return App 全局上下文
     */
    public static App getInstance() {
        return mInstance;
    }

    /**
     * @return 内存泄露检测工具
     */
    public static RefWatcher getRefWatcher() {
        return mRefWatcher;
    }

    /**
     * @return Volley 全局请求队列
     */
    public static RequestQueue getHttpQueues() {
        return mHttpQueues;
    }

    /**
     * @return 前后台切换状态
     */
    public boolean isBackground() {
        return mLiftListener.isBackground();
    }

    /**
     * 退出应用
     */
    public void exitApp() {
        ExitAppReceiver.exitApp(this);
        Runtime.getRuntime().exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 独立进程导致重复初始化问题处理
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : runningApps) {
                if (processInfo.pid == android.os.Process.myPid()) {
                    if (!getPackageName().equals(processInfo.processName)) {
                        LogUtil.i(processInfo.processName);
                        return;
                    }
                }
            }
        }

        mInstance = App.this;
        mInstance.registerActivityLifecycleCallbacks(mLiftListener = new ApplicationLifecycleListener());

        initAllSdk();
    }

    private void initAllSdk() {
        initStrictMode();

        installLeakCanary(this);

        initRequestQueues(getApplicationContext());

        initImageLoaderConfig(getApplicationContext());
    }

    private void initStrictMode() {
        if (Constants.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy
                    .Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());// 磁盘读写及网络
            StrictMode.setVmPolicy(new StrictMode.VmPolicy
                    .Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());// 内存相关检测
        }
    }

    private void initRequestQueues(Context context) {
        mHttpQueues = Volley.newRequestQueue(context);
    }

    /**
     * ImageLoader 异步网络图片加载框架初始化
     */
    private void initImageLoaderConfig(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPoolSize(3);//线程池内加载的数量
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();// 禁止缓存多张图片
        config.memoryCacheSize(20 * 1024 * 1024);// 内存缓存 20 MiB
        config.diskCacheSize(50 * 1024 * 1024); // 固化缓存 50 MiB
        config.diskCacheFileCount(100);
        config.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)); // connectTimeout (5 s), readTimeout (30 s)超时时间
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());// 缓存文件名称 key 转换方式
        config.tasksProcessingOrder(QueueProcessingType.LIFO);//设置加载显示图片队列进程
        config.writeDebugLogs(); // 日志记录

        // Initialize ImageLoader with onfiguration.
        ImageLoader.getInstance().init(config.build());

        // 以下配置用于 displayImage(String uri, ImageView imageView, DisplayImageOptions options) 调用配置
        // DisplayImageOptions.Builder options = new DisplayImageOptions.Builder();
        // options.showImageOnLoading(R.mipmap.ic_launcher);            //设置图片在下载期间显示的图片
        // options.showImageForEmptyUri(R.mipmap.ic_launcher);          //设置图片Uri为空或是错误的时候显示的图片
        // options.showImageOnFail(R.mipmap.ic_launcher);               //设置图片加载/解码过程中错误时候显示的图片
        // options.cacheInMemory(true);                                 //设置下载的图片是否缓存在内存中
        // options.cacheOnDisk(true);                                   //设置下载的图片是否缓存在SD卡中
        // options.imageScaleType(ImageScaleType.EXACTLY_STRETCHED);    //设置图片以如何的编码方式显示
        // options.considerExifParams(true);                            //是否考虑JPEG图像EXIF参数（旋转，翻转）
        // options.bitmapConfig(Bitmap.Config.RGB_565);                 //设置图片的解码类型
        // options.decodingOptions(android.graphics.BitmapFactory.Options decodingOptions);//设置图片的解码配置
        // options.delayBeforeLoading(int delayInMillis);               //int delayInMillis为你设置的下载前的延迟时间
        // options.preProcessor(BitmapProcessor preProcessor);          //设置图片加入缓存前，对bitmap进行设置
        // options.resetViewBeforeLoading(true);                        //设置图片在下载前是否重置，复位
        // options.displayer(new RoundedBitmapDisplayer(20));           //是否设置为圆角，弧度为多少
        // options.displayer(new FadeInBitmapDisplayer(100));           //是否图片加载好后渐入的动画时间
        // options.build();
    }

    /**
     * 内存泄露检测工具：release 版本下使用 RefWatcher.DISABLED
     */
    private void installLeakCanary(Application app) {
        if (!Constants.DEBUG) {
            mRefWatcher = RefWatcher.DISABLED;
        } else {
            mRefWatcher = LeakCanary.install(app);
        }
    }

}
