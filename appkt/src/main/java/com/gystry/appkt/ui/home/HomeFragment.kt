package com.gystry.appkt.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gystry.appkt.R
import com.gystry.appkt.model.Feed
import com.gystry.appkt.ui.AbsListFragment
import com.gystry.libnavannotation.FragmentDestination
import com.scwang.smartrefresh.layout.api.RefreshLayout

@FragmentDestination(pageUrl = "main/tabs/home",asStarter = true)
class HomeFragment : AbsListFragment<Feed>() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun getAdapter(): PagedListAdapter<Feed, RecyclerView.ViewHolder> {
       return Any() as PagedListAdapter<Feed, RecyclerView.ViewHolder>
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        super.onLoadMore(refreshLayout)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        super.onRefresh(refreshLayout)
    }


}
