package com.gystry.pjetpack.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gystry.pjetpack.model.Feed;
import com.gystry.pjetpack.ui.publish.CaptureActivity;

/**
 * @author gystry
 * 创建日期：2020/7/20 10
 * 邮箱：gystry@163.com
 * 描述：
 */
public class FeedDetailActivity extends AppCompatActivity {
    public static final String KEY_FEED = "key_feed";
    public static final String KEY_CATEGORY = "key_category";
    private ViewHandler viewHandler;

    /**
     * category 视频详情页做无缝续播时候使用
     * @param context
     * @param item
     * @param category
     */
    public static void startFeedDetailActivity(Context context, Feed item, String category) {
        Intent intent = new Intent(context, FeedDetailActivity.class);
        intent.putExtra(KEY_FEED,item);
        intent.putExtra(KEY_CATEGORY,category);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Feed feed = (Feed) getIntent().getParcelableExtra(KEY_FEED);
        if (feed == null) {
            finish();
            return;
        }
        if (feed.itemType == Feed.IMAGE_TYPE) {
            viewHandler = new ImageViewHandler(this);
        } else {
            viewHandler = new VideoViewHandler(this);
        }

        viewHandler.bindInitData(feed);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (viewHandler!=null) {
            viewHandler.onActivityResult(requestCode,resultCode,data);
        }
    }
}
