package com.gystry.pjetpack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.alibaba.fastjson.JSONObject;
import com.gystry.libcommon.AppGlobal;
import com.gystry.libcommon.extention.LiveDataBus;
import com.gystry.libnetwork.ApiResponse;
import com.gystry.libnetwork.ApiService;
import com.gystry.libnetwork.JsonCallback;
import com.gystry.pjetpack.model.Comment;
import com.gystry.pjetpack.model.Feed;
import com.gystry.pjetpack.model.TagList;
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

    public static final String DATA_FROM_INTERACTION = "data_from_interaction";
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
                            LiveDataBus.getInstance().with(DATA_FROM_INTERACTION).postValue(feed);
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
                            LiveDataBus.getInstance().with(DATA_FROM_INTERACTION).postValue(feed);
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
                            LiveDataBus.getInstance().with(DATA_FROM_INTERACTION).postValue(feed);
                        }
                    }

                    @Override
                    public void onError(ApiResponse<JSONObject> response) {
                        showToast(response.message);
                    }
                });
    }

    public static LiveData<Boolean> deleteFeed(Context context, long itemId) {
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        new AlertDialog.Builder(context)
                .setNegativeButton("删除", (dialog, which) -> {
                    dialog.dismiss();
                    deleteFeedInternal(liveData, itemId);
                }).setPositiveButton("取消", (dialog, which) -> dialog.dismiss()).setMessage("确定要删除这条评论吗？").create().show();
        return liveData;
    }

    private static void deleteFeedInternal(MutableLiveData<Boolean> liveData, long itemId) {
        ApiService.get("/feeds/deleteFeed")
                .addParams("itemId", itemId)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(ApiResponse<JSONObject> response) {
                        if (response.body != null) {
                            boolean success = response.body.getBoolean("result");
                            liveData.postValue(success);
                            showToast("删除成功");
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


    //关注/取消关注一个帖子标签
    public static void toggleTagLike(LifecycleOwner owner, TagList tagList) {
        if (!UserManager.getInstance().isLogin()) {
            final LiveData<User> liveData = UserManager.getInstance().login(AppGlobal.getApplication());
            liveData.observe(owner, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        toggleTagLikeInternal(tagList);
                    }
                    liveData.removeObserver(this);
                }
            });
        }
        toggleTagLikeInternal(tagList);
    }

    private static void toggleTagLikeInternal(TagList tagList) {
        ApiService.get("/tag/toggleTagFollow")
                .addParams("tagId", tagList.tagId)
                .addParams("userId", UserManager.getInstance().getUserId())
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(ApiResponse<JSONObject> response) {
                        if (response.body != null) {
                            Boolean follow = response.body.getBoolean("hasFollow");
                            tagList.setHasFollow(follow);
                        }
                    }

                    @Override
                    public void onError(ApiResponse<JSONObject> response) {
                        showToast(response.message);
                    }
                });
    }
}
