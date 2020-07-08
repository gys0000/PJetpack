package com.gystry.pjetpack.utils;

/**
 * @author gystry
 * 创建日期：2020/7/8 14
 * 邮箱：gystry@163.com
 * 描述：
 */
public class StringConvert {
    public static String convertFeedUgc(int count) {
        if (count < 10000) {
            return String.valueOf(count);
        }
        return count / 10000 + "万";
    }
}
