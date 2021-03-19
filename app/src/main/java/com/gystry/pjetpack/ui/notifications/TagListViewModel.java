package com.gystry.pjetpack.ui.notifications;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;

import com.alibaba.fastjson.TypeReference;
import com.gystry.libnetworkkt.ApiResponse;
import com.gystry.libnetworkkt.ApiService;
import com.gystry.pjetpack.AbsViewModel;
import com.gystry.pjetpack.model.TagList;
import com.gystry.pjetpack.ui.login.UserManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author gystry
 * 创建日期：2020/8/6 14
 * 邮箱：gystry@163.com
 * 描述：
 */
public class TagListViewModel extends AbsViewModel<TagList> {
    private String tagType;
    private int offset;
    private AtomicBoolean loadAfter = new AtomicBoolean();
    private MutableLiveData switchTabLiveData = new MutableLiveData();

    @Override
    public DataSource createDataSource() {
        return new DataSource();
    }

    public MutableLiveData getSwitchTabLiveData() {
        return switchTabLiveData;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    private class DataSource extends ItemKeyedDataSource<Long, TagList> {

        @Override
        public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<TagList> callback) {
            loadData(0, callback);
        }

        private void loadData(long requestKey, LoadCallback<TagList> callback) {
            if (requestKey > 0) {
                loadAfter.set(true);
            }

            ApiResponse<List<TagList>> response = ApiService.INSTANCE.<List<TagList>>get("/tag/queryTagList")
                    .addParams("userId", UserManager.getInstance().getUserId())
                    .addParams("tagId", requestKey)
                    .addParams("tagType", tagType)
                    .addParams("pageCount", 10)
                    .addParams("offset", offset)
                    .responseType(new TypeReference<ArrayList<TagList>>() {
                    }.getType())
                    .execute();

            List<TagList> result = response.getBody() == null ? Collections.emptyList() : response.getBody();
            callback.onResult(result);

            if (requestKey > 0) {
                loadAfter.set(false);
                offset += result.size();
                ((MutableLiveData) getBoundaryPageData()).postValue(result.size() > 0);
            } else {
                offset = result.size();
            }
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<TagList> callback) {
            loadData(params.key, callback);
        }

        @Override
        public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<TagList> callback) {
            callback.onResult(Collections.emptyList());
        }

        @NonNull
        @Override
        public Long getKey(@NonNull TagList item) {
            return item.tagId;
        }
    }

    @SuppressLint("RestrictedApi")
    public void loadData(long tagId, ItemKeyedDataSource.LoadCallback callback) {
        if (tagId <= 0 || loadAfter.get()) {
            callback.onResult(Collections.emptyList());
            return;
        }
        ArchTaskExecutor.getIOThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ((TagListViewModel.DataSource) getDataSource()).loadData(tagId, callback);
            }
        });

    }
}
