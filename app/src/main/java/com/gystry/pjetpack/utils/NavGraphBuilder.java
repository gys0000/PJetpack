package com.gystry.pjetpack.utils;

import android.content.ComponentName;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;

import com.gystry.pjetpack.FixFragmentNavigator;
import com.gystry.libcommon.AppGlobal;
import com.gystry.pjetpack.model.Destination;

import java.util.HashMap;

/**
 * @author gystry
 * 创建日期：2020/6/23 17
 * 邮箱：gystry@163.com
 * 描述：
 */
public class NavGraphBuilder {
    public static void build(NavController controller, FragmentActivity activity,int containerId) {
        NavigatorProvider navigatorProvider = controller.getNavigatorProvider();
//        FragmentNavigator fragmentNavigator = navigatorProvider.getNavigator(FragmentNavigator.class);
        FixFragmentNavigator fixFragmentNavigator = new FixFragmentNavigator(activity, activity.getSupportFragmentManager(), containerId);
        ActivityNavigator activityNavigator = navigatorProvider.getNavigator(ActivityNavigator.class);

        navigatorProvider.addNavigator(fixFragmentNavigator);

        final NavGraph navGraph = new NavGraph(new NavGraphNavigator(navigatorProvider));

        HashMap<String, Destination> destConfig = AppConfig.getDestConfig();
        for (Destination value : destConfig.values()) {
            if (value.isFragment) {
                final FragmentNavigator.Destination destination = fixFragmentNavigator.createDestination();
                destination.setClassName(value.claszName);
                destination.setId(value.id);
                destination.addDeepLink(value.pageUrl);
                //destination对象就是一个个页面节点对象，然后destination对象需要添加到NavGraph对象中
                navGraph.addDestination(destination);
            } else {
                final ActivityNavigator.Destination destination = activityNavigator.createDestination();
                destination.setComponentName(new ComponentName(AppGlobal.getApplication().getPackageName(), value.claszName));
                destination.setId(value.id);
                destination.addDeepLink(value.pageUrl);
                navGraph.addDestination(destination);
            }

            if (value.asStater) {
                navGraph.setStartDestination(value.id);
            }
        }
        controller.setGraph(navGraph);
    }
}
