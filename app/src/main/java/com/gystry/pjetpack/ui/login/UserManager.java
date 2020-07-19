package com.gystry.pjetpack.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gystry.libnetwork.cache.CacheManager;
import com.gystry.pjetpack.model.User;

public class UserManager {
    public static final String KEY_CACHE_USER = "cache_user";
    private static UserManager mUserManager = new UserManager();
    private MutableLiveData<User> userLiveData ;
    private User mUser;

    public static UserManager getInstance(){
        return mUserManager;
    }

    private UserManager() {
        User cache = (User) CacheManager.getCache(KEY_CACHE_USER);
        if (cache!=null&&cache.expires_time>System.currentTimeMillis()) {
            mUser=cache;
        }
    }

    public void save(User user) {
        mUser = user;
        CacheManager.save(KEY_CACHE_USER, user);
        //先判断这个livedata有没有注册观察者，如果注册了观察者的话，就post出去了，如果没有注册的话，就没必要抛出去
        if (userLiveData.hasObservers()) {
            userLiveData.postValue(user);
        }
    }

    /**
     * 这个方法返回值是livedata，而不是mutablelivedata，是因为不想让方法调用者拿到mutablelivedata对象之后发送数据
     *
     * @param context
     * @return
     */
    public LiveData<User> login(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        return getUserLiveData();
    }

    public boolean isLogin() {
        return mUser != null && mUser.expires_time > System.currentTimeMillis();
    }

    public User getUser() {
        return isLogin() ? mUser : null;
    }

    public long getUserId() {
        return isLogin() ? mUser.userId : 0;
    }
    private MutableLiveData<User> getUserLiveData() {
        if (userLiveData == null) {
            userLiveData = new MutableLiveData<>();
        }
        return userLiveData;
    }
}
