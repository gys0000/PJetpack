package com.gystry.pjetpack.ui.mine;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gystry.libcommon.extention.AbsPageListAdapter;
import com.gystry.pjetpack.InteractionPresenter;
import com.gystry.pjetpack.R;
import com.gystry.pjetpack.model.Feed;
import com.gystry.pjetpack.ui.MutableItemKeyDataSource;
import com.gystry.pjetpack.ui.home.FeedAdapter;
import com.gystry.pjetpack.ui.login.UserManager;
import com.gystry.pjetpack.utils.TimeUtils;

/**
 * @author gystry
 * 创建日期：2020/8/12 18
 * 邮箱：gystry@163.com
 * 描述：
 */
public class ProfileListAdapter extends FeedAdapter {
    public ProfileListAdapter(Context context, String category) {
        super(context, category);
    }

    @Override
    public int getItemViewType2(int position) {
        if (TextUtils.equals(category, ProfileActivity.TAB_TYPE_COMMENT)) {
            return R.layout.layout_feed_type_comment;
        } else if (TextUtils.equals(category, ProfileActivity.TAB_TYPE_ALL)) {
            Feed feed = getItem(position);
            if (feed.topComment != null && feed.topComment.userId == UserManager.getInstance().getUserId()) {
                return R.layout.layout_feed_type_comment;
            }
        }
        return super.getItemViewType2(position);
    }

    @Override
    protected void onBindViewHolder2(ViewHolder holder, int position) {
        View deleteView = holder.itemView.findViewById(R.id.feed_delete);
        TextView createTime = holder.itemView.findViewById(R.id.create_time);

        Feed feed = getItem(position);
        createTime.setVisibility(View.VISIBLE);
        createTime.setText(TimeUtils.caculate(feed.createTime));

        boolean isCommentTab = TextUtils.equals(category, ProfileActivity.TAB_TYPE_COMMENT);
        deleteView.setVisibility(View.VISIBLE);
        deleteView.setOnClickListener(v -> {
            //如果是个人主页的评论tab，删除的时候，实际上是删除帖子的评论。
            if (isCommentTab) {
                InteractionPresenter.deleteFeedComment(context, feed.itemId, feed.topComment.commentId)
                        .observe((LifecycleOwner) context, success -> {
                            refreshList(feed);
                        });
            } else {
                InteractionPresenter.deleteFeed(context, feed.itemId)
                        .observe((LifecycleOwner) context, success -> {
                            refreshList(feed);
                        });
            }
        });
    }
    private void refreshList(Feed delete) {
        //实际上这个方法 可以再封装一下
        PagedList<Feed> currentList = getCurrentList();
        MutableItemKeyDataSource<Integer, Feed> dataSource = new MutableItemKeyDataSource<Integer, Feed>((ItemKeyedDataSource) currentList.getDataSource()) {
            @NonNull
            @Override
            public Integer getKey(@NonNull Feed item) {
                return item.id;
            }
        };
        //for循环一遍,过滤掉被删除的这个帖子
        for (Feed feed : currentList) {
            if (feed != delete) {
                dataSource.data.add(feed);
            }
        }
        PagedList<Feed> pagedList = dataSource.buildNewPagedList(currentList.getConfig());
        submitList(pagedList);
    }
}
