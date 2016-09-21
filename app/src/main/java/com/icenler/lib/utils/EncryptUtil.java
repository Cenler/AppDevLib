package com.icenler.lib.utils;

import com.icenler.lib.utils.helper.StringHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by iCenler - 2015/7/28：
 * Description：常用数据加密形式
 */
public class EncryptUtil {

    private EncryptUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static final String TYPE_MD5 = "MD5";
    private static final String TYPE_SHA1 = "SHA-1";

    /**
     * @param bytes
     * @return SHA1 算法加密
     */
    public static String makeSha1Sum(byte[] bytes) {
        return makeEncrypt(TYPE_SHA1, bytes);
    }

    /**
     * @param bytes
     * @return MD5 算法加密
     */
    public static String makeMd5Sum(byte[] bytes) {
        return makeEncrypt(TYPE_MD5, bytes);
    }

    /**
     * @param type
     * @param bytes
     * @return 根据机密类型加密
     */
    private static String makeEncrypt(String type, byte[] bytes) {
        String target = null;
        MessageDigest digest = null;
        if (bytes != null) {
            try {
                digest = MessageDigest.getInstance(type);
                digest.update(bytes);
                target = StringHelper.byte2Hex(digest.digest());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return target;
    }

}
