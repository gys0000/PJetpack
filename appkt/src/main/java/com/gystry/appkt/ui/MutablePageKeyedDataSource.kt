package com.gystry.appkt.ui

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import java.util.*
import kotlin.collections.ArrayList

class MutablePageKeyedDataSource<Value> :PageKeyedDataSource<Int,Value>() {
    public val data:MutableList<Value> =ArrayList<Value>()

    @SuppressLint("RestrictedApi")
    public fun buildNewPagedList(config:PagedList.Config):PagedList<Value>{
        return PagedList.Builder<Int,Value>(this,config)
                .setFetchExecutor(ArchTaskExecutor.getIOThreadExecutor())
                .setNotifyExecutor(ArchTaskExecutor.getMainThreadExecutor())
                .build()
    }
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Value>) {
        callback.onResult(data,null,null)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Value>) {
        callback.onResult(Collections.emptyList(),null)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Value>) {
        callback.onResult(Collections.emptyList(),null)
    }

}