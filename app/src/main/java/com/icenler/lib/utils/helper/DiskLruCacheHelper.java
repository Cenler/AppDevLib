package com.icenler.lib.utils.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.icenler.lib.utils.AppUtil;
import com.icenler.lib.utils.BitmapUtil;
import com.icenler.lib.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by iCenler - 2015/9/9.
 * Description：固化缓存帮助类 配合 LruCache 实现三级缓存机制
 */
public class DiskLruCacheHelper {

    private static final String TAG = "DiskLruCacheHelper";

    private static final String DEF_DIR_NAME = "diskCache";// 默认缓存目录
    private static final int DEF_MAX_CACHE_SIZE = 5 * 1024 * 1024;  // 最大缓存容量
    private static final int DEF_VALUE_COUNT = 1;                   // key 所对应 value 数量

    private DiskLruCache mDiskLruCache;

    public DiskLruCacheHelper(Context context) throws IOException {
        mDiskLruCache = generateCache(context, DEF_DIR_NAME, DEF_MAX_CACHE_SIZE);
    }

    public DiskLruCacheHelper(Context context, String dirName) throws IOException {
        mDiskLruCache = generateCache(context, dirName, DEF_MAX_CACHE_SIZE);
    }

    public DiskLruCacheHelper(Context context, String dirName, int maxCount) throws IOException {
        mDiskLruCache = generateCache(context, dirName, maxCount);
    }

    //custom cache dir
    public DiskLruCacheHelper(File dir) throws IOException {
        mDiskLruCache = generateCache(null, dir, DEF_MAX_CACHE_SIZE);
    }

    public DiskLruCacheHelper(Context context, File dir) throws IOException {
        mDiskLruCache = generateCache(context, dir, DEF_MAX_CACHE_SIZE);
    }

    public DiskLruCacheHelper(Context context, File dir, int maxCount) throws IOException {
        mDiskLruCache = generateCache(context, dir, maxCount);
    }

    private DiskLruCache generateCache(Context context, File dir, int maxCount) throws IOException {
        if (!dir.exists()) {
            if (dir.mkdirs() && !dir.isDirectory()) {
                throw new IllegalArgumentException(dir + " is not a directory or generate path failed");
            }
        }

        return DiskLruCache.open(dir, AppUtil.getAppVersionCode(context), DEF_VALUE_COUNT, maxCount);
    }

    private DiskLruCache generateCache(Context context, String dirName, int maxCount) throws IOException {
        return DiskLruCache.open(
                getDiskCacheDir(context, dirName),
                AppUtil.getAppVersionCode(context),
                DEF_VALUE_COUNT,
                maxCount);
    }


    // =======================================
    // ============== String 数据 读写 =============
    // =======================================
    public void put(String key, String value) {
        DiskLruCache.Editor edit = null;
        BufferedWriter bw = null;
        try {
            edit = editor(key);
            if (edit == null) return;
            OutputStream os = edit.newOutputStream(0);
            bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(value);
            edit.commit();//write CLEAN
        } catch (IOException e) {
            e.printStackTrace();
            try {
                //s
                edit.abort();//write REMOVE
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getAsString(String key) {
        InputStream inputStream = null;
        try {
            //write READ
            inputStream = get(key);
            if (inputStream == null) return null;
            StringBuilder sb = new StringBuilder();
            int len = 0;
            byte[] buf = new byte[128];
            while ((len = inputStream.read(buf)) != -1) {
                sb.append(new String(buf, 0, len));
            }

            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        }
        return null;
    }

    public void put(String key, JSONObject jsonObject) {
        put(key, jsonObject.toString());
    }

    public JSONObject getAsJson(String key) {
        String val = getAsString(key);
        try {
            if (val != null)
                return new JSONObject(val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    // =======================================
    // ============ JSONArray 数据 读写 =============
    // =======================================
    public void put(String key, JSONArray jsonArray) {
        put(key, jsonArray.toString());
    }

    public JSONArray getAsJSONArray(String key) {
        String JSONString = getAsString(key);
        try {
            JSONArray obj = new JSONArray(JSONString);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // =======================================
    // ============== byte 数据 读写 =============
    // =======================================
    public void put(String key, byte[] bytes) {
        OutputStream out = null;
        DiskLruCache.Editor editor = null;
        try {
            editor = editor(key);
            if (editor == null) {
                return;
            }
            out = editor.newOutputStream(0);
            out.write(bytes);
            out.flush();
            editor.commit();//write CLEAN
        } catch (Exception e) {
            e.printStackTrace();
            try {
                editor.abort();//write REMOVE
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public byte[] getAsBytes(String key) {
        byte[] res = null;
        InputStream is = get(key);
        if (is == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buf = new byte[256];
            int len = 0;
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            res = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }


    // =======================================
    // ============== 序列化 数据 读写 =============
    // =======================================
    public void put(String key, Serializable value) {
        DiskLruCache.Editor editor = editor(key);
        ObjectOutputStream oos = null;
        if (editor == null) return;
        try {
            OutputStream os = editor.newOutputStream(0);
            oos = new ObjectOutputStream(os);
            oos.writeObject(value);
            oos.flush();
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                editor.abort();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (oos != null)
                    oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public <T> T getAsSerializable(String key) {
        T t = null;
        InputStream is = get(key);
        ObjectInputStream ois = null;
        if (is == null) return null;
        try {
            ois = new ObjectInputStream(is);
            t = (T) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null)
                    ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return t;
    }


    // =======================================
    // ============== bitmap 数据 读写 =============
    // =======================================
    public void put(String key, Bitmap bitmap) {
        put(key, BitmapUtil.bitmap2Bytes(bitmap));
    }

    public Bitmap getAsBitmap(String key) {
        byte[] bytes = getAsBytes(key);
        if (bytes == null) return null;

        return BitmapUtil.bytes2Bitmap(bytes);
    }

    // =======================================
    // ============= drawable 数据 读写 =============
    // =======================================
    public void put(String key, Drawable value) {
        put(key, BitmapUtil.drawable2Bitmap(value));
    }

    public Drawable getAsDrawable(String key) {
        byte[] bytes = getAsBytes(key);
        if (bytes == null) {
            return null;
        }

        return BitmapUtil.bitmap2Drawable(BitmapUtil.bytes2Bitmap(bytes));
    }

    // =======================================
    // ============= other methods =============
    // =======================================
    public boolean remove(String key) {
        try {
            key = hashKeyForDisk(key);
            return mDiskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void close() throws IOException {
        mDiskLruCache.close();
    }

    public void delete() throws IOException {
        mDiskLruCache.delete();
    }

    public void flush() throws IOException {
        mDiskLruCache.flush();
    }

    public boolean isClosed() {
        return mDiskLruCache.isClosed();
    }

    public long size() {
        return mDiskLruCache.size();
    }

    public File getDirectory() {
        return mDiskLruCache.getDirectory();
    }

    /**
     * 大文件处理操作 - 直接通过流读写
     *
     * @param key
     * @return
     */
    public DiskLruCache.Editor editor(String key) {
        try {
            key = hashKeyForDisk(key);
            //wirte DIRTY
            DiskLruCache.Editor edit = mDiskLruCache.edit(key);
            //edit maybe null :the entry is editing
            if (edit == null) {
                LogUtil.w("the entry spcified key:" + key + " is editing by other . ");
            }
            return edit;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public InputStream get(String key) {
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(hashKeyForDisk(key));
            //not find entry , or entry.readable = false
            if (snapshot == null) {
                LogUtil.w("not find entry , or entry.readable = false");
                return null;
            }
            //write READ
            return snapshot.getInputStream(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param context
     * @param uniqueName 缓存文件夹
     * @return 指定存储路径
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * @param key
     * @return 转换存储唯一标识
     */
    private static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(key.getBytes());
            cacheKey = StringHelper.byte2Hex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }

        return cacheKey;
    }

}



