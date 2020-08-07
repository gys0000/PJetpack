package com.gystry.pjetpack.ui.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gystry.libcommon.extention.AbsPageListAdapter;
import com.gystry.pjetpack.InteractionPresenter;
import com.gystry.pjetpack.databinding.LayoutFeedCommentListItemBinding;
import com.gystry.pjetpack.databinding.LayoutTagListItemBinding;
import com.gystry.pjetpack.model.TagList;

/**
 * @author gystry
 * 创建日期：2020/8/6 15
 * 邮箱：gystry@163.com
 * 描述：
 */
public class TagListAdapter extends AbsPageListAdapter<TagList, TagListAdapter.ViewHolder> {

    private final Context mContext;
    private final LayoutInflater mInflater;

    protected TagListAdapter(Context context) {
        super(new DiffUtil.ItemCallback<TagList>() {
            @Override
            public boolean areItemsTheSame(@NonNull TagList oldItem, @NonNull TagList newItem) {
                return oldItem.tagId == newItem.tagId;
            }

            @Override
            public boolean areContentsTheSame(@NonNull TagList oldItem, @NonNull TagList newItem) {
                return oldItem.equals(newItem);
            }
        });
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType2(int position) {
        return 0;
    }

    @Override
    protected ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        LayoutTagListItemBinding itemBinding = LayoutTagListItemBinding.inflate(mInflater, parent, false);
        return new ViewHolder(itemBinding.getRoot(), itemBinding);
    }

    @Override
    protected void onBindViewHolder2(ViewHolder holder, int position) {
        holder.bindData(getItem(position));
        holder.itemBinding.actionFollow.setOnClickListener(v -> {
            InteractionPresenter.toggleTagLike(((LifecycleOwner) mContext), getItem(position));
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutTagListItemBinding itemBinding;

        public ViewHolder(@NonNull View itemView, LayoutTagListItemBinding itemBinding) {
            super(itemView);
            this.itemBinding = itemBinding;
        }

        public void bindData(TagList item) {
            itemBinding.setTagList(item);
        }
    }
}
