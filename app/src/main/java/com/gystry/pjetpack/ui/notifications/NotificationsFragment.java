package com.gystry.pjetpack.ui.notifications;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.gystry.libnavannotation.FragmentDestination;
import com.gystry.pjetpack.R;
import com.gystry.pjetpack.model.SofaTab;
import com.gystry.pjetpack.ui.dashboard.DashboardFragment;
import com.gystry.pjetpack.utils.AppConfig;

@FragmentDestination(pageUrl = "main/tabs/notification", asStarter = false)
public class NotificationsFragment extends DashboardFragment {

    @Override
    protected Fragment getTabFragment(int position) {
        TagListFragment tagListFragment = TagListFragment.newInstance(getTabConfig().tabs.get(position).tag);
        return tagListFragment;
    }

    @Override
    protected SofaTab getTabConfig() {
        return AppConfig.getFindTab();
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        String tagType = childFragment.getArguments().getString(TagListFragment.KEY_TAG_TYPE);
        if (TextUtils.equals(tagType, "onlyFollow")) {
            ViewModelProviders.of(childFragment).get(TagListViewModel.class)
                    .getSwitchTabLiveData().observe(this, new Observer() {
                @Override
                public void onChanged(Object o) {
                    viewpager.setCurrentItem(1);
                }
            });
        }
    }
}