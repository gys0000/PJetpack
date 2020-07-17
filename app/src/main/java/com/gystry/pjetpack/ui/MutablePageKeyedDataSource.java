package com.gystry.pjetpack.ui;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author gystry
 * 创建日期：2020/7/17 17
 * 邮箱：gystry@163.com
 * 描述：具体原理见 MutableItemKeyedDataSource
 */
public class MutablePageKeyedDataSource<Value> extends PageKeyedDataSource<Integer, Value> {
    public List<Value> data = new ArrayList<>();

    public PagedList<Value> buildNewPagedList(PagedList.Config config) {
        @SuppressLint("RestrictedApi") PagedList<Value> pagedList =
                new PagedList.Builder<Integer, Value>(this, config)
                        .setFetchExecutor(ArchTaskExecutor.getIOThreadExecutor())
                        .setNotifyExecutor(ArchTaskExecutor.getMainThreadExecutor())
                        .build();
        return pagedList;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Value> callback) {
        callback.onResult(data, null, null);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Value> callback) {
        callback.onResult(Collections.emptyList(), null);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Value> callback) {
        callback.onResult(Collections.emptyList(), null);
    }
}
