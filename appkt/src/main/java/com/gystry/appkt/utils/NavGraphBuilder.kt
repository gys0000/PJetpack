package com.gystry.appkt.utils

import android.app.Activity
import android.content.ComponentName
import androidx.fragment.app.FragmentActivity
import androidx.navigation.*
import androidx.navigation.fragment.FragmentNavigator
import com.gystry.appkt.FixFragmentNavigator
import com.gystry.libcommon.AppGlobal
import java.lang.Appendable

/**
 * @author gystry
 * 创建日期：2021/3/29 15
 * 邮箱：gystry@163.com
 * 描述：
 */
object NavGraphBuilder {
    fun build(controller:NavController, activity: FragmentActivity,containerId:Int){
        val navigatorProvider = controller.navigatorProvider
//        val fragmentNavigator = navigatorProvider.getNavigator(FragmentNavigator::class.java)
        val fixFragmentNavigator = FixFragmentNavigator(activity, activity.supportFragmentManager, containerId)
        val activityNavigator = navigatorProvider.getNavigator(ActivityNavigator::class.java)

        navigatorProvider.addNavigator(fixFragmentNavigator)
        val navGraph = NavGraph(NavGraphNavigator(navigatorProvider))
        val destConfig = getDestConfig()

        for (value in destConfig.values) {
            if (value.isFragment) {
                val destination = fixFragmentNavigator.createDestination()
                destination.className=value.claszName
                destination.id=value.id
                destination.addDeepLink(value.pageUrl)
                navGraph.addDestination(destination)
            }else{
                val destination = activityNavigator.createDestination()
                destination.setComponentName(ComponentName(AppGlobal.getApplication().packageName,value.claszName))
                destination.id=value.id
                destination.addDeepLink(value.pageUrl)
                navGraph.addDestination(destination)
            }

            if (value.asStater) {
                navGraph.startDestination=value.id
            }
        }

        controller.graph=navGraph
    }
}