package com.gystry.pjetpack.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

public class WindowInsetNavHostFragment extends NavHostFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        WindowInsetsFramLayout windowInsetsFramLayout = new WindowInsetsFramLayout(inflater.getContext());
        windowInsetsFramLayout.setId(getId());
        return windowInsetsFramLayout;
    }
}
