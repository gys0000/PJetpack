package com.gystry.pjetpack.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.gystry.pjetpack.R;
import com.gystry.pjetpack.model.BottomBar;
import com.gystry.pjetpack.model.Destination;
import com.gystry.pjetpack.utils.AppConfig;

import java.util.HashMap;
import java.util.List;

public class AppBottomBar extends BottomNavigationView {

    private static int[] sIcons=new  int[]{R.drawable.icon_tab_home,R.drawable.icon_tab_sofa,R.drawable.icon_tab_publish,R.drawable.icon_tab_find,R.drawable.icon_tab_mine};
    public AppBottomBar(@NonNull Context context) {
        this(context, null);
    }

    public AppBottomBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public AppBottomBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        BottomBar bottomBar = AppConfig.getBottomBar();
        List<BottomBar.Tabs> tabs = bottomBar.tabs;
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{};

        int[] colors = {Color.parseColor(bottomBar.activeColor), Color.parseColor(bottomBar.inActiveColor)};
        ColorStateList colorStateList = new ColorStateList(states, colors);
        setItemIconTintList(colorStateList);
        setItemTextColor(colorStateList);
        setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        setSelectedItemId(bottomBar.selectTab);

        for (int i = 0; i < tabs.size(); i++) {
            BottomBar.Tabs tab = tabs.get(i);
            if (!tab.enable) {
                return;
            }
            //将页面对应的id付值给menuItem，那么我们就可以用过menu来对页面进行操作
            int id = getId(tab.pageUrl);
            if (id < 0) {
                return;
            }
            MenuItem item = getMenu().add(0, id, tab.index, tab.title);
            //设置icon
            item.setIcon(sIcons[tab.index]);
        }

        // 设置按钮大小  然后给中间的大按钮设置颜色，然后设置点击不会有上下浮动
        for (int i = 0; i < tabs.size(); i++) {
            BottomBar.Tabs tab = tabs.get(i);
            int iconSize = dp2px(tab.size);

            BottomNavigationMenuView childAt = (BottomNavigationMenuView) getChildAt(0);
            BottomNavigationItemView child = (BottomNavigationItemView) childAt.getChildAt(tab.index);
            child.setIconSize(iconSize);

            if (TextUtils.isEmpty(tab.title)) {
                child.setIconTintList(ColorStateList.valueOf(Color.parseColor(tab.tintColor)));
                child.setShifting(false);
            }
        }

    }

    private int dp2px(int size){
        float v = getContext().getResources().getDisplayMetrics().density * size + 0.5f;
        return (int) v;
    }

    private int getId(String pageUrl) {
        HashMap<String, Destination> destConfig = AppConfig.getDestConfig();
        Destination destination = destConfig.get(pageUrl);
        if (destination == null) {
            return -1;
        }
        return destination.id;
    }
}
