package com.gystry.appkt.ui.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import com.alibaba.fastjson.TypeReference
import com.gystry.appkt.AbsViewModel
import com.gystry.appkt.databinding.LayoutFeedAuthorBinding
import com.gystry.appkt.model.Feed
import com.gystry.appkt.ui.login.UserManager
import com.gystry.libnetworkkt.*
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.collections.ArrayList

class HomeViewModel : AbsViewModel<Feed>() {

    private val loadAfter = AtomicBoolean(false)
    public var mFeedType: String? = null

    @Volatile
    private var withCache = true

    override fun createDataSource(): DataSource<Int, Feed> {
        return mDataSource
    }

    val mDataSource = object : ItemKeyedDataSource<Int, Feed>() {

        override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Feed>) {
            //已经是在子线程了，所以没必要再开一个线程进行同步网络请求
            //加载初始化数据的
            loadData(0, params.requestedLoadSize, callback)
            withCache = false
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Feed>) {
            //加载分页数据
            loadData(params.key, params.requestedLoadSize, callback)
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Feed>) {
            //能够向前加载  比如进入页面的时候加载的是第三页，那么向上翻动的时候可以加载第二页第一页
            //一般用不到，返回一个空的列表
            callback.onResult(Collections.emptyList())
        }

        override fun getKey(item: Feed): Int = item.id
    }

    private fun loadData(key: Int, count: Int, callback: ItemKeyedDataSource.LoadCallback<Feed>) {
        if (key > 0) {
            loadAfter.set(true)
        }
        var request = ApiService.get<ArrayList<Feed>>("/feeds/queryHotFeedsList")
                .addParams("feedType", mFeedType)
                .addParams("userId", UserManager.instance.getUserId())
                .addParams("feedId", key)
                .addParams("pageCount", count)
                .responseType(object : TypeReference<ArrayList<Feed?>?>() {}.type)
        if (withCache) {
            request.cacheStrategy(CACHE_ONLY)
            //请求缓存数据的时候，开启一个新的线程，这样就不会阻塞接口的请求
            request.execute(object : JsonCallback<ArrayList<Feed>>() {
                override fun onCacheSuccess(response: ApiResponse<ArrayList<Feed>>) {
                    Log.d("TAG", "onCacheSuccess: ${response.body?.size}")
                    if (response.body != null) {

                    }
                }
            })
        }
        //上边request设置了cacheStrategy为只用缓存的，那么使用了缓存之后我们还要请求网络更新缓存，
        //那么就需要重新建立一个请求网络的request，但是因为和上边request的设置都一样，只是改了cacheStrategy
        //所以进行clone一下就行了
        val netRequest = if (withCache) request.clone() else request
        netRequest.cacheStrategy(if (key == 0) NET_CACHE else NET_ONLY)
        var response:ApiResponse<List<Feed>> = netRequest.execute() as ApiResponse<List<Feed>>
        val data:List<Feed> =if (response.body==null) Collections.emptyList() else response.body as List<Feed>
        callback.onResult(data)
        if (key>0){
            boundaryPageData.postValue(data.isNotEmpty())
        }
    }

    @SuppressLint("RestrictedApi")
    private fun loadAfter(id: Int, callback: ItemKeyedDataSource.LoadCallback<Feed>) {
        if (loadAfter.get()) {
            callback.onResult(Collections.emptyList())
        }
        ArchTaskExecutor.getIOThreadExecutor().execute {
            loadData(id, config.pageSize, callback)
        }
    }

}