package com.gystry.libcommon;

import android.util.DisplayMetrics;

/**
 * @author gystry
 * 创建日期：2020/7/8 16
 * 邮箱：gystry@163.com
 * 描述：
 */
public class PixUtils {

    public static int dp2px(int dpValue) {
        DisplayMetrics metrics = AppGlobal.getApplication().getResources().getDisplayMetrics();
        return (int) (metrics.density * dpValue + 0.5f);
    }

    public static int getScreenWidth() {
        DisplayMetrics metrics = AppGlobal.getApplication().getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static int getScreenHeight() {
        DisplayMetrics metrics = AppGlobal.getApplication().getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }
}
