package com.gystry.pjetpack.ui.detail;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.ActivityNavigator;
import androidx.recyclerview.widget.RecyclerView;

import com.gystry.pjetpack.R;
import com.gystry.pjetpack.databinding.ActivityFeedDetailTypeImageBinding;
import com.gystry.pjetpack.databinding.LayoutFeedDetailBottomInteractionBinding;
import com.gystry.pjetpack.databinding.LayoutFeedDetailTypeImageHeaderBinding;
import com.gystry.pjetpack.model.Feed;
import com.gystry.pjetpack.widget.PpImageView;

/**
 * @author gystry
 * 创建日期：2020/7/21 11
 * 邮箱：gystry@163.com
 * 描述：处理图文详情页
 * <p>
 * DataBindingUtil.setContentView(activity, R.layout.activity_feed_detail_type_image);
 */
public class ImageViewHandler extends ViewHandler {


    private final ActivityFeedDetailTypeImageBinding mImageBinding;
    private LayoutFeedDetailTypeImageHeaderBinding mHeaderBinding;

    public ImageViewHandler(FragmentActivity activity) {
        super(activity);
        //通过这个方法之后，FeedDetailActivity的页面样式就是activity_feed_detail_type_image
        mImageBinding = DataBindingUtil.setContentView(activity, R.layout.activity_feed_detail_type_image);
        mImageBinding.setFeed(mFeed);
        mRecycleView = mImageBinding.recyclerView;
        mInteractionBinding = mImageBinding.interactionLayout;
    }

    @Override
    public void bindInitData(Feed feed) {
        super.bindInitData(feed);
        mHeaderBinding = LayoutFeedDetailTypeImageHeaderBinding.inflate(LayoutInflater.from(mActivity), mRecycleView, false);
        mHeaderBinding.setFeed(mFeed);
        PpImageView headerImage = mHeaderBinding.headerImage;
        headerImage.bind(mFeed.width, mFeed.height, mFeed.width > mFeed.height ? 0 : 16, mFeed.cover);
        listAdapter.addHeaderView(mHeaderBinding.getRoot());

        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean visible = mHeaderBinding.getRoot().getTop() <= -mImageBinding.titleLayout.getMeasuredHeight();
                mImageBinding.authorInfoLayout.getRoot().setVisibility(visible?View.VISIBLE:View.GONE);
                mImageBinding.title.setVisibility(visible?View.GONE:View.VISIBLE);
            }
        });
    }
}
