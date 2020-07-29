package com.gystry.pjetpack.ui.detail;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.OverScroller;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import com.gystry.libcommon.PixUtils;
import com.gystry.pjetpack.R;
import com.gystry.pjetpack.widget.FullScreenPlayerView;

/**
 * @author gystry
 * 创建日期：2020/7/29 14
 * 邮箱：gystry@163.com
 * 描述：
 */
public class ViewZoomBehavior extends CoordinatorLayout.Behavior<FullScreenPlayerView> {

    private int minHeight;
    private int scrollingId;
    private FullScreenPlayerView refChild;
    private View mScrollingView;
    private int childOriginalHeight;
    private boolean canFullScreen;
    private ViewDragHelper viewDragHelper;
    private OverScroller overScroller;
    private ViewZoomCallback mViewZoomCallback;

    public ViewZoomBehavior() {
    }

    public ViewZoomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.view_zoom_behavior);
        scrollingId = typedArray.getResourceId(R.styleable.view_zoom_behavior_scrolling_id, 0);
        minHeight = typedArray.getDimensionPixelOffset(R.styleable.view_zoom_behavior_min_height, PixUtils.dp2px(200));
        typedArray.recycle();

        overScroller = new OverScroller(context);
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent,
                                 @NonNull FullScreenPlayerView child,
                                 int layoutDirection) {
        if (callback == null) {
            viewDragHelper = ViewDragHelper.create(parent, 1.0f, callback);
            refChild = child;
            mScrollingView = parent.findViewById(scrollingId);
            childOriginalHeight = child.getMeasuredHeight();
            canFullScreen = childOriginalHeight > parent.getMeasuredHeight();
        }
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        //
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            if (canFullScreen && refChild.getBottom() >= minHeight) {
                return true;
            }
            return false;
        }

        /**
         * 告诉viewDragHelper 在屏幕上华东多少距离才算拖拽
         * @param child
         * @return
         */
        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            return viewDragHelper.getTouchSlop();
        }

        /**
         * 告诉viewDragHelper 手指拖拽的这个view本次最终能够移动的距离
         * @param child
         * @param top
         * @param dy
         * @return
         */
        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            if (refChild == null || dy == 0) {
                return 0;
            }

            //dy>0 代表手指从屏幕上方往屏幕下方滑动
            //dy<0 代表手指从屏幕下方往屏幕上方滑动

            if (dy < 0 && refChild.getBottom() < minHeight || dy > 0 && refChild.getBottom() > childOriginalHeight
                    || (dy > 0 && (mScrollingView != null && mScrollingView.canScrollVertically(-1)))) {
                return 0;
            }

            int maxConsumed = 0;
            if (dy > 0) {
                if (refChild.getBottom() + dy > childOriginalHeight) {
                    maxConsumed = childOriginalHeight - refChild.getBottom();
                } else {
                    maxConsumed = dy;
                }
            } else {
                if (refChild.getBottom() + dy < minHeight) {
                    maxConsumed = minHeight - refChild.getBottom();
                } else {
                    maxConsumed = dy;
                }
            }

            ViewGroup.LayoutParams layoutParams = refChild.getLayoutParams();
            layoutParams.height = layoutParams.height + maxConsumed;
            refChild.setLayoutParams(layoutParams);
            if (mViewZoomCallback != null) {
                mViewZoomCallback.onDragZoom(layoutParams.height);
            }
            return maxConsumed;
        }

        /**
         * 手指从屏幕上离开的时候会被调用
         * @param releasedChild
         * @param xvel
         * @param yvel
         */
        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (refChild.getBottom() > minHeight && refChild.getBottom() > childOriginalHeight && yvel != 0) {
                FlingRunnable flingRunnable = new FlingRunnable(refChild);
                flingRunnable.fling((int) xvel, (int) yvel);
            }
        }
    };

    @Override
    public boolean onTouchEvent(@NonNull CoordinatorLayout parent, @NonNull FullScreenPlayerView child, @NonNull MotionEvent ev) {
        if (!canFullScreen || viewDragHelper == null) {
            return super.onTouchEvent(parent, child, ev);
        }
        viewDragHelper.processTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull FullScreenPlayerView child, @NonNull MotionEvent ev) {
        if (!canFullScreen || viewDragHelper == null) {
            return super.onInterceptTouchEvent(parent, child, ev);
        }

        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    public void setViewZoomCallback(ViewZoomCallback viewZoomCallback) {

        mViewZoomCallback = viewZoomCallback;
    }

    public interface ViewZoomCallback {
        void onDragZoom(int height);
    }

    private class FlingRunnable implements Runnable {

        private View flingView;

        public FlingRunnable(View flingView) {
            this.flingView = flingView;
        }

        public void fling(int xVel, int yVel) {
            overScroller.fling(0, flingView.getBottom(), xVel, yVel, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
            run();
        }

        @Override
        public void run() {
            ViewGroup.LayoutParams layoutParams = flingView.getLayoutParams();
            int height = layoutParams.height;
            if (overScroller.computeScrollOffset() && height >= minHeight && height <= childOriginalHeight) {
                int newHeight = Math.min(overScroller.getCurrY(), childOriginalHeight);
                if (newHeight != height) {
                    layoutParams.height = newHeight;
                    flingView.setLayoutParams(layoutParams);
                    if (mViewZoomCallback != null) {
                        mViewZoomCallback.onDragZoom(newHeight);
                    }
                }
                ViewCompat.postOnAnimation(flingView, this::run);
            } else {
                flingView.removeCallbacks(this::run);
            }
        }
    }
}
