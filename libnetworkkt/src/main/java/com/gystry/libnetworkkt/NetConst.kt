package com.gystry.libnetworkkt

/**
 * @author gystry
 * 创建日期：2021/3/18 14
 * 邮箱：gystry@163.com
 * 描述：
 */
//只访问缓存，即便缓存不存在，也不访问网络
const val CACHE_ONLY=1
//先访问缓存，同时发起网络请求，成功后缓存到本地
const val CACHE_FIRST=2
//只访问网络，不做任何存储
const val NET_ONLY=3
//先访问网络，成功后缓存到本地
const val NET_CACHE=4