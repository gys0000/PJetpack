package com.gystry.pjetpack.ui;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class MutableItemKeyDataSource<Key, Value> extends ItemKeyedDataSource<Key, Value> {
    private ItemKeyedDataSource dataSource;

    public MutableItemKeyDataSource(ItemKeyedDataSource dataSource) {

        this.dataSource = dataSource;
    }

    public List<Value> data = new ArrayList<>();

    public PagedList<Value> buildNewPagedList(PagedList.Config config) {
        @SuppressLint("RestrictedApi") PagedList<Value> pagedList =
                new PagedList.Builder<Key, Value>(this, config)
                        .setFetchExecutor(ArchTaskExecutor.getIOThreadExecutor())
                        .setNotifyExecutor(ArchTaskExecutor.getMainThreadExecutor())
                        .build();
        return pagedList;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Key> params, @NonNull LoadInitialCallback<Value> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Key> params, @NonNull LoadCallback<Value> callback) {
        dataSource.loadAfter(params, callback);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Key> params, @NonNull LoadCallback<Value> callback) {
        callback.onResult(Collections.emptyList());
    }

    @NonNull
    @Override
    public abstract Key getKey(@NonNull Value item);
}
