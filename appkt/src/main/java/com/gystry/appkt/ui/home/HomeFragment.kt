package com.gystry.appkt.ui.home

import androidx.paging.PagedListAdapter
import com.gystry.appkt.model.Feed
import com.gystry.appkt.ui.AbsListFragment
import com.gystry.libnavannotation.FragmentDestination
import com.scwang.smartrefresh.layout.api.RefreshLayout

@FragmentDestination(pageUrl = "main/tabs/home", asStarter = true)
class HomeFragment : AbsListFragment<Feed, HomeViewModel,FeedAdapter.ViewHolder>() {

    //这是在newwork上的第一次提交 时间是15：42
    override fun getAdapter(): PagedListAdapter<Feed, FeedAdapter.ViewHolder> {
      val feedType=  if(arguments==null) "all" else requireArguments().getString("fedType")
       return FeedAdapter(context, feedType!!)
    }

    //这是在newwork上的第二次提交 时间是15：48
    override fun onLoadMore(refreshLayout: RefreshLayout) {
        super.onLoadMore(refreshLayout)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        super.onRefresh(refreshLayout)
    }

    override fun afterCreateView() {
    }


}
