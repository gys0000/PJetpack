package com.gystry.pjetpack.ui.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;

import com.gystry.pjetpack.exoplayer.PageListPlayDetector;
import com.gystry.pjetpack.exoplayer.PageListPlayManager;
import com.gystry.pjetpack.model.Feed;
import com.gystry.pjetpack.ui.AbsListFragment;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

/**
 * @author gystry
 * 创建日期：2020/8/12 17
 * 邮箱：gystry@163.com
 * 描述：
 */
public class ProfileListFragment extends AbsListFragment<Feed, ProfileViewModel> {

    private PageListPlayDetector playDetector;
    private String tabType;
    private boolean shouldPause;

    public static ProfileListFragment newInstance(String tabType) {

        Bundle args = new Bundle();
        args.putString(ProfileActivity.KEY_TAB_TYPE, tabType);
        ProfileListFragment fragment = new ProfileListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playDetector = new PageListPlayDetector(this, recyclerView);
        mViewModel.setProfileType(tabType);
        refreshLayout.setEnableRefresh(false);
    }

    @Override
    public PagedListAdapter getAdapter() {
        tabType = getArguments().getString(ProfileActivity.KEY_TAB_TYPE);
        return new ProfileListAdapter(getContext(),tabType){
            @Override
            public void onViewDetachedFromWindow2(ViewHolder holder) {
                if (holder.isVideoItem()) {
                    playDetector.removeTarget(holder.listPlayerView);
                }
            }

            @Override
            public void onViewAttachedToWindow2(ViewHolder holder) {
                if (holder.isVideoItem()) {
                    playDetector.addTarget(holder.listPlayerView);
                }
            }

            @Override
            public void onStartFeedDetailActivity(Feed feed) {
                shouldPause = false;
            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        if (shouldPause) {
            playDetector.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        shouldPause = true;
        //从评论tab页跳转到 详情页之后再返回回来，咱们需要暂停视频播放。因为评论和tab页是没有视频的
        if (TextUtils.equals(tabType, ProfileActivity.TAB_TYPE_COMMENT)) {
            playDetector.onPause();
        } else {
            playDetector.onResume();
        }
    }

    @Override
    public void onDestroyView() {
        PageListPlayManager.release(tabType);
        super.onDestroyView();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        PagedList<Feed> currentList = adapter.getCurrentList();
        finishRefresh(currentList != null && currentList.size() > 0);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }
}
