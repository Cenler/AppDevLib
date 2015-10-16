package com.icenler.lib.utils;

import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import com.icenler.lib.ui.base.BaseApplication;

import java.io.File;

/**
 * Created by iCenler - 2015/9/10.
 * Description： Android 手机存储工具类，读取手机及SD卡相关信息
 */
public class MemoryUtil {

    private MemoryUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    /**
     * 获取手机存储路径
     *
     * @return
     */
    public static String getDataDirectoryPath() {
        return Environment.getDataDirectory().getAbsolutePath();
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    /**
     * 获取SD卡的总容量：单位 byte
     *
     * @return
     */
    public static long getSDCardTotalSize() {
        return isSDCardEnable() ? new StatFs(getSDCardPath()).getTotalBytes() : 0;
    }

    /**
     * 获取SD卡可用空间大小：单位 byte
     *
     * @return
     */
    public static long getSDCardFreeSize() {
        return isSDCardEnable() ? new StatFs(getSDCardPath()).getAvailableBytes() : 0;
    }

    /**
     * 获取手机总存储大小
     *
     * @return
     */
    public static long getRomTotalSize() {
        return new StatFs(getDataDirectoryPath()).getTotalBytes();
    }

    /**
     * 获取手机可用存储大小
     *
     * @return
     */
    public static long getRomAvailableSize() {
        return new StatFs(getDataDirectoryPath()).getAvailableBytes();
    }

    /**
     * 获取指定路径空间大小
     *
     * @param path
     * @return
     */
    public static long getPahtAvailableBytes(String path) {
        return new StatFs(path).getAvailableBytes();
    }

    /**
     * 获取指定文件可用空间大小
     *
     * @param file
     * @return
     */
    public static long getFileAvailableBytes(File file) {
        return new StatFs(file.getAbsolutePath()).getAvailableBytes();
    }

    /**
     * @param number
     * @return 自动格式化空间大小单位
     */
    public static String formatSpaceSize(long number) {
        return Formatter.formatFileSize(BaseApplication.getInstance(), number);
    }

}
