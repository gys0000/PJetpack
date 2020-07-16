package com.gystry.libcommon;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author gystry
 * 创建日期：2020/7/16 16
 * 邮箱：gystry@163.com
 * 描述：
 */
public class RoundFramLayout extends FrameLayout {
    public RoundFramLayout(@NonNull Context context) {
        this(context, null);
    }

    public RoundFramLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundFramLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RoundFramLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        ViewHelper.setViewLine(this, attrs, defStyleAttr, defStyleRes);
    }

    public void setViewOutLine(int radius,int radiusSide){
        ViewHelper.setViewLine(this,radius,radiusSide);
    }
}
