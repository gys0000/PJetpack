package com.gystry.pjetpack.exoplayer;

import android.graphics.Point;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gystry
 * 创建日期：2020/7/17 11
 * 邮箱：gystry@163.com
 * 描述：
 */
public class PageListPlayDetector {

    private List<IPlayTarget> mTarget = new ArrayList<>();
    private RecyclerView recyclerView;
    private IPlayTarget playingTarget;

    public void addTarget(IPlayTarget target) {
        mTarget.add(target);
    }

    public void removeTarget(IPlayTarget target) {
        mTarget.remove(target);
    }

    private final RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            autoPlay();
        }
    };

    public PageListPlayDetector(LifecycleOwner owner, RecyclerView recyclerView) {
        this.recyclerView = recyclerView;

        owner.getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    recyclerView.getAdapter().unregisterAdapterDataObserver(adapterDataObserver);
                    owner.getLifecycle().removeObserver(this);
                }
            }
        });
        //recycleView监听有新的item添加在recycleview上
        recyclerView.getAdapter().registerAdapterDataObserver(adapterDataObserver);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                //当滚动停止之后
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    autoPlay();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (playingTarget!=null&&playingTarget.isPlaying()&isTargetInBounds(playingTarget)) {
                    playingTarget.inActive();
                }
            }
        });
    }


    /**
     * 自动播放
     */
    private void autoPlay() {
        if (mTarget.size() <= 0 && recyclerView.getChildCount() <= 0) {
            return;
        }

        //如果上一个正在播放并且正处于屏幕内，就没必要再开启
        if (playingTarget!=null&&playingTarget.isPlaying()&&isTargetInBounds(playingTarget)) {
            return;
        }

        IPlayTarget activeTarget = null;
        for (IPlayTarget playTarget : mTarget) {
            //判断playview是否一半在屏幕上
            boolean inBounds = isTargetInBounds(playTarget);
            if (inBounds) {
                activeTarget = playTarget;
                break;
            }
        }
        //如果经过循环之后，有正在屏幕上的视频控件，那么就全局保存
        if (activeTarget != null) {
            if (playingTarget != null && playingTarget.isPlaying()) {
                playingTarget.inActive();
            }
            playingTarget = activeTarget;
            playingTarget.onActive();
        }
    }

    private boolean isTargetInBounds(IPlayTarget playTarget) {
        ensureRecycleViewLocation();
        ViewGroup owner = playTarget.getOwner();
        int[] location = new int[2];
        owner.getLocationOnScreen(location);
        int center = location[1] + owner.getHeight() / 2;
        return center > recyclePoint.x && center <= recyclePoint.y;
    }

    private Point recyclePoint;

    private Point ensureRecycleViewLocation() {
        if (recyclePoint == null) {
            int[] location = new int[2];
            recyclerView.getLocationOnScreen(location);
            int top = location[1];
            int bottom = top + recyclerView.getHeight();
            recyclePoint = new Point(top, bottom);
        }
        return recyclePoint;
    }

    public void onPause() {
        if (playingTarget!=null) {
            playingTarget.inActive();
        }
    }

    public void onResume() {
        if (playingTarget!=null) {
            playingTarget.onActive();
        }
    }
}
