package com.icenler.lib.base;

import android.app.Application;
import android.content.Context;

import com.icenler.lib.receiver.ExitAppReceiver;
import com.icenler.lib.utils.LogUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by iCenler - 2015/7/13：
 * Description：应用启动初始化
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
        initImageLoaderConfig(getApplicationContext());
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
        // config.writeDebugLogs(); // 日志记录

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
    private void installLeakCanary() {
        // TODO 待引入环境自动检测控制
        mRefWatcher = LeakCanary.install(this);
        mRefWatcher = RefWatcher.DISABLED;
    }

    /**
     * 退出应用
     */
    public void exitApp() {
        ExitAppReceiver.exitApp(this);
        Runtime.getRuntime().exit(0);
        // android.os.Process.killProcess(android.os.Process.myPid());
    }

}
