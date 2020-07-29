package com.gystry.pjetpack.ui.detail;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.gystry.libcommon.PixUtils;
import com.gystry.pjetpack.R;

/**
 * 范型中的view指的是被应用的view
 */
public class ViewAnchorBehavior extends CoordinatorLayout.Behavior<View> {

    private int anchorId;
    private int extraUsed;

    public ViewAnchorBehavior(int player_view) {
        anchorId=player_view;
        extraUsed = PixUtils.dp2px(48);
    }

    /**
     * 这个构造函数一定要重写，不然在布局中使用就没有效果了
     *
     * @param context
     * @param attrs
     */
    public ViewAnchorBehavior(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.view_anchor_behavior, 0, 0);
        anchorId = typedArray.getResourceId(R.styleable.view_anchor_behavior_anchorId, 0);
        typedArray.recycle();

        extraUsed = PixUtils.dp2px(48);
    }

    /**
     *
     * @param parent 根布局
     * @param child 当前的view，也就是被应用了当前behavior的view
     * @param dependency 依赖的view
     * @return
     */
    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        return anchorId==dependency.getId();
    }

    /**
     * CoordinatorLayout 在测量每一个view的时候，会调用这个方法，如果返回true，
     * CoordinatorLayout就不会再次测量child了，会使用咱们给的测量的值去摆放view的位置
     * @param parent 根布局
     * @param child 应用了behavior的控件
     * @param parentWidthMeasureSpec 根布局的宽度
     * @param widthUsed 在横向上布局了多少控件
     * @param parentHeightMeasureSpec 根布局的高度
     * @param heightUsed 在垂直方向上摆放了多少控件
     * @return
     */
    @Override
    public boolean onMeasureChild(@NonNull CoordinatorLayout parent,
                                  @NonNull View child,
                                  int parentWidthMeasureSpec,
                                  int widthUsed,
                                  int parentHeightMeasureSpec,
                                  int heightUsed) {

        View anchorView = parent.findViewById(anchorId);
        if (anchorView==null) {
            return false;
        }

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        int topMargin = layoutParams.topMargin;
        int bottom = anchorView.getBottom();
        heightUsed=bottom+topMargin+extraUsed;
        parent.onMeasureChild(child,parentWidthMeasureSpec,0,parentHeightMeasureSpec,heightUsed);

        return true;
    }

    /**
     * 当CoordinatorLayout在摆放每一个子view的时候回调该方法，
     * 如果return true  CoordinatorLayout就不会再次拜访这个view的位置了
     *
     * @param parent
     * @param child
     * @param layoutDirection
     * @return
     */
    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull View child, int layoutDirection) {
        View anchorView = parent.findViewById(anchorId);
        if (anchorView==null) {
            return false;
        }
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        int topMargin = params.topMargin;
        int bottom = anchorView.getBottom();
        parent.onLayoutChild(child,layoutDirection);
        child.offsetTopAndBottom(bottom+topMargin);
        return true;
    }
}
