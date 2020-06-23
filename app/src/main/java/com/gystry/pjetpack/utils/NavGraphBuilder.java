package com.gystry.pjetpack.utils;

import android.content.ComponentName;

import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;

import com.gystry.pjetpack.model.Destination;

import java.util.HashMap;

/**
 * @author gystry
 * 创建日期：2020/6/23 17
 * 邮箱：gystry@163.com
 * 描述：
 */
public class NavGraphBuilder {
    public static void build(NavController controller) {
        NavigatorProvider navigatorProvider = controller.getNavigatorProvider();
        FragmentNavigator fragmentNavigator = navigatorProvider.getNavigator(FragmentNavigator.class);
        ActivityNavigator activityNavigator = navigatorProvider.getNavigator(ActivityNavigator.class);
        final NavGraph navGraph = new NavGraph(new NavGraphNavigator(navigatorProvider));

        HashMap<String, Destination> destConfig = AppConfig.getDestConfig();
        for (Destination value : destConfig.values()) {
            if (value.isFragment) {
                final FragmentNavigator.Destination destination = fragmentNavigator.createDestination();
                destination.setClassName(value.claszName);
                destination.setId(value.id);
                destination.addDeepLink(value.pageUrl);
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
