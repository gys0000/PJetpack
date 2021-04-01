package com.gystry.appkt.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import com.gystry.appkt.AbsViewModel
import com.gystry.appkt.model.Feed
import java.util.*

class HomeViewModel : AbsViewModel<Feed>() {

    override fun createDataSource(): DataSource<Any, Feed> {
        TODO("Not yet implemented")
    }

    val mDataSource= object :ItemKeyedDataSource<Int,Feed>(){

        override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Feed>) {
            //加载初始化数据的
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Feed>) {
            //加载分页数据
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Feed>) {
            //能够向前加载  比如进入页面的时候加载的是第三页，那么向上翻动的时候可以加载第二页第一页
            //一般用不到，返回一个空的列表
            callback.onResult(Collections.emptyList())
        }

        override fun getKey(item: Feed): Int {
            TODO("Not yet implemented")
        }

    }
}