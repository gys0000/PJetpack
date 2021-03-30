package com.gystry.appkt.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.gystry.appkt.R
import com.gystry.appkt.model.BottomBar
import com.gystry.appkt.utils.getBottomBar
import com.gystry.appkt.utils.getPageId
import com.gystry.libcommon.PixUtils

/**
 * @author gystry
 * 创建日期：2021/3/29 16
 * 邮箱：gystry@163.com
 * 描述：
 */
@SuppressLint("RestrictedApi")
//class AppBottomBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BottomNavigationView(context) {
class AppBottomBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BottomNavigationView(context,attrs,defStyleAttr) {

    companion object {
        private val sIcons = intArrayOf(R.drawable.icon_tab_home, R.drawable.icon_tab_sofa, R.drawable.icon_tab_publish, R.drawable.icon_tab_find, R.drawable.icon_tab_mine)
    }

     init {
        val bottomBar = getBottomBar()
        val tabs = bottomBar.tabs
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(android.R.attr.state_selected)
        states[1] = intArrayOf()
        val colors = intArrayOf(Color.parseColor(bottomBar.activeColor), Color.parseColor(bottomBar.inActiveColor))
        val colorStateList = ColorStateList(states, colors)
        itemIconTintList = colorStateList
        itemTextColor = colorStateList
        labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        selectedItemId = bottomBar.selectTab

        for (i in tabs.indices) {
            val tab: BottomBar.Tabs = tabs[i]
            if (!tab.enable) {
                continue
            }
            //将页面对应的id付值给menuItem，那么我们就可以用过menu来对页面进行操作
            val id = tab.pageUrl.let { getPageId(it) }
            if (id < 0) {
                continue
            }
            val item: MenuItem = menu.add(0, id, tab.index, tab.title)
            //设置icon
            item.setIcon(sIcons[tab.index])
        }

        // 设置按钮大小  然后给中间的大按钮设置颜色，然后设置点击不会有上下浮动
        for (i in tabs.indices) {
            val tab: BottomBar.Tabs = tabs[i]
            val iconSize: Int = PixUtils.dp2px(tab.size)
            val childAt = getChildAt(0) as BottomNavigationMenuView
            val child = childAt.getChildAt(tab.index) as BottomNavigationItemView
            child.setIconSize(iconSize)
            if (TextUtils.isEmpty(tab.title)) {
                child.setIconTintList(ColorStateList.valueOf(Color.parseColor(tab.tintColor)))
                //不让按钮点击的时候有上下浮动的效果
                child.setShifting(false)
            }
        }
    }

}