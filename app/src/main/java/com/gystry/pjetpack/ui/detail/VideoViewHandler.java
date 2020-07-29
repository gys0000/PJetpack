package com.gystry.pjetpack.ui.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.gystry.pjetpack.R;
import com.gystry.pjetpack.databinding.LayoutFeedDetailTypeVideoBinding;
import com.gystry.pjetpack.databinding.LayoutFeedDetailTypeVideoHeaderBinding;
import com.gystry.pjetpack.model.Feed;

import com.google.android.exoplayer2.ui.PlayerView;
import com.gystry.pjetpack.widget.FullScreenPlayerView;

/**
 * @author gystry
 * 创建日期：2020/7/21 11
 * 邮箱：gystry@163.com
 * 描述：视频详情页的处理类
 */
public class VideoViewHandler extends ViewHandler {

    private final com.gystry.pjetpack.databinding.LayoutFeedDetailBottomInteractionBinding mInteractionBinding;
    private final RecyclerView mRecycleView;
    private final FullScreenPlayerView playerView;
    private final CoordinatorLayout.LayoutParams layoutParams;
    private final CoordinatorLayout coordinator;
    private LayoutFeedDetailTypeVideoBinding mVideoBinding;
    private String category;
    private boolean backPress;

    public VideoViewHandler(FragmentActivity activity) {
        super(activity);

        mVideoBinding = DataBindingUtil.setContentView(activity, R.layout.layout_feed_detail_type_video);
        mInteractionBinding = mVideoBinding.bottomInteraction;
        mRecycleView = mVideoBinding.recyclerView;
        coordinator = mVideoBinding.coordinator;

        View authorInfoView = mVideoBinding.authorInfo.getRoot();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) authorInfoView.getLayoutParams();
        params.setBehavior(new ViewAnchorBehavior(R.id.player_view));

        playerView = mVideoBinding.playerView;

        layoutParams = (CoordinatorLayout.LayoutParams) playerView.getLayoutParams();
        ViewZoomBehavior behavior = (ViewZoomBehavior) layoutParams.getBehavior();
        behavior.setViewZoomCallback(new ViewZoomBehavior.ViewZoomCallback() {
            @Override
            public void onDragZoom(int height) {
                int bottom = playerView.getBottom();
                boolean moveUp = height < bottom;
                boolean fullScreen = moveUp ? height >= coordinator.getBottom() - mInteractionBinding.getRoot().getHeight()
                        : height > coordinator.getBottom();
                setViewApperance(fullScreen);
            }
        });
    }

    @Override
    public void bindInitData(Feed feed) {
        super.bindInitData(feed);
        mVideoBinding.setFeed(feed);

        category = mActivity.getIntent().getStringExtra(FeedDetailActivity.KEY_CATEGORY);
        mVideoBinding.playerView.bindData(category, mFeed.width, mFeed.height, mFeed.cover, mFeed.url);

        mVideoBinding.playerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean fullScreen = mVideoBinding.playerView.getBottom() >= mVideoBinding.coordinator.getBottom();
                setViewApperance(fullScreen);
            }
        }, 50);

        LayoutFeedDetailTypeVideoHeaderBinding headerBinding =
                LayoutFeedDetailTypeVideoHeaderBinding.inflate(
                        LayoutInflater.from(mActivity),
                        mRecycleView,
                        false);
        headerBinding.setFeed(feed);
        listAdapter.addHeaderView(headerBinding.getRoot());
    }

    private void setViewApperance(boolean fullScreen) {
        mVideoBinding.setFullscreen(fullScreen);
        mVideoBinding.fullscreenAuthorInfo.getRoot().setVisibility(fullScreen ? View.VISIBLE : View.GONE);

        int inputHeight = mInteractionBinding.getRoot().getMeasuredHeight();
        int ctrlViewHeight = mVideoBinding.playerView.getPlayController().getMeasuredHeight();
        int bottom = mVideoBinding.playerView.getPlayController().getBottom();
        mVideoBinding.playerView.getPlayController().setY(fullScreen ? bottom - inputHeight - ctrlViewHeight
                : bottom - ctrlViewHeight);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backPress = true;
        mVideoBinding.playerView.getPlayController().setTranslationY(0);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!backPress) {
            mVideoBinding.playerView.inActive();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        backPress = false;
        mVideoBinding.playerView.onActive();


    }
}
