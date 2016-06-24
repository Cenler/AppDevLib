package com.icenler.lib.utils.helper;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by iCenler - 2015/7/17.
 * Description：常用字符串工具类
 * 1、 常用正则表达式判断
 * 2、 ……
 */
public class StringHelper {

    private StringHelper() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 中文匹配
     */
    public static boolean isChinese(String str) {
        Pattern pattern = Pattern.compile("^[\\u0391-\\uffe5]*$");
        Matcher matcher = pattern.matcher(str);

        return matcher.matches();
    }

    /**
     * 邮箱匹配
     */
    public static boolean checkEmail(String str) {
        Pattern pattern = Pattern.compile("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$");
        Matcher matcher = pattern.matcher(str);

        return matcher.matches();
    }

    /**
     * 6-20位数字或字母账户匹配
     */
    public static boolean checkAccount(String str) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{6,20}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 身份证验证
     */
    public static boolean checkIDCard(String idCard) {
        Pattern pattern = Pattern.compile("^(([0-9]{14}[x0-9]{1})|([0-9]{17}[x0-9]{1}))$");
        Matcher matcher = pattern.matcher(idCard);

        return matcher.matches();
    }

    /**
     * 手机号断断
     * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186
     * 电信：133、153、180、189、（1349卫通）
     */
    public static boolean isMobile(String mobiles) {
        // ^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$" or
        // ^[1]([3][0-9]{1}|58|59|88|89)[0-9]{8}$"
        Pattern CMCC_REG_EX = Pattern.compile("^1(3[4-9]|5[01789]|8[78])[0-9]{8}$"); // 移动
        Pattern CUCC_REG_EX = Pattern.compile("^1(3[0-2]|5[256]|8[56])[0-9]{8}$");   // 联通
        Pattern CTCC_REG_EX = Pattern.compile("^1(33|53|8[09])[0-9]{8}$");           // 电信
        Pattern pattern = Pattern.compile("^[1]([3][0-9]{1}|58|59|88|89)[0-9]{8}$");
        Matcher matcher = pattern.matcher(mobiles);

        if (matcher.matches()) {
            if (CMCC_REG_EX.matcher(mobiles).matches())
                NetworkHelper.MOBILES_TYPE = NetworkHelper.CMCC;
            else if (CUCC_REG_EX.matcher(mobiles).matches())
                NetworkHelper.MOBILES_TYPE = NetworkHelper.CUCC;
            else if (CTCC_REG_EX.matcher(mobiles).matches())
                NetworkHelper.MOBILES_TYPE = NetworkHelper.CTCC;

            return true;
        } else {
            return false;
        }
    }

    /**
     * 不支持含小数点的数字
     */
    public static boolean isPureNumber(String str) {
        Pattern p = Pattern.compile("^[0-9]*$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 检查是否是数字，支持含小数点的数字
     */
    public static boolean isValidNumber(String str) {
        Pattern p = Pattern.compile("^\\d+\\.{0,1}\\d*$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 判断输入是否是6-20位数字和字母组合
     */
    public static boolean isNumAndLetter(String str) {
        Pattern p = Pattern.compile("(?!^[0-9]+$)(?!^[a-zA-Z]+$)[0-9a-zA-Z]{6,20}");
        Matcher m = p.matcher(str);
        return m.matches();
    }


    /**
     * Url 连接匹配 Patterns 参见文档
     */
    public static boolean isWebUrl(String url) {
        return Patterns.WEB_URL.matcher(url).matches();
    }

    /**
     * @param bytes
     * @return 返回字节码的十六进制表示形式
     */
    public static String byte2Hex(byte[] bytes) {
        final String HEX = "0123456789ABCDEF";
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes) {
            sb.append(HEX.charAt((b >> 4) & 0x0F));
            sb.append(HEX.charAt(b & 0x0F));
        }

        return sb.toString();
    }

}
