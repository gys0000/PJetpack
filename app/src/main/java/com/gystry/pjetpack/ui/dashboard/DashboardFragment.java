package com.gystry.pjetpack.ui.dashboard;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gystry.libnavannotation.FragmentDestination;
import com.gystry.pjetpack.R;
import com.gystry.pjetpack.databinding.FragmentDashboardBinding;
import com.gystry.pjetpack.model.SofaTab;
import com.gystry.pjetpack.ui.home.HomeFragment;
import com.gystry.pjetpack.utils.AppConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@FragmentDestination(pageUrl = "main/tabs/dash", asStarter = false)
public class DashboardFragment extends Fragment {


    private FragmentDashboardBinding dashboardBinding;
    private ViewPager2 viewpager;
    private TabLayout tabLayout;
    private SofaTab tabConfig;
    private List<SofaTab.TabsBean> tabs;
    private HashMap<Integer, Fragment> mFragmentMap = new HashMap<>();
    private TabLayoutMediator tabLayoutMediator;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false);

        return dashboardBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewpager = dashboardBinding.viewpager;
        tabLayout = dashboardBinding.tabLayout;
        tabConfig = getTabConfig();
        tabs = new ArrayList<>();
        for (SofaTab.TabsBean tab : tabConfig.tabs) {
            if (tab.enable) {
                tabs.add(tab);
            }
        }
        viewpager.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);

        viewpager.setAdapter(new FragmentStateAdapter(getChildFragmentManager(), this.getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                Fragment fragment = mFragmentMap.get(position);
                if (fragment == null) {
                    fragment = getTabFragment(position);
                }
                return fragment;
            }

            @Override
            public int getItemCount() {
                return tabs.size();
            }
        });

        tabLayout.setTabGravity(tabConfig.tabGravity);

        tabLayoutMediator = new TabLayoutMediator(tabLayout, viewpager, false, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setCustomView(makeTabView(position));
            }
        });
        tabLayoutMediator.attach();

        viewpager.registerOnPageChangeCallback(onPageChangeCallback);
        viewpager.post(new Runnable() {
            @Override
            public void run() {
                viewpager.setCurrentItem(tabConfig.select,false);
            }
        });
    }

    ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            int tabCount = tabLayout.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                TextView tabTextView = (TextView) tab.getCustomView();
                if (tab.getPosition() == position) {
                    //当前选中的tab
                    tabTextView.setTextSize(tabConfig.activeSize);
                    tabTextView.setTypeface(Typeface.DEFAULT_BOLD);

                } else {
                    tabTextView.setTextSize(tabConfig.normalSize);
                    tabTextView.setTypeface(Typeface.DEFAULT);
                }
            }
        }
    };

    private View makeTabView(int position) {
        TextView tabView = new TextView(getContext());
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{};

        int[] colors = {Color.parseColor(tabConfig.activeColor), Color.parseColor(tabConfig.normalColor)};
        ColorStateList stateList = new ColorStateList(states, colors);
        tabView.setTextColor(stateList);
        tabView.setText(tabs.get(position).title);
        tabView.setTextSize(tabConfig.normalSize);
        return tabView;
    }

    private Fragment getTabFragment(int position) {
        return HomeFragment.newInstance(tabs.get(position).tag);
    }

    private SofaTab getTabConfig() {
        return AppConfig.getSofaTab();
    }

    @Override
    public void onDestroy() {
        tabLayoutMediator.detach();
        viewpager.unregisterOnPageChangeCallback(onPageChangeCallback);
        super.onDestroy();
    }
}