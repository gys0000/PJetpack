package com.gystry.pjetpack.ui.notifications;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;

import com.alibaba.fastjson.TypeReference;
import com.gystry.libnetwork.ApiResponse;
import com.gystry.libnetwork.ApiService;
import com.gystry.pjetpack.AbsViewModel;
import com.gystry.pjetpack.model.Feed;
import com.gystry.pjetpack.ui.login.UserManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author gystry
 * 创建日期：2020/8/7 16
 * 邮箱：gystry@163.com
 * 描述：
 */
public class TagFeedListViewModel extends AbsViewModel<Feed> {
    private String feedType;
    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }
    @Override
    public DataSource createDataSource() {
        return null;
    }

    private class DataSource extends ItemKeyedDataSource<Integer, Feed> {

        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Feed> callback) {
            loadData(0, callback);
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            loadData(params.key, callback);
        }

        @Override
        public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            callback.onResult(Collections.emptyList());
        }

        @NonNull
        @Override
        public Integer getKey(@NonNull Feed item) {
            return item.id;
        }
    }

    private void loadData(Integer feedId, ItemKeyedDataSource.LoadCallback<Feed> callback) {
        ApiResponse<List<Feed>> response = ApiService.get("/feeds/queryHotFeedsList")
                .addParams("userId", UserManager.getInstance().getUserId())
                .addParams("pageCount", 10)
                .addParams("feedType", feedType)
                .addParams("feedId", feedId)
                .responseType(new TypeReference<ArrayList<Feed>>() {
                }.getType())
                .execute();

        List<Feed> result = response.body == null ? Collections.emptyList() : response.body;
        callback.onResult(result);

        if (feedId > 0) {
            //分页的情况 通知一下 UI 本次加载是否有数据,方便UI 关闭上拉加载动画什么的
            ((MutableLiveData) getBoundaryPageData()).postValue(result.size() > 0);
        }
    }
}
