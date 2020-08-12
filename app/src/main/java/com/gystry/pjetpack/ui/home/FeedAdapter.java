package com.gystry.pjetpack.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewConfigurationCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gystry.libcommon.extention.AbsPageListAdapter;
import com.gystry.libcommon.extention.LiveDataBus;
import com.gystry.pjetpack.BR;
import com.gystry.pjetpack.InteractionPresenter;
import com.gystry.pjetpack.R;
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
public class FeedAdapter extends AbsPageListAdapter<Feed, FeedAdapter.ViewHolder> {

    protected String category;
    protected Context context;
    protected final LayoutInflater inflater;

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
    public int getItemViewType2(int position) {
        Feed feed = getItem(position);
        if (feed.itemType == Feed.IMAGE_TYPE) {
            return R.layout.layout_feed_type_image;
        } else if (feed.itemType == Feed.VIDEO_TYPE) {
            return R.layout.layout_feed_type_video;
        }
        return 0;
    }

    @Override
    protected ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, viewType, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    protected void onBindViewHolder2(ViewHolder holder, int position) {
        final Feed feed = getItem(position);
        holder.bindData(feed);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedDetailActivity.startFeedDetailActivity(context, feed, category);
                onStartFeedDetailActivity(feed);
                LiveDataBus.getInstance()
                        .with(InteractionPresenter.DATA_FROM_INTERACTION)
                        .observe((LifecycleOwner) context, new Observer<Feed>() {
                            @Override
                            public void onChanged(Feed newOne) {
                                if (feed.id != newOne.id)
                                    return;
                                feed.author = newOne.author;
                                feed.ugc = newOne.ugc;
                                feed.notifyChange();
                            }

                        });
            }
        });
    }

    public void onStartFeedDetailActivity(Feed feed) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewDataBinding dataBinding;
        public ListPlayerView listPlayerView;
        public ImageView feedImage;

        public ViewHolder(View root, ViewDataBinding dataBinding) {
            super(root);
            this.dataBinding = dataBinding;
        }

        public void bindData(Feed item) {
            dataBinding.setVariable(BR.feed, item);
            dataBinding.setVariable(BR.lifeCycleOwner, context);
            if (dataBinding instanceof LayoutFeedTypeImageBinding) {
                LayoutFeedTypeImageBinding imageBinding = (LayoutFeedTypeImageBinding) this.dataBinding;
                feedImage = imageBinding.feedImage;
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
