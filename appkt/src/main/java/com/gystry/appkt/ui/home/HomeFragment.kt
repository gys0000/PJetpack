package com.gystry.appkt.ui.home

import androidx.paging.PagedListAdapter
import com.gystry.appkt.model.Feed
import com.gystry.appkt.ui.AbsListFragment
import com.gystry.libnavannotation.FragmentDestination
import com.scwang.smartrefresh.layout.api.RefreshLayout

@FragmentDestination(pageUrl = "main/tabs/home", asStarter = true)
class HomeFragment : AbsListFragment<Feed, HomeViewModel, FeedAdapter.ViewHolder>() {


    override fun getAdapter(): PagedListAdapter<Feed, FeedAdapter.ViewHolder> {
        val feedType = if (arguments == null) "all" else requireArguments().getString("fedType")
        return FeedAdapter(context, feedType!!)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        super.onLoadMore(refreshLayout)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        super.onRefresh(refreshLayout)
    }
    //这是master上的第二次提交   时间大约在16：20

    override fun afterCreateView() {
    }

    //这是master上的第一次提交   时间大约在16：18

}
