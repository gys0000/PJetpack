package com.gystry.appkt.ui.home

import androidx.paging.PagedListAdapter
import com.gystry.appkt.model.Feed
import com.gystry.appkt.ui.AbsListFragment
import com.gystry.libnavannotation.FragmentDestination
import com.scwang.smartrefresh.layout.api.RefreshLayout

@FragmentDestination(pageUrl = "main/tabs/home", asStarter = true)
class HomeFragment : AbsListFragment<Feed, HomeViewModel, FeedAdapter.ViewHolder>() {

    //thirdwork第一次提交
    override fun getAdapter(): PagedListAdapter<Feed, FeedAdapter.ViewHolder> {
        val feedType = if (arguments == null) "all" else requireArguments().getString("fedType")
        return FeedAdapter(context, feedType!!)
    }
    override fun onLoadMore(refreshLayout: RefreshLayout) {
        super.onLoadMore(refreshLayout)
    }
    //thirdwork第二次提交

    //thirdwork第三次提交
    //thirdwork第四次提交

    override fun onRefresh(refreshLayout: RefreshLayout) {
        super.onRefresh(refreshLayout)
    }

    override fun afterCreateView() {
    }


    //这是master上的第三次提交

    //这是master上的第四次提交

}
