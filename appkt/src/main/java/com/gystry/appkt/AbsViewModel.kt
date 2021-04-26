package com.gystry.appkt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.gystry.appkt.model.Feed

/**
 * @author gystry
 * 创建日期：2021/4/1 17
 * 邮箱：gystry@163.com
 * 描述：
 */
abstract class AbsViewModel<T> : ViewModel() {
    var createDataSource: DataSource<Int, T>? = null
    val boundaryPageData
        get() = MutableLiveData<Boolean>()
    val cacheDataSource
        get() = MutableLiveData<PagedList<Feed>>()
    protected val config: PagedList.Config by lazy {
        PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(12)
                .build()
    }

    //监听pagelist数据加载的状态
    private val boundaryCallback: PagedList.BoundaryCallback<T> by lazy {
        object : PagedList.BoundaryCallback<T>() {
            override fun onZeroItemsLoaded() {
                //从PagedList的数据源的初始加载返回零项时被调用
                boundaryPageData.postValue(false)
            }

            override fun onItemAtFrontLoaded(itemAtFront: T) {
                //当列表的第一条数据被加载的时候会被调用
                boundaryPageData.postValue(true)
            }

            override fun onItemAtEndLoaded(itemAtEnd: T) {
                //当pageList的最后一条数据被加载到屏幕上的时候，这个方法会被调用
                super.onItemAtEndLoaded(itemAtEnd)
            }
        }
    }

    private val factory: DataSource.Factory<Int, T> by lazy {
        object : DataSource.Factory<Int, T>() {
            override fun create(): DataSource<Int, T> {
                createDataSource = createDataSource()
                return createDataSource as DataSource<Int, T>
            }
        }
    }

    fun getDataSource():DataSource<Int, T>?{
        return createDataSource
    }

    val pageData: LiveData<PagedList<T>>
        get() = LivePagedListBuilder(factory, config)
                .setInitialLoadKey(0)//加载初始化数据的时候需要传递的数据
                .setBoundaryCallback(boundaryCallback)//
                .build()


    abstract fun createDataSource(): DataSource<Int, T>
}