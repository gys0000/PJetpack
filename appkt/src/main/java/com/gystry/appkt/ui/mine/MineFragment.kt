package com.gystry.appkt.ui.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gystry.appkt.R
import com.gystry.appkt.ui.dashboard.DashboardViewModel
import com.gystry.libnavannotation.FragmentDestination

/**
 * @author gystry
 * 创建日期：2021/3/29 10
 * 邮箱：gystry@163.com
 * 描述：
 */
@FragmentDestination(pageUrl = "main/tabs/my",asStarter = false)
class MineFragment : Fragment() {
    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}