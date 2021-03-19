package com.gystry.pjetpack.ui.home;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;

import com.alibaba.fastjson.TypeReference;
import com.gystry.libnetworkkt.ApiResponse;
import com.gystry.libnetworkkt.ApiService;
import com.gystry.libnetworkkt.JsonCallback;
import com.gystry.libnetworkkt.NetConstKt;
import com.gystry.libnetworkkt.Request;
import com.gystry.pjetpack.AbsViewModel;
import com.gystry.pjetpack.model.Feed;
import com.gystry.pjetpack.ui.MutablePageKeyedDataSource;
import com.gystry.pjetpack.ui.login.UserManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class HomeViewModel extends AbsViewModel<Feed> {

    private volatile boolean withCache=true;
    private AtomicBoolean loadAfter = new AtomicBoolean(false);
    public MutableLiveData<PagedList<Feed>> cacheLiveData = new MutableLiveData<>();
    private String mFeedType;
    @Override
    public DataSource createDataSource() {
        return dataSource;
    }

    public MutableLiveData<PagedList<Feed>> getCacheLiveData() {
        return cacheLiveData;
    }

    public void setFeedType(String feedType) {

        mFeedType = feedType;
    }

    ItemKeyedDataSource<Integer, Feed> dataSource = new ItemKeyedDataSource<Integer, Feed>() {
        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Feed> callback) {
            //做加载初始化数据的
            loadData(0,params.requestedLoadSize, callback);
            withCache = false;
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            //做加载分页数据的
            loadData(params.key,params.requestedLoadSize, callback);
        }

        @Override
        public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            callback.onResult(Collections.emptyList());
            //能够向前加载,，一般用不到，所以可以传一个空的集合出去
        }

        @NonNull
        @Override
        public Integer getKey(@NonNull Feed item) {
            return item.id;
        }
    };

    private void loadData(int key, int count, ItemKeyedDataSource.LoadCallback<Feed> callback) {
// /feeds/queryHotFeedsList
        if (key > 0) {
            loadAfter.set(true);
        }
        Log.e("loadData", "----->" + mFeedType+":"+UserManager.getInstance().getUserId()+":"+key+":"+count);
        Request request = ApiService.INSTANCE.get("/feeds/queryHotFeedsList")
                .addParams("feedType", mFeedType)
                .addParams("userId", UserManager.getInstance().getUserId())
                .addParams("feedId", key)
                .addParams("pageCount", count)
                .responseType(new TypeReference<ArrayList<Feed>>() {
                }.getType());
        if (withCache) {
            request.cacheStrategy(NetConstKt.CACHE_ONLY);
            //请求缓存数据的时候，开启一个新的线程，这样就不会阻塞接口的请求
            request.execute(new JsonCallback<List<Feed>>() {
                @Override
                public void onCacheSuccess(ApiResponse<List<Feed>> response) {
                    Log.e("onCacheSuccess", "onCacheSuccess" + response.getBody());
                    if (response.getBody()!=null) {
                        MutablePageKeyedDataSource dataSource = new MutablePageKeyedDataSource<Feed>();
                        dataSource.data.addAll(response.getBody());

                        PagedList<Feed> pagedList = dataSource.buildNewPagedList(config);
                        cacheLiveData.postValue(pagedList);
                    }
                }
            });
        }

        Request netRequest = withCache ? request.clone() : request;
        //设置缓存模式，如果当前是第一个数据的话，那么就应该是下拉刷新，就将网络请求的数据缓存下来，要不然就不缓存
        netRequest.cacheStrategy(key == 0 ? NetConstKt.NET_CACHE : NetConstKt.NET_ONLY);
        ApiResponse<List<Feed>> response = netRequest.execute();
        Log.e("loadData", "onCacheSuccess" + response.getBody());
        List<Feed> data = response.getBody() == null ? Collections.emptyList() : response.getBody();
        callback.onResult(data);

        if (key > 0) {
            //通过livedata发送数据，告诉ui层，是否应该主动关闭上拉加载分页的动画
            getBoundaryPageData().postValue(data.size() > 0);
            loadAfter.set(false);
        }


    }

    @SuppressLint("RestrictedApi")
    public void loadAfter(int id, ItemKeyedDataSource.LoadCallback<Feed> callback) {
        if (loadAfter.get()) {
            callback.onResult(Collections.emptyList());
        }

        ArchTaskExecutor.getIOThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                loadData(id, config.pageSize, callback);
            }
        });
    }

}