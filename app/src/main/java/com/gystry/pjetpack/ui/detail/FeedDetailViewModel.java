package com.gystry.pjetpack.ui.detail;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;

import com.alibaba.fastjson.TypeReference;
import com.gystry.libnetwork.ApiResponse;
import com.gystry.libnetwork.ApiService;
import com.gystry.pjetpack.AbsViewModel;
import com.gystry.pjetpack.model.Comment;
import com.gystry.pjetpack.ui.login.UserManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author gystry
 * 创建日期：2020/7/21 16
 * 邮箱：gystry@163.com
 * 描述：
 */
public class FeedDetailViewModel extends AbsViewModel<Comment> {
    private long itemId;

    @Override
    public DataSource createDataSource() {
        return null;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    class DataSource extends ItemKeyedDataSource<Integer, Comment> {
        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Comment> callback) {
            loadData(params.requestedInitialKey, params.requestedLoadSize, callback);
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Comment> callback) {
            loadData(params.key, params.requestedLoadSize, callback);
        }

        private void loadData(int key, int requestedLoadSize, LoadCallback callback) {
            ApiResponse<List<Comment>> response = ApiService.get("/comment/queryFeedComments")
                    .addParams("id", key)
                    .addParams("itemId", itemId)
                    .addParams("userId", UserManager.getInstance().getUserId())
                    .addParams("pageCount", requestedLoadSize)
                    .responseType(new TypeReference<ArrayList<Comment>>() {
                    }.getType())
                    .execute();
            List<Comment> body = response.body == null ? Collections.emptyList() : response.body;
            callback.onResult(body);
        }

        @Override
        public void loadBefore(@NonNull LoadParams params, @NonNull LoadCallback callback) {
            callback.onResult(Collections.emptyList());
        }

        @NonNull
        @Override
        public Integer getKey(@NonNull Comment item) {
            return item.id;
        }
    }
}
