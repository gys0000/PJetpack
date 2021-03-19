package com.gystry.libnetworkkt

/**
 * @author gystry
 * 创建日期：2021/3/18 11
 * 邮箱：gystry@163.com
 * 描述：
 */
abstract class JsonCallback<T> {
    open fun onSuccess(response:ApiResponse<T>?){}
    open fun onError(response:ApiResponse<T>?){}
    open fun onCacheSuccess(response:ApiResponse<T>){}
}