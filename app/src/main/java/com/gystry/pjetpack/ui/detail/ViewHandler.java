package com.gystry.pjetpack.ui.detail;

import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gystry.libcommon.EmptyView;
import com.gystry.pjetpack.R;
import com.gystry.pjetpack.databinding.LayoutFeedDetailBottomInteractionBinding;
import com.gystry.pjetpack.model.Comment;
import com.gystry.pjetpack.model.Feed;

/**
 * @author gystry
 * 创建日期：2020/7/21 11
 * 邮箱：gystry@163.com
 * 描述：图文详情页和视频详情页相同功能点的开发在这个类里边开发，不同功能点的开发在这个类的子类里边开发
 */
public abstract class ViewHandler {
    private final FeedDetailViewModel viewModel;
    protected FragmentActivity mActivity;
    protected Feed mFeed;
    protected RecyclerView mRecycleView;
    protected LayoutFeedDetailBottomInteractionBinding mInteractionBinding;
    protected FeedCommentAdapter listAdapter;

    public ViewHandler(FragmentActivity activity) {
        mActivity = activity;
        viewModel = ViewModelProviders.of(activity).get(FeedDetailViewModel.class);
    }

    @CallSuper
    public void bindInitData(Feed feed) {
        mInteractionBinding.setOwner(mActivity);
        mFeed = feed;
        mRecycleView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRecycleView.setItemAnimator(null);
        listAdapter = new FeedCommentAdapter();
        mRecycleView.setAdapter(listAdapter);
        viewModel.setItemId(mFeed.itemId);
        viewModel.getPageData().observe(mActivity, new Observer<PagedList<Comment>>() {
            @Override
            public void onChanged(PagedList<Comment> comments) {
                listAdapter.submitList(comments);
                hasEmpty(comments.size() > 0);
            }
        });
    }

    private EmptyView emptyView;

    private void hasEmpty(boolean b) {
        if (b) {
            if (emptyView != null) {
                listAdapter.removeHeaderView(emptyView);
            }
        } else {
            if (emptyView == null) {
                emptyView = new EmptyView(mActivity);
                emptyView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                emptyView.setTitle(mActivity.getString(R.string.feed_comment_empty));
                listAdapter.addHeaderView(emptyView);
            }
        }
    }
}
