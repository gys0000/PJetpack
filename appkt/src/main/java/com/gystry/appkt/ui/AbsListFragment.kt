package com.gystry.appkt.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gystry.appkt.databinding.LayoutRefreshViewBinding
import com.gystry.libcommon.EmptyView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener

/**
 * @author gystry
 * 创建日期：2021/3/31 10
 * 邮箱：gystry@163.com
 * 描述：
 */
abstract class AbsListFragment<T> : Fragment(), OnLoadMoreListener, OnRefreshListener {
    private lateinit var adapter: PagedListAdapter<T, RecyclerView.ViewHolder>
    protected lateinit var binding: LayoutRefreshViewBinding
    protected lateinit var recyclerView: RecyclerView
    protected lateinit var refreshLayout: SmartRefreshLayout
    protected lateinit var emptyView: EmptyView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = LayoutRefreshViewBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        refreshLayout = binding.refreshLayout
        emptyView = binding.emptyView

        refreshLayout.setEnableLoadMore(true)
        refreshLayout.setEnableRefresh(true)
        refreshLayout.setOnLoadMoreListener(this)
        refreshLayout.setOnRefreshListener(this)

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.itemAnimator = null
        adapter = getAdapter()
        recyclerView.adapter = adapter
        return binding.root
    }

    open fun submitList(pageList: PagedList<T>) {
        if (pageList.size > 0) {
            adapter.submitList(pageList)
        }
        finishRefresh(pageList.size > 0)
    }

    open fun finishRefresh(hasData: Boolean) {
        var hasData = hasData
        val currentList = adapter.currentList
        hasData = hasData || (currentList != null && currentList.size > 0)
        var state = refreshLayout.state
        if (state.isFooter && state.isOpening) {
            refreshLayout.finishLoadMore()
        } else if (state.isHeader && state.isOpening) {
            refreshLayout.finishRefresh()
        }

        if (hasData) {
            emptyView.visibility = View.GONE
        } else {
            emptyView.visibility = View.VISIBLE
        }
    }

    abstract fun getAdapter(): PagedListAdapter<T, RecyclerView.ViewHolder>

    override fun onLoadMore(refreshLayout: RefreshLayout) {
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
    }
}