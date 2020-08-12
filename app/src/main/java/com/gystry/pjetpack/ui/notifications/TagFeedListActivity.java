package com.gystry.pjetpack.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gystry.libcommon.EmptyView;
import com.gystry.libcommon.PixUtils;
import com.gystry.libcommon.extention.AbsPageListAdapter;
import com.gystry.pjetpack.R;
import com.gystry.pjetpack.databinding.ActivityLayoutTagFeedListBinding;
import com.gystry.pjetpack.databinding.LayoutTagFeedListHeaderBinding;
import com.gystry.pjetpack.exoplayer.PageListPlayDetector;
import com.gystry.pjetpack.exoplayer.PageListPlayManager;
import com.gystry.pjetpack.model.Feed;
import com.gystry.pjetpack.model.TagList;
import com.gystry.pjetpack.ui.home.FeedAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * @author gystry
 * 创建日期：2020/8/7 15
 * 邮箱：gystry@163.com
 * 描述：
 */
public class TagFeedListActivity extends AppCompatActivity implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener {
    public static final String KEY_TAG_LIST = "tag_list";
    public static final String KEY_FEED_TYPE = "tag_feed_list";
    private ActivityLayoutTagFeedListBinding binding;
    private RecyclerView recyclerView;
    private EmptyView emptyView;
    private SmartRefreshLayout refreshLayout;
    private AbsPageListAdapter adapter;
    private TagList tagList;
    private PageListPlayDetector playDetector;
    private TagFeedListViewModel tagFeedListViewModel;
    private boolean shouldPause;
    private int totalScrollY;
    public static void startActivity(Context context, TagList tagList) {
        Intent intent = new Intent(context, TagFeedListActivity.class);
        intent.putExtra(KEY_TAG_LIST, tagList);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_layout_tag_feed_list);
        recyclerView = binding.refreshLayout.recyclerView;
        emptyView = binding.refreshLayout.emptyView;
        refreshLayout = binding.refreshLayout.refreshLayout;
        binding.actionBack.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = ((AbsPageListAdapter) getAdapter());
        recyclerView.setAdapter(adapter);
        DividerItemDecoration decoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        decoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.list_divider));
        recyclerView.addItemDecoration(decoration);
        recyclerView.setItemAnimator(null);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);

        tagList = ((TagList) getIntent().getSerializableExtra(KEY_TAG_LIST));
        binding.setTagList(tagList);
        binding.setOwner(this);

        // TODO: 2020/8/7 ViewModel
        tagFeedListViewModel = ViewModelProviders.of(this).get(TagFeedListViewModel.class);
        tagFeedListViewModel.setFeedType(tagList.title);
        tagFeedListViewModel.getPageData().observe(this, feeds -> submitList(feeds));
        tagFeedListViewModel.getBoundaryPageData().observe(this, hasData -> finishRefresh(hasData));

        playDetector = new PageListPlayDetector(this, recyclerView);
        addHeaderView();
    }

    private void submitList(PagedList<Feed> feeds) {
        if (feeds.size() > 0) {
            adapter.submitList(feeds);
        }
        finishRefresh(feeds.size() > 0);
    }

    private void finishRefresh(boolean hasData) {
        PagedList currentList = adapter.getCurrentList();
        hasData = currentList != null && currentList.size() > 0 || hasData;

        if (hasData) {
            emptyView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }

        RefreshState state = refreshLayout.getState();
        if (state.isOpening && state.isHeader) {
            refreshLayout.finishRefresh();
        } else if (state.isOpening && state.isFooter) {
            refreshLayout.finishLoadMore();
        }
    }

    private void addHeaderView() {
        LayoutTagFeedListHeaderBinding headerBinding = LayoutTagFeedListHeaderBinding.inflate(LayoutInflater.from(this), recyclerView, false);
        headerBinding.setTagList(tagList);
        headerBinding.setOwner(this);
        adapter.addHeaderView(headerBinding.getRoot());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalScrollY += dy;
                boolean overHeight = totalScrollY > PixUtils.dp2px(48);
                binding.tagLogo.setVisibility(overHeight ? View.VISIBLE : View.GONE);
                binding.tagTitle.setVisibility(overHeight ? View.VISIBLE : View.GONE);
                binding.topBarFollow.setVisibility(overHeight ? View.VISIBLE : View.GONE);
                binding.actionBack.setImageResource(overHeight ? R.drawable.icon_back_black : R.drawable.icon_back_white);
                binding.topBar.setBackgroundColor(overHeight ? Color.WHITE : Color.TRANSPARENT);
                binding.topLine.setVisibility(overHeight ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (shouldPause) {
            playDetector.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        shouldPause = true;
        playDetector.onResume();
    }

    @Override
    protected void onDestroy() {
        PageListPlayManager.release(KEY_FEED_TYPE);
        super.onDestroy();
    }

    public PagedListAdapter getAdapter() {
        return new FeedAdapter(this, KEY_FEED_TYPE) {
            @Override
            public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
                super.onViewAttachedToWindow(holder);
            }

            @Override
            public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
                super.onViewDetachedFromWindow(holder);
            }

            @Override
            public void onStartFeedDetailActivity(Feed feed) {
                super.onStartFeedDetailActivity(feed);
            }

            @Override
            public void onCurrentListChanged(@Nullable PagedList<Feed> previousList, @Nullable PagedList<Feed> currentList) {
                super.onCurrentListChanged(previousList, currentList);
            }
        };
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        tagFeedListViewModel.getDataSource().invalidate();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        PagedList currentList = getAdapter().getCurrentList();
        finishRefresh(currentList != null && currentList.size() > 0);
    }
}
