package com.gystry.libcommon.extention;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author gystry
 * 创建日期：2020/7/21 09
 * 邮箱：gystry@163.com
 * 描述：
 */
public abstract class AbsPageListAdapter<T, VH extends RecyclerView.ViewHolder> extends PagedListAdapter<T, VH> {

    //保存头部和底部布局的集合
    private SparseArray<View> mHeaders = new SparseArray<>();
    private SparseArray<View> mFooters = new SparseArray<>();

    private int BASE_ITEM_TYPE_HEADER = 100000;
    private int BASE_ITEM_TYPE_FOOTER = 200000;

    protected AbsPageListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }

    /**
     * 添加头部布局
     *
     * @param view
     */
    public void addHeaderView(View view) {
        if (mHeaders.indexOfValue(view) < 0) {
            mHeaders.put(BASE_ITEM_TYPE_HEADER++, view);
            notifyDataSetChanged();
        }
    }

    public void removeHeaderView(View view){
        int index = mHeaders.indexOfValue(view);
        if(index < 0){
            return;
        }
        mHeaders.remove(index);
        notifyDataSetChanged();
    }

    // 移除底部
    public void removeFooterView(View view) {
        int index = mFooters.indexOfValue(view);
        if (index < 0) return;
        mFooters.removeAt(index);
        notifyDataSetChanged();
    }


    /**
     * 添加底部布局
     *
     * @param view
     */
    public void addFooterView(View view) {
        if (mFooters.indexOfValue(view) < 0) {
            mFooters.put(BASE_ITEM_TYPE_FOOTER++, view);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        int itemCount = super.getItemCount();
        return itemCount + mHeaders.size() + mFooters.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            return mHeaders.keyAt(position);
        }
        if (isFooterPosition(position)) {
            return mFooters.keyAt(position);
        }
        position = position - mHeaders.size();
        return getItemViewType2(position);
    }

    public abstract int getItemViewType2(int position);

    /**
     * 判断是不是底部布局
     * @param position
     * @return
     */
    private boolean isFooterPosition(int position) {
        if (position > getOriginalItemCount() + mHeaders.size()) {
            return true;
        }
        return false;
    }

    /**
     * 判断是不是头部布局
     * @param position
     * @return
     */
    private boolean isHeaderPosition(int position) {
        if (position < mHeaders.size()) {
            return true;
        }
        return false;
    }

    /**
     * 获取除了顶部和底部的数据量
     *
     * @return
     */
    public int getOriginalItemCount() {
        return getItemCount() - mHeaders.size() - mFooters.size();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mHeaders.indexOfKey(viewType)>=0) {
            View view = mHeaders.get(viewType);
            return (VH) new RecyclerView.ViewHolder(view){};
        }
        if (mFooters.indexOfKey(viewType)>=0) {
            View view = mFooters.get(viewType);
            return (VH) new RecyclerView.ViewHolder(view){};
        }
        return onCreateViewHolder2(parent,viewType);
    }

    protected abstract VH onCreateViewHolder2(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (isHeaderPosition(position)||isFooterPosition(position)) {
            return;
        }
        position=position-mHeaders.size();
        onBindViewHolder2(holder,position);
    }

    protected abstract void onBindViewHolder2(VH holder, int position);

    /**
     * 在网络加载成功之前就添加了headerView和footerView，那么在网络数据加载之后列表不会定位到第一个item上边
     * 因为网络数据加载之后的pageList的数量并不包含headerView和footerView的数量，那么page框架在计算列表当中item的时候位置会出现错误
     * 页面刷新完成之后都会执行到registerAdapterDataObserver的observer观察者中，这个观察这种的positionStart的数量并没有包含headerView的数量
     * 所以我们需要想办法自己执行以下这个观察者，修改positionStart的值。
     * 下边使用的是代理的方式封装一层，修改一下positionStart在执行observer
     * @param observer
     */
    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(new AdapterDataObserverProxy(observer));
    }

    private class AdapterDataObserverProxy extends RecyclerView.AdapterDataObserver{

        private final RecyclerView.AdapterDataObserver observer;

        public AdapterDataObserverProxy(RecyclerView.AdapterDataObserver observer) {
            this.observer = observer;
        }

        public void onChanged() {
            // Do nothing
            observer.onChanged();
        }

        public void onItemRangeChanged(int positionStart, int itemCount) {
            // do nothing
            observer.onItemRangeChanged(positionStart+mHeaders.size(),itemCount);
        }

        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            observer.onItemRangeChanged(positionStart+mHeaders.size(),itemCount,payload);
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            // do nothing
            observer.onItemRangeInserted(positionStart+mHeaders.size(),itemCount);
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            // do nothing
            observer.onItemRangeRemoved(positionStart+mHeaders.size(),itemCount);
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            // do nothing
            observer.onItemRangeMoved(fromPosition+mHeaders.size(),toPosition+mHeaders.size(),itemCount);
        }
    }
}
