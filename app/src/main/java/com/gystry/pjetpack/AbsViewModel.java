package com.gystry.pjetpack;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

/**
 * @author gystry
 * 创建日期：2020/7/9 11
 * 邮箱：gystry@163.com
 * 描述：
 */
public abstract class AbsViewModel<T> extends ViewModel {

    private LiveData<PagedList<T>> pageData;
    private DataSource dataSource;
    private MutableLiveData<Boolean> boundaryPageData=new MutableLiveData<>();

    public AbsViewModel() {
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(10)//第一次加载数据的时候（初始化加载）加载的数据数量
//                .setMaxSize(100)  最大数值
//                .setEnablePlaceholders(false)  占位符，当要展示100条数据的时候，但是实际数据只有十条，那到第十一条的时候就会用占位符展示
//                .setPrefetchDistance()//在距离列表底部还剩几个数据的时候就开始加载下一页，如果不设置，就默认是一页的数量
                .build();

        pageData = new LivePagedListBuilder(factory, config)
                .setInitialLoadKey(0)
//                .setFetchExecutor()
                .setBoundaryCallback(boundaryCallback)//监听pagelist数据加载的状态
                .build();
    }

    public LiveData<PagedList<T>> getPageData() {
        return pageData;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public MutableLiveData<Boolean> getBoundaryPageData() {
        return boundaryPageData;
    }

    PagedList.BoundaryCallback<T> boundaryCallback = new PagedList.BoundaryCallback<T>() {
        @Override
        public void onZeroItemsLoaded() {
            //从PagedList的数据源的初始加载返回零项时被调用
            boundaryPageData.postValue(false);
        }

        @Override
        public void onItemAtFrontLoaded(@NonNull T itemAtFront) {
            //当列表的第一条数据被加载的时候会被调用
            boundaryPageData.postValue(true);
        }

        @Override
        public void onItemAtEndLoaded(@NonNull T itemAtEnd) {
            //当pageList的最后一条数据被加载到屏幕上的时候，这个方法会被调用
            super.onItemAtEndLoaded(itemAtEnd);
        }
    };

    DataSource.Factory factory = new DataSource.Factory() {
        @NonNull
        @Override
        public DataSource create() {
            dataSource = createDataSource();
            return dataSource;
        }
    };

    public abstract DataSource createDataSource();
}
