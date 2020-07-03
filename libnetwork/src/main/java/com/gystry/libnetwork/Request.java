package com.gystry.libnetwork;

import androidx.annotation.IntDef;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * @author gystry
 * 创建日期：2020/7/3 14
 * 邮箱：gystry@163.com
 * 描述：T 这个泛型指的是response的实体类类型，R 表示Request的子类
 */
public abstract class Request<T, R extends Request> {

    public String mUrl;
    protected HashMap<String, String> headers = new HashMap<>();
    protected HashMap<String, Object> params = new HashMap<>();

    //只访问缓存，即便缓存不存在，也不访问网络
    public static final int CACHE_ONLY = 1;
    //先访问缓存，同时发起网络请求，成功后缓存到本地
    public static final int CACHE_FIRST = 2;
    //只访问网络，不做任何存储
    public static final int NET_ONLY = 3;
    //先访问网络，成功后缓存到本地
    public static final int NET_CACHE = 4;
    private String cacheKey;

    //使用注解标注数据访问类型
    @IntDef({CACHE_ONLY, CACHE_FIRST, NET_CACHE, NET_ONLY})
    public @interface CahceStrategy {

    }

    public Request(String url) {
        mUrl = url;
    }

    /**
     * 添加header
     *
     * @param key
     * @param value
     * @return
     */
    public R addHeader(String key, String value) {
        headers.put(key, value);
        return (R) this;
    }

    /**
     * 添加参数
     *
     * @param key
     * @param value
     * @return
     */
    public R addParams(String key, Object value) {
        try {
            //判断value是不是基本数据类型
            Field field = value.getClass().getField("TYPE");
            Class claz = (Class) field.get(null);
            if (claz.isPrimitive()) {
                params.put(key, value);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (R) this;
    }

    public R cacheKey(String key) {
        cacheKey = key;
        return (R) this;
    }

    /**
     * 真正得网络请求，参数为callback的话就为异步，没有call就为同步
     */
    public void execute(JsonCallback<T> callback) {
        getCall();
    }

    private Call getCall() {
        final okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        addHeaders(builder);
        okhttp3.Request request = generateRequest(builder);
        final Call call = ApiService.okHttpClient.newCall(request);
        return call;
    }

    protected abstract okhttp3.Request generateRequest(okhttp3.Request.Builder builder);

    private void addHeaders(okhttp3.Request.Builder builder) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
    }

    public void execute() {
    }
}
