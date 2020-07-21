package com.gystry.pjetpack.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.alibaba.fastjson.JSONObject;
import com.gystry.libcommon.AppGlobal;
import com.gystry.libnetwork.ApiResponse;
import com.gystry.libnetwork.ApiService;
import com.gystry.libnetwork.JsonCallback;
import com.gystry.pjetpack.model.Comment;
import com.gystry.pjetpack.model.Feed;
import com.gystry.pjetpack.model.User;
import com.gystry.pjetpack.ui.ShareDialog;
import com.gystry.pjetpack.ui.login.UserManager;

import org.json.JSONException;

import java.util.Date;

/**
 * @author gystry
 * 创建日期：2020/7/16 11
 * 邮箱：gystry@163.com
 * 描述：
 */
public class InteractionPresenter {

    public static final String URL_TOGGLE_FEED_LIKE = "/ugc/toggleFeedLike";
    public static final String URL_TOGGLE_FEED_DISS = "/ugc/dissFeed";
    public static final String URL_SHARE = "/ugc/increaseShareCount";
    public static final String URL_TOGGLE_COMMENT_LIKE = "/ugc/toggleCommentLike";

    public static void toggleFeedLike(LifecycleOwner owner, Feed feed) {
        if (!UserManager.getInstance().isLogin()) {
            final LiveData<User> liveData = UserManager.getInstance().login(AppGlobal.getApplication());
            Log.e("InteractionPresenter", "---->" + owner);
            liveData.observe(owner, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        toggleFeedLikeInternal(feed);
                    }
                    liveData.removeObserver(this);
                }
            });
        }
        toggleFeedLikeInternal(feed);
    }

    private static void toggleFeedLikeInternal(Feed feed) {
        ApiService.get(URL_TOGGLE_FEED_LIKE)
                .addParams("userId", UserManager.getInstance().getUserId())
                .addParams("itemId", feed.itemId)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(ApiResponse<JSONObject> response) {
                        if (response.body != null) {
                            boolean hasLiked = response.body.getBoolean("hasLiked");
                            feed.getUgc().setHasLiked(hasLiked);
                        }
                    }
                });
    }

    public static void toggleFeedDiss(LifecycleOwner owner, Feed feed) {
        if (!UserManager.getInstance().isLogin()) {
            final LiveData<User> liveData = UserManager.getInstance().login(AppGlobal.getApplication());
            liveData.observe(owner, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        toggleFeedDissInternal(feed);
                    }
                    liveData.removeObserver(this);
                }
            });
        }
        toggleFeedDissInternal(feed);
    }

    private static void toggleFeedDissInternal(Feed feed) {
        ApiService.get(URL_TOGGLE_FEED_DISS)
                .addParams("userId", UserManager.getInstance().getUserId())
                .addParams("itemId", feed.itemId)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(ApiResponse<JSONObject> response) {
                        if (response.body != null) {
                            boolean hasdiss = response.body.getBoolean("hasdiss");
                            feed.getUgc().setHasdiss(hasdiss);
                        }
                    }
                });
    }

    /**
     * 打开分享弹窗
     *
     * @param context
     * @param feed
     */
    public static void openShare(Context context, Feed feed) {
        String url = "http://h5.aliyun.ppjoke.com/item/%s?timestamp=%s&user_id=%s";
        String content = String.format(url, feed.itemId, new Date().getTime(), UserManager.getInstance().getUserId());
        ShareDialog shareDialog = new ShareDialog(context);
        shareDialog.setShareContent(content);
        shareDialog.setShareItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService.get(URL_SHARE)
                        .addParams("itemId", feed.itemId)
                        .execute(new JsonCallback<JSONObject>() {
                            @Override
                            public void onSuccess(ApiResponse<JSONObject> response) {
                                super.onSuccess(response);
                                if (response.body != null) {
                                    int count = response.body.getIntValue("count");
                                    feed.getUgc().setShareCount(count);
                                }
                            }
                        });
            }
        });
    }

    public static void toggleCommentLike(LifecycleOwner owner, Comment comment) {
        if (!UserManager.getInstance().isLogin()) {
            final LiveData<User> liveData = UserManager.getInstance().login(AppGlobal.getApplication());
            liveData.observe(owner, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        toggleCommentLikeInternal(comment);
                    }
                    liveData.removeObserver(this);
                }
            });
        }
        toggleCommentLikeInternal(comment);
    }

    private static void toggleCommentLikeInternal(Comment comment) {
        ApiService.get(URL_TOGGLE_COMMENT_LIKE)
                .addParams("commentId", comment.commentId)
                .addParams("userId", UserManager.getInstance().getUserId())
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(ApiResponse<JSONObject> response) {
                        if (response.body != null) {
                            boolean hasLiked = response.body.getBooleanValue("hasLiked");
                            comment.getUgc().setHasLiked(hasLiked);

                        }
                    }
                });
    }

    public static void toggleFeedFavorite(LifecycleOwner owner, Feed feed) {
        if (!UserManager.getInstance().isLogin()) {
            final LiveData<User> liveData = UserManager.getInstance().login(AppGlobal.getApplication());
            liveData.observe(owner, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        toggleFeedFavorite(feed);
                    }
                    liveData.removeObserver(this);
                }
            });
        }
        toggleFeedFavorite(feed);
    }

    private static void toggleFeedFavorite(Feed feed) {
        ApiService.get("/ugc/toggleFavorite")
                .addParams("itemId", feed.itemId)
                .addParams("userId", UserManager.getInstance().getUserId())
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(ApiResponse<JSONObject> response) {
                        if (response.body != null) {
                            boolean hasFavorite = response.body.getBooleanValue("hasFavorite");
                            feed.getUgc().setHasFavorite(hasFavorite);
                        }
                    }

                    @Override
                    public void onError(ApiResponse<JSONObject> response) {
                        super.onError(response);
                        showToast(response.message);
                    }
                });
    }

    //关注/取消关注一个用户
    public static void toggleFollowUser(LifecycleOwner owner, Feed feed) {
        if (!UserManager.getInstance().isLogin()) {
            final LiveData<User> liveData = UserManager.getInstance().login(AppGlobal.getApplication());
            liveData.observe(owner, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        toggleFollowUser(feed);
                    }
                    liveData.removeObserver(this);
                }
            });
        }
        toggleFollowUser(feed);
    }

    public static void toggleFollowUser(Feed feed) {
        ApiService.get("/ugc/toggleUserFollow")
                .addParams("followUserId", UserManager.getInstance().getUserId())
                .addParams("userId", feed.author.userId)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(ApiResponse<JSONObject> response) {
                        if (response.body != null) {
                            boolean hasFollow = response.body.getBooleanValue("hasLiked");
                            feed.getAuthor().setHasFollow(hasFollow);
                        }
                    }

                    @Override
                    public void onError(ApiResponse<JSONObject> response) {
                        showToast(response.message);
                    }
                });
    }


    @SuppressLint("RestrictedApi")
    private static void showToast(String message) {
        ArchTaskExecutor.getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AppGlobal.getApplication(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
