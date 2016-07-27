package com.icenler.lib.utils;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.icenler.lib.feature.App;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.icenler.lib.utils.Preconditions.checkNotNull;

/**
 * Created by Cenler on 2015/11/3.
 * Description:
 */
public class FileHelper {

    private FileHelper() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * 递归删除文件及文件夹
     *
     * @param file
     */
    public static boolean deleteAll(File file) {
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles != null && childFiles.length > 0) {
                for (int i = 0; i < childFiles.length; i++) {
                    deleteAll(childFiles[i]);
                }
            }
        }

        return file.delete();
    }


    /**
     * @param originPath
     * @param targetPath
     * @return 解压文件
     */
    public static boolean unZip(@NonNull String originPath, @NonNull String targetPath) {
        checkNotNull(originPath);

        return unZip(new File(originPath), targetPath);
    }

    public static boolean unZip(@NonNull File originFile, @NonNull String targetPath) {
        checkNotNull(originFile);

        try {
            return unZip(new FileInputStream(originFile), targetPath);
        } catch (FileNotFoundException e) {
            LogUtil.e("Origin file not found: " + originFile.getAbsolutePath(), e);
            return false;
        }
    }

    public static boolean unZip(@NonNull InputStream originInputStream, @NonNull String targetPath) {
        checkNotNull(originInputStream);
        checkNotNull(targetPath);

        CheckedInputStream checkedInputStream = new CheckedInputStream(originInputStream, new Adler32());
        BufferedInputStream bufferedInputStream = new BufferedInputStream(checkedInputStream);
        ZipInputStream zipInputStream = new ZipInputStream(bufferedInputStream);

        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int length;
        ZipEntry zipEntry;

        try {
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                File targetFile = new File(targetPath, zipEntry.getName());

                LogUtil.d(targetFile.getAbsolutePath());

                if (zipEntry.isDirectory()) {

                    targetFile.mkdirs();
                } else {

                    File parentFile = targetFile.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }

                    FileOutputStream fos = null;
                    BufferedOutputStream bos = null;
                    try {
                        fos = new FileOutputStream(targetFile);
                        bos = new BufferedOutputStream(fos, buffer.length * 4);
                        while ((length = zipInputStream.read(buffer)) > 0) {
                            bos.write(buffer, 0, length);
                        }
                        bos.flush();
                    } catch (IOException e) {
                        LogUtil.e("unzip file fail", e);
                    } finally {
                        closeQuietly(bos);
                        closeQuietly(fos);
                    }
                }
            }
            LogUtil.i("unzip file success. Checksum: " + checkedInputStream.getChecksum().getValue());

            return true;
        } catch (IOException e) {
            LogUtil.e("unzip file damaged", e);
            return false;
        } finally {
            closeQuietly(zipInputStream);
            closeQuietly(bufferedInputStream);
            closeQuietly(checkedInputStream);
            closeQuietly(originInputStream);
        }
    }

    /**
     * 序列化文件对象读取
     *
     * @param targetPath
     * @param fileName
     * @return
     */
    public static <T extends Object> T readObjectForDiskCache(String targetPath, String fileName) {
        if (TextUtils.isEmpty(targetPath) || TextUtils.isEmpty(fileName))
            return null;

        Object obj = null;
        File cacheFile;
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            cacheFile = new File(targetPath, fileName);
            fis = new FileInputStream(cacheFile);
            ois = new ObjectInputStream(fis);
            obj = ois.readObject();
        } catch (Exception e) {
            LogUtil.e("Error: ", e);
        } finally {
            closeQuietly(ois);
            closeQuietly(fis);
        }

        return (T) obj;
    }

    /**
     * 序列化对象文件存储
     *
     * @param targetPath
     * @param fileName
     * @param object
     */
    public static boolean writeObjectForDiskCache(String targetPath, String fileName, Serializable object) {
        if (TextUtils.isEmpty(targetPath) || TextUtils.isEmpty(fileName))
            return false;

        File targetDir;
        File cacheFile;

        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            targetDir = new File(targetPath);
            if (!targetDir.exists())
                targetDir.mkdirs();

            cacheFile = new File(targetDir, fileName);
            // 若文件存在则删除
            if (!cacheFile.createNewFile())
                cacheFile.delete();

            fos = new FileOutputStream(cacheFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.flush();

            return true;
        } catch (IOException e) {
            LogUtil.e("write object failed", e);
            return false;
        } finally {
            closeQuietly(oos);
            closeQuietly(fos);
        }

    }

    /**
     * 目录 or 文件复制
     *
     * @param originPath 来源目录
     * @param targetPath 目标目录
     */
    public static void copyFile(String originPath, String targetPath) {
        if (TextUtils.isEmpty(originPath) || TextUtils.isEmpty(targetPath))
            return;

        File origin = new File(originPath);
        File target = new File(targetPath);

        if (!target.exists())
            target.mkdirs();

        if (origin.isDirectory()) {
            // 目录操作
            LogUtil.d("Copy folder"
                    + " origin:" + originPath
                    + " target:" + targetPath);

            String[] fileList = origin.list();

            for (int i = 0; i < fileList.length; i++) {
                String childName = fileList[i];
                String folderName = origin.getName();

                copyFile(TextUtils.concat(originPath, File.separator, childName).toString()
                        , TextUtils.concat(targetPath, File.separator, folderName).toString());
            }
        } else {
            // 文件操作
            if (target.isDirectory() && target.canWrite()) {

                byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                int length;
                String filename = origin.getName();

                FileInputStream fis = null;
                FileOutputStream fos = null;
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;

                try {
                    fis = new FileInputStream(originPath);
                    fos = new FileOutputStream(new File(targetPath, filename));
                    bis = new BufferedInputStream(fis);
                    bos = new BufferedOutputStream(fos);

                    while ((length = bis.read(buffer)) > 0) {
                        bos.write(buffer, 0, length);
                    }
                    bos.flush();

                    LogUtil.d("Copy file success"
                            + " origin:" + originPath
                            + " target:" + targetPath);
                } catch (IOException e) {
                    LogUtil.e("Copy error:", e);
                } finally {
                    closeQuietly(bos);
                    closeQuietly(bis);
                    closeQuietly(fos);
                    closeQuietly(fis);
                }
            }
        }
    }

    /**
     * 目录 or 文件删除
     *
     * @param targetPath
     */
    public static void deleteFile(String targetPath) {
        if (TextUtils.isEmpty(targetPath))
            return;

        File file = new File(targetPath);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (file != null && files.length != 0) {
                    for (File f : files) {
                        deleteFile(f.getAbsolutePath());
                    }
                }
            }

            file.delete();

            LogUtil.d("Delete file:" + targetPath);
        }
    }

    /**
     * @param path 文件下载路径 or 文件路径
     * @return 文件名称
     */
    public static String getFileName(String path) {
        if (TextUtils.isEmpty(path))
            return null;

        return path.substring(path.lastIndexOf(File.separator) + 1, path.length());
    }

    /**
     * @param type {@link android.os.Environment#DIRECTORY_MUSIC},
     *             {@link android.os.Environment#DIRECTORY_PODCASTS},
     *             {@link android.os.Environment#DIRECTORY_RINGTONES},
     *             {@link android.os.Environment#DIRECTORY_ALARMS},
     *             {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
     *             {@link android.os.Environment#DIRECTORY_PICTURES},
     *             {@link android.os.Environment#DIRECTORY_MOVIES}
     * @return 获取应用缓存目录
     */
    public static String getFilesDirectory(String type) {
        return getAppStorageDirectory(type);
    }

    public static String getCacheDirectory() {
        return getAppStorageDirectory("cache");
    }

    private static String getAppStorageDirectory(String dirName) {
        if (isSDCardEnable()) {
            File file = App.getInstance().getExternalFilesDir(dirName);
            if (null == file) {
                // 特殊情况下为 null 处理
                String cacheDir = TextUtils.concat(
                        Environment.getExternalStorageDirectory().getPath(), File.separator
                        , "Android", File.separator
                        , "data", File.separator
                        , App.getInstance().getPackageName(), File.separator
                        , dirName, File.separator).toString();
                file = new File(cacheDir);
                file.mkdirs();
            }

            return file.getAbsolutePath();
        } else {
            return dirName == null ?
                    App.getInstance().getFileStreamPath(dirName).getAbsolutePath() :
                    App.getInstance().getFilesDir().getAbsolutePath();
        }
    }

    /**
     * @return 判断SDCard是否可用
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

}
