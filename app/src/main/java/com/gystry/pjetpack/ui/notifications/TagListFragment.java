package com.gystry.pjetpack.ui.notifications;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.gystry.pjetpack.R;
import com.gystry.pjetpack.model.TagList;
import com.gystry.pjetpack.ui.AbsListFragment;
import com.gystry.pjetpack.ui.MutableItemKeyDataSource;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

/**
 * @author gystry
 * 创建日期：2020/8/6 14
 * 邮箱：gystry@163.com
 * 描述：
 */
public class TagListFragment extends AbsListFragment<TagList, TagListViewModel> {
    public static final String KEY_TAG_TYPE = "tag_type";
    private String tagType;

    public static TagListFragment newInstance(String tagType) {

        Bundle args = new Bundle();
        args.putString(KEY_TAG_TYPE, tagType);
        TagListFragment fragment = new TagListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void afterCreateView() {

    }

    @Override
    public PagedListAdapter getAdapter() {
        tagType = getArguments().getString(KEY_TAG_TYPE);
        mViewModel.setTagType(tagType);
        TagListAdapter tagListAdapter = new TagListAdapter(getContext());
        return tagListAdapter;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (TextUtils.equals(tagType, "onlyFollow")) {
            emptyView.setTitle(getString(R.string.tag_list_no_follow));
            emptyView.setButton(getString(R.string.tag_list_no_follow_button), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewModel.getSwitchTabLiveData().setValue(new Object());
                }
            });
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        //当有一次返回的数据为空数据，那么以后就不会再做分页数据加载了
        PagedList<TagList> currentList = getAdapter().getCurrentList();
        long tagId = currentList == null ? 0 : currentList.get(currentList.size() - 1).tagId;
        mViewModel.loadData(tagId, new ItemKeyedDataSource.LoadCallback<TagList>() {
            @Override
            public void onResult(@NonNull List data) {
                MutableItemKeyDataSource<Long, TagList> mutableItemKeyDataSource = new MutableItemKeyDataSource<Long, TagList>((ItemKeyedDataSource) mViewModel.getDataSource()) {
                    @NonNull
                    @Override
                    public Long getKey(@NonNull TagList item) {
                        return item.tagId;
                    }
                };
                mutableItemKeyDataSource.data.addAll(currentList);
                mutableItemKeyDataSource.data.addAll(data);
                PagedList<TagList> tagLists = mutableItemKeyDataSource.buildNewPagedList(currentList.getConfig());
                if (data.size() > 0) {
                    submitList(tagLists);
                }
            }
        });


    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mViewModel.getDataSource().invalidate();
    }
}
