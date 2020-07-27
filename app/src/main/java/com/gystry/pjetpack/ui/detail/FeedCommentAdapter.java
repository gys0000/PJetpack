package com.gystry.pjetpack.ui.detail;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gystry.libcommon.PixUtils;
import com.gystry.libcommon.extention.AbsPageListAdapter;
import com.gystry.pjetpack.InteractionPresenter;
import com.gystry.pjetpack.databinding.LayoutFeedCommentListItemBinding;
import com.gystry.pjetpack.model.Comment;
import com.gystry.pjetpack.ui.MutableItemKeyDataSource;
import com.gystry.pjetpack.ui.login.UserManager;
import com.gystry.pjetpack.ui.publish.PreviewActivity;

import static android.view.View.VISIBLE;

/**
 * @author gystry
 * 创建日期：2020/7/21 14
 * 邮箱：gystry@163.com
 * 描述：评论列表的适配器
 */
public class FeedCommentAdapter extends AbsPageListAdapter<Comment, FeedCommentAdapter.ViewHolder> {

    private Context context;

    protected FeedCommentAdapter(Context context) {
        super(new DiffUtil.ItemCallback<Comment>() {
            @Override
            public boolean areItemsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
                return newItem.id == oldItem.id;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
                return newItem.equals(oldItem);
            }
        });
        this.context = context;
    }

    @Override
    public int getItemViewType2(int position) {
        return 0;
    }

    @Override
    protected ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        LayoutFeedCommentListItemBinding listItemBinding = LayoutFeedCommentListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(listItemBinding.getRoot(), listItemBinding);
    }

    @Override
    protected void onBindViewHolder2(ViewHolder holder, int position) {
        Comment item = getItem(position);
        holder.bindData(item);
        holder.mListItemBinding.commentDelete.setOnClickListener(v -> {
            InteractionPresenter.deleteFeed(context, item.itemId).observe(((LifecycleOwner) context), aBoolean -> {
                if (aBoolean) {
                    MutableItemKeyDataSource<Integer, Comment> mutableItemKeyDataSource = new MutableItemKeyDataSource<Integer, Comment>((ItemKeyedDataSource) getCurrentList().getDataSource()) {
                        @NonNull
                        @Override
                        public Integer getKey(@NonNull Comment item) {
                            return item.id;
                        }
                    };
                    PagedList<Comment> currentList = getCurrentList();
                    for (Comment comment : currentList) {
                        if (comment != getItem(position)) {
                            mutableItemKeyDataSource.data.add(comment);
                        }
                    }
                    PagedList<Comment> comments = mutableItemKeyDataSource.buildNewPagedList(getCurrentList().getConfig());
                    submitList(comments);
                }
            });
        });

        holder.mListItemBinding.commentCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isVideo = item.commentType == Comment.COMMENT_TYPE_VIDEO;
                PreviewActivity.startActivityForResult((Activity) context, isVideo ? item.videoUrl : item.imageUrl, isVideo, null);

            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private LayoutFeedCommentListItemBinding mListItemBinding;

        public ViewHolder(View root, LayoutFeedCommentListItemBinding listItemBinding) {
            super(root);
            mListItemBinding = listItemBinding;
        }

        public void bindData(Comment item) {
            mListItemBinding.setComment(item);
            mListItemBinding.labelAuthor.setVisibility(UserManager.getInstance().getUserId() == item.author.userId ? View.VISIBLE : View.GONE);
            mListItemBinding.commentDelete.setVisibility(UserManager.getInstance().getUserId() == item.author.userId ? View.VISIBLE : View.GONE);
            if (!TextUtils.isEmpty(item.imageUrl)) {
                mListItemBinding.commentCover.setVisibility(VISIBLE);
                mListItemBinding.commentCover.bind(item.width, item.height, 0, PixUtils.dp2px(200), PixUtils.dp2px(200), item.imageUrl);
                if (!TextUtils.isEmpty(item.videoUrl)) {
                    mListItemBinding.videoIcon.setVisibility(VISIBLE);
                } else {
                    mListItemBinding.videoIcon.setVisibility(View.GONE);
                }
            } else {
                mListItemBinding.commentCover.setVisibility(View.GONE);
                mListItemBinding.videoIcon.setVisibility(View.GONE);
            }
        }
    }
}
