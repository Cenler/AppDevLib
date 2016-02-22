package com.multichannel;

/**
 * Created by Fangde on 2016/2/22.
 * Description:
 */
public class MultiChannelBuildTool {

    public static String[] channels = new String[]{
            "Qihoo_360",
            "Tencent_qq",
            "Baiudu",
            "Wandoujia",
            "Xiaomi"
    };

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        System.out.println((System.currentTimeMillis() - startTime) + " MS");
    }

}
