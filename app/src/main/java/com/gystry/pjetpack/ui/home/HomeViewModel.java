package com.gystry.pjetpack.ui.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;

import com.alibaba.fastjson.TypeReference;
import com.gystry.libnetwork.ApiResponse;
import com.gystry.libnetwork.ApiService;
import com.gystry.libnetwork.JsonCallback;
import com.gystry.libnetwork.Request;
import com.gystry.pjetpack.AbsViewModel;
import com.gystry.pjetpack.model.Feed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeViewModel extends AbsViewModel<Feed> {

    private volatile boolean withCache;

    @Override
    public DataSource createDataSource() {
        return dataSource;
    }

    ItemKeyedDataSource<Integer, Feed> dataSource = new ItemKeyedDataSource<Integer, Feed>() {
        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Feed> callback) {
            //做加载初始化数据的
            //先加载缓存，再加载网络数据，最后网络数据加载回来再更新缓存
            loadData(0, callback);
            withCache = false;
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            //做加载分页数据的
            loadData(params.key, callback);
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

    private void loadData(int key, ItemKeyedDataSource.LoadCallback<Feed> callback) {
// /feeds/queryHotFeedsList

        Request request = ApiService.get("/feeds/queryHotFeedsList")
                .addParams("feedType", null)
                .addParams("userId", 0)
                .addParams("feedId", key)
                .addParams("pageCount", 10)
                .responseType(new TypeReference<ArrayList<Feed>>() {
                }.getType());
        if (withCache) {
            request.cacheStrategy(Request.CACHE_ONLY);
            //请求缓存数据的时候，开启一个新的线程，这样就不会阻塞接口的请求
            request.execute(new JsonCallback<List<Feed>>() {
                @Override
                public void onCacheSuccess(ApiResponse<List<Feed>> response) {
                    Log.e("onCacheSuccess", "onCacheSuccess" + response.body.size());
                }
            });
        }

        try {
            Request netRequest = withCache ? request.clone() : request;
            //设置缓存模式，如果当前是第一个数据的话，那么就应该是下拉刷新，就将网络请求的数据缓存下来，要不然就不缓存
            netRequest.cacheStrategy(key == 0 ? Request.NET_CACHE : Request.NET_ONLY);
            ApiResponse<List<Feed>> response = netRequest.execute();
            List<Feed> data = response.body == null ? Collections.emptyList() : response.body;
            callback.onResult(data);

            if (key > 0) {
                //通过livedata发送数据，告诉ui层，是否应该主动关闭上拉加载分页的动画
                getBoundaryPageData().postValue(data.size() > 0);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }


    }

}