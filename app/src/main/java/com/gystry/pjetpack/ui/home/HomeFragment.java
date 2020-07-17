package com.gystry.pjetpack.ui.home;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;

import com.gystry.libnavannotation.FragmentDestination;
import com.gystry.pjetpack.exoplayer.PageListPlayDetector;
import com.gystry.pjetpack.model.Feed;
import com.gystry.pjetpack.ui.AbsListFragment;
import com.gystry.pjetpack.ui.MutablePageKeyedDataSource;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

@FragmentDestination(pageUrl = "main/tabs/home", asStarter = true)
public class HomeFragment extends AbsListFragment<Feed, HomeViewModel> {

    private HomeViewModel homeViewModel;
    private PageListPlayDetector pageListPlayDetector;

    public static HomeFragment newInstance(String feedType) {

        Bundle args = new Bundle();
        args.putString("feedType",feedType);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void afterCreateView() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel.getCacheLiveData().observe(getViewLifecycleOwner(), new Observer<PagedList<Feed>>() {
            @Override
            public void onChanged(PagedList<Feed> feeds) {
                submitList(feeds);
            }
        });
        pageListPlayDetector = new PageListPlayDetector(this, recyclerView);

    }

    @Override
    public PagedListAdapter getAdapter() {
        String feedType = getArguments() == null ? "all" : getArguments().getString("feedType");
        return new FeedAdapter(getContext(), feedType) {
            //检测当列表滑动的时候，内部的item进入和滑出屏幕
            @Override
            public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
                super.onViewAttachedToWindow(holder);
                if (holder.isVideoItem()) {
                    pageListPlayDetector.addTarget(holder.getListPlayerView());
                }
            }

            @Override
            public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
                super.onViewDetachedFromWindow(holder);
                pageListPlayDetector.removeTarget(holder.getListPlayerView());
            }
        };
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        PagedList<Feed> currentList = adapter.getCurrentList();
        if (currentList == null || currentList.size() <= 0) {
            finishRefresh(false);
            return;
        }
        Feed feed = currentList.get(adapter.getItemCount() - 1);
        mViewModel.loadAfter(feed.id, new ItemKeyedDataSource.LoadCallback<Feed>() {
            @Override
            public void onResult(@NonNull List<Feed> data) {
                PagedList.Config config = currentList.getConfig();
                if (data != null && data.size() > 0) {
                    MutablePageKeyedDataSource dataSource = new MutablePageKeyedDataSource<>();
                    dataSource.data.addAll(currentList);
                    dataSource.data.addAll(data);
                    PagedList pagedList = dataSource.buildNewPagedList(config);
                    submitList(pagedList);
                }
            }
        });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        homeViewModel.getDataSource().invalidate();
    }

    @Override
    public void onPause() {
        pageListPlayDetector.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        pageListPlayDetector.onResume();
        super.onResume();
    }
}