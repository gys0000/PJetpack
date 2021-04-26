package com.gystry.appkt.ui.home

import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import com.gystry.appkt.model.Feed
import com.gystry.appkt.ui.AbsListFragment
import com.gystry.appkt.ui.MutablePageKeyedDataSource
import com.gystry.libnavannotation.FragmentDestination
import com.scwang.smartrefresh.layout.api.RefreshLayout

@FragmentDestination(pageUrl = "main/tabs/home", asStarter = true)
class HomeFragment : AbsListFragment<Feed, HomeViewModel, FeedAdapter.ViewHolder>() {

    override fun getAdapter(): PagedListAdapter<Feed, FeedAdapter.ViewHolder> {
        val feedType = if (arguments == null) "all" else requireArguments().getString("fedType")
        return FeedAdapter(context, feedType!!)
    }
    override fun onLoadMore(refreshLayout: RefreshLayout) {
        val feed = getAdapter().currentList?.get(getAdapter().itemCount - 1)
        mViewModel.loadAfter(feed?.id!!,object :ItemKeyedDataSource.LoadCallback<Feed>(){
            override fun onResult(data: MutableList<Feed>) {
                val config = getAdapter().currentList?.config
                if (data!=null&&data.size>=0) {
            val dataSource=MutablePageKeyedDataSource<Feed>()
                    dataSource.data.addAll(data)
                    val buildNewPagedList = dataSource.buildNewPagedList(config!!)
                    mViewModel.cacheLiveData.postValue(buildNewPagedList)
                }
            }

        })
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        //当下拉刷新的时候，调用datasource的invalidate
        mViewModel.getDataSource()?.invalidate()
    }

    override fun afterCreateView() {
        mViewModel.cacheLiveData.observe(this){
            getAdapter().submitList(it)
        }
    }

}
//提交一次
//提交two次
//提交3次