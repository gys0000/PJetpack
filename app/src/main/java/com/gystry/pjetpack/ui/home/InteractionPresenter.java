package com.gystry.pjetpack.ui.home;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.alibaba.fastjson.JSONObject;
import com.gystry.libcommon.AppGlobal;
import com.gystry.libnetwork.ApiResponse;
import com.gystry.libnetwork.ApiService;
import com.gystry.libnetwork.JsonCallback;
import com.gystry.pjetpack.model.Feed;
import com.gystry.pjetpack.model.User;
import com.gystry.pjetpack.ui.login.UserManager;

import org.json.JSONException;

/**
 * @author gystry
 * 创建日期：2020/7/16 11
 * 邮箱：gystry@163.com
 * 描述：
 */
public class InteractionPresenter {

    public static final String URL_TOGGLE_FEED_LIKE = "/ugc/toggleFeedLike";
    public static final String URL_TOGGLE_FEED_DISS = "/ugc/dissFeed";

    public static void toggleFeedLike(LifecycleOwner owner, Feed feed) {
        if (!UserManager.getInstance().isLogin()) {
            final LiveData<User> liveData = UserManager.getInstance().login(AppGlobal.getApplication());
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
                            boolean hasdiss= response.body.getBoolean("hasdiss");
                            feed.getUgc().setHasdiss(hasdiss);
                        }
                    }
                });
    }

    public static void openShare(Context context,Feed feed){
        
    }
}
