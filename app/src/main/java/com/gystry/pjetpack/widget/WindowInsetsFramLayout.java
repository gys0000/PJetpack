package com.gystry.pjetpack.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class WindowInsetsFramLayout  extends FrameLayout {
    public WindowInsetsFramLayout(@NonNull Context context) {
        super(context);
    }

    public WindowInsetsFramLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WindowInsetsFramLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        requestApplyInsets();
    }

    @Override
    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        return super.dispatchApplyWindowInsets(insets);
    }
}
