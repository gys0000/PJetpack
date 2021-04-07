package com.gystry.appkt.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gystry.appkt.AbsViewModel
import com.gystry.appkt.R
import com.gystry.appkt.databinding.LayoutRefreshViewBinding
import com.gystry.libcommon.EmptyView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import java.lang.reflect.ParameterizedType

/**
 * @author gystry
 * 创建日期：2021/3/31 10
 * 邮箱：gystry@163.com
 * 描述：
 */
abstract class AbsListFragment<T, M : AbsViewModel<T>,V:RecyclerView.ViewHolder> : Fragment(), OnLoadMoreListener, OnRefreshListener {
    private lateinit var adapter: PagedListAdapter<T, V>
    protected lateinit var binding: LayoutRefreshViewBinding
    protected lateinit var recyclerView: RecyclerView
    protected lateinit var refreshLayout: SmartRefreshLayout
    protected lateinit var emptyView: EmptyView
    protected lateinit var mViewModel:M

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
        //默认给列表中的Item 一个 10dp的ItemDecoration
       val decoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        decoration.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.list_divider)!!)
        recyclerView.addItemDecoration(decoration)
        recyclerView.adapter = adapter

        afterCreateView()
        return binding.root
    }

    abstract fun afterCreateView()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = javaClass.genericSuperclass as ParameterizedType
        val arguments = type.actualTypeArguments
        if(arguments.size>1){
            val argument = arguments[1]
            val modelClaz = (argument as Class<*>).asSubclass(AbsViewModel::class.java)
            mViewModel = ViewModelProviders.of(this).get(modelClaz) as M
            mViewModel.pageData.observe(viewLifecycleOwner){
                adapter.submitList(it)
            }
            mViewModel.boundaryPageData.observe(viewLifecycleOwner){
                finishRefresh(it)
            }
        }
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

   abstract fun getAdapter(): PagedListAdapter<T, V>

    override fun onLoadMore(refreshLayout: RefreshLayout) {
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
    }
}