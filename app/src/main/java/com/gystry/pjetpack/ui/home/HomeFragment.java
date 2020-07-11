package com.gystry.pjetpack.ui.home;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;

import com.gystry.libnavannotation.FragmentDestination;
import com.gystry.pjetpack.model.Feed;
import com.gystry.pjetpack.ui.AbsListFragment;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

@FragmentDestination(pageUrl = "main/tabs/home", asStarter = true)
public class HomeFragment extends AbsListFragment<Feed, HomeViewModel> {

    private HomeViewModel homeViewModel;

    @Override
    protected void afterCreateView() {

    }

    @Override
    public PagedListAdapter getAdapter() {
        String feedType = getArguments() == null ? "all" : getArguments().getString("feedType");
        return new FeedAdapter(getContext(), feedType);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        homeViewModel.getDataSource().invalidate();
    }
}