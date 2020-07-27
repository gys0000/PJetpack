package com.gystry.pjetpack.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewConfigurationCompat;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gystry.libcommon.extention.LiveDataBus;
import com.gystry.pjetpack.BR;
import com.gystry.pjetpack.InteractionPresenter;
import com.gystry.pjetpack.databinding.LayoutFeedTypeImageBinding;
import com.gystry.pjetpack.databinding.LayoutFeedTypeVideoBinding;
import com.gystry.pjetpack.model.Feed;
import com.gystry.pjetpack.ui.detail.FeedDetailActivity;
import com.gystry.pjetpack.widget.ListPlayerView;

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

    protected FeedAdapter(Context context, String category) {
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
        Feed feed = getItem(position);
        holder.bindData(getItem(position), position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedDetailActivity.startFeedDetailActivity(context,getItem(position),category);
                onStartFeedDetailActivity(feed);
                LiveDataBus.getInstance().with(InteractionPresenter.DATA_FROM_INTERACTION)
                        .observe(((LifecycleOwner) context), new Observer<Feed>() {
                            @Override
                            public void onChanged(Feed feed) {
                                Feed item = getItem(position);
                                if (item.id!=feed.id) {
                                    return;
                                }
                                item.author=feed.author;
                                item.ugc=feed.ugc;
                                item.notifyChange();
                            }
                        });
            }
        });
    }

    public void onStartFeedDetailActivity(Feed feed) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding dataBinding;
        private ListPlayerView listPlayerView;

        public ViewHolder(View root, ViewDataBinding dataBinding) {
            super(root);
            this.dataBinding = dataBinding;
        }

        public void bindData(Feed item, int position) {
            dataBinding.setVariable(BR.feed, item);
            dataBinding.setVariable(BR.lifeCycleOwner, context);
            if (dataBinding instanceof LayoutFeedTypeImageBinding) {
                LayoutFeedTypeImageBinding imageBinding = (LayoutFeedTypeImageBinding) this.dataBinding;
                imageBinding.feedImage.bind(item.width, item.height, 16, item.cover);
            } else {
                LayoutFeedTypeVideoBinding videoBinding = (LayoutFeedTypeVideoBinding) this.dataBinding;
                videoBinding.listPlayerView.bindData(category, item.width, item.height, item.cover, item.url);
                listPlayerView = videoBinding.listPlayerView;
            }
        }

        public boolean isVideoItem() {
            return dataBinding instanceof LayoutFeedTypeVideoBinding;
        }

        public ListPlayerView getListPlayerView() {
            return listPlayerView;
        }
    }
}
