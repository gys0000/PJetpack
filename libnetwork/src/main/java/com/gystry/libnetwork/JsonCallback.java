package com.gystry.libnetwork;

/**
 * @author gystry
 * 创建日期：2020/7/3 17
 * 邮箱：gystry@163.com
 * 描述：
 */
public abstract class JsonCallback<T> {
    public void onSuccess(ApiResponse<T> response) {
    }

    public void onError(ApiResponse<T> response) {
    }

    public void onCacheSuccess(ApiResponse<T> response) {
    }
}
