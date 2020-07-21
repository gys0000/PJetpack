package com.gystry.pjetpack.utils;

import java.util.Calendar;

/**
 * @author gystry
 * 创建日期：2020/7/20 10
 * 邮箱：gystry@163.com
 * 描述：
 */
public class TimeUtils {

    public static String caculate(long time) {
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        long diff = (timeInMillis - time) / 1000;
        if (diff < 60) {
            return diff + "秒前";
        } else if (diff < 3600) {
            return diff / 60 + "分钟前";
        } else if (diff < 3600 * 24) {
            return diff / 3600 + "小时前";
        } else {
            return diff / (3600 * 24) + "天前";
        }
    }
}
