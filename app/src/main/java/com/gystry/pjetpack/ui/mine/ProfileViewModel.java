package com.gystry.pjetpack.ui.mine;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;

import com.alibaba.fastjson.TypeReference;
import com.gystry.libnetworkkt.ApiResponse;
import com.gystry.libnetworkkt.ApiService;
import com.gystry.pjetpack.AbsViewModel;
import com.gystry.pjetpack.model.Feed;
import com.gystry.pjetpack.ui.login.UserManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author gystry
 * 创建日期：2020/8/12 17
 * 邮箱：gystry@163.com
 * 描述：
 */
public class ProfileViewModel extends AbsViewModel<Feed> {
    private String profileType;


    @Override
    public DataSource createDataSource() {
        return new DataSource();
    }

    public void setProfileType(String tabType) {
        this.profileType = tabType;
    }

    private class DataSource extends ItemKeyedDataSource<Integer, Feed> {

        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Feed> callback) {
            loadData(params.requestedInitialKey, callback);
        }

        private void loadData(Integer key, ItemKeyedDataSource.LoadCallback<Feed> callback) {
            ApiResponse<List<Feed>> response = ApiService.INSTANCE.<List<Feed>>get("/feeds/queryProfileFeeds")
                    .addParams("feedId", key)
                    .addParams("userId", UserManager.getInstance().getUserId())
                    .addParams("pageCount", 10)
                    .addParams("profileType", profileType)
                    .responseType(new TypeReference<ArrayList<Feed>>() {
                    }.getType())
                    .execute();

            List<Feed> result = response.getBody() == null ? Collections.emptyList() : response.getBody();
            callback.onResult(result);

            if (key > 0) {
                //告知UI层 本次分页是否有更多数据被加载回来了,也方便UI层关闭上拉加载的动画
                ((MutableLiveData) getBoundaryPageData()).postValue(result.size() > 0);
            }

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
}
