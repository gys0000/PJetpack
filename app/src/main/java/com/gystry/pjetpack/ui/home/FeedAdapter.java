package com.gystry.pjetpack.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gystry.pjetpack.databinding.LayoutFeedTypeImageBinding;
import com.gystry.pjetpack.databinding.LayoutFeedTypeVideoBinding;
import com.gystry.pjetpack.model.Feed;

/**
 * @author gystry
 * 创建日期：2020/7/9 10
 * 邮箱：gystry@163.com
 * 描述：
 */
public class FeedAdapter extends PagedListAdapter<Feed, FeedAdapter.ViewHolder> {

    private final String category;
    private Context context;
    private LayoutInflater inflater;

    protected FeedAdapter(Context context,String category) {
        //diffCallback 数据做差分比较时的回调
        super(new DiffUtil.ItemCallback<Feed>() {
            @Override
            public boolean areItemsTheSame(@NonNull Feed oldItem, @NonNull Feed newItem) {
                return oldItem.id == newItem.id;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Feed oldItem, @NonNull Feed newItem) {
                return oldItem.equals(newItem);
            }
        });
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.category = category;
    }

    @Override
    public int getItemViewType(int position) {
        Feed feed = getItem(position);
        return feed.itemType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding dataBinding = null;
        if (viewType == Feed.IMAGE_TYPE) {
            dataBinding = LayoutFeedTypeImageBinding.inflate(inflater, parent, false);
        } else {
            dataBinding = LayoutFeedTypeVideoBinding.inflate(inflater, parent, false);
        }
        return new ViewHolder(dataBinding.getRoot(), dataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(getItem(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding dataBinding;

        public ViewHolder(View root, ViewDataBinding dataBinding) {
            super(root);
            this.dataBinding = dataBinding;
        }

        public void bindData(Feed item, int position) {
            if (dataBinding instanceof LayoutFeedTypeImageBinding) {
                LayoutFeedTypeImageBinding imageBinding = (LayoutFeedTypeImageBinding) this.dataBinding;
                imageBinding.setFeed(item);
                imageBinding.setLifecycleOwner((LifecycleOwner) context);
                imageBinding.feedImage.bind(item.width, item.height, 16, item.cover);
            }else {
                LayoutFeedTypeVideoBinding videoBinding = (LayoutFeedTypeVideoBinding) this.dataBinding;
                videoBinding.setFeed(item);
                videoBinding.setLifecycleOwner((LifecycleOwner) context);
                videoBinding.listPlayerView.bindData(category,item.width,item.height,item.cover,item.url);
            }
        }
    }
}
