package com.gystry.libnetwork;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;

import com.gystry.libnetwork.cache.CacheManager;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author gystry
 * 创建日期：2020/7/3 14
 * 邮箱：gystry@163.com
 * 描述：T 这个泛型指的是response的实体类类型，R 表示Request的子类
 */
public abstract class Request<T, R extends Request> implements Cloneable{

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
    private Type mType;
    private Class mClaz;
    private int mCacheStrategy;

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
        if (value==null) {
            return ((R) this);
        }
        try {
            if (value.getClass()==String.class) {
                params.put(key,value);
            }
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
     * 参数中使用@CahceStrategy注解的作用是，这个方法被调用的时候，传的参数必须是这个注解限制的几种数据
     * @param cacheStrategy
     * @return
     */
    public R cacheStrategy(@CahceStrategy int cacheStrategy) {
        mCacheStrategy = cacheStrategy;
        return (R) this;
    }

    /**
     * 解决范型擦除的问题
     *
     * @param type
     * @return
     */
    public R responseType(Type type) {
        mType = type;
        return (R) this;
    }

    public R responseType(Class claz) {
        mClaz = claz;
        return (R) this;
    }

    /**
     * 真正得网络请求，参数为callback的话就为异步，没有call就为同步
     */
    @SuppressLint("RestrictedApi")
    public void execute(final JsonCallback<T> callback) {
        if (mCacheStrategy != NET_ONLY) {
            ArchTaskExecutor.getIOThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    ApiResponse<T> response = readCache();
                    if (callback!=null) {
                        callback.onCacheSuccess(response);
                    }
                }
            });
        }
        if (mCacheStrategy!=CACHE_ONLY) {
            getCall().enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    ApiResponse<T> response = new ApiResponse<>();
                    response.message = e.getMessage();
                    callback.onError(response);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    ApiResponse<T> apiResponse = parseResponse(response, callback);
                    if (apiResponse.success) {
                        callback.onSuccess(apiResponse);
                    } else {
                        callback.onError(apiResponse);
                    }

                }
            });
        }
    }

    private ApiResponse<T> readCache() {
        String key = TextUtils.isEmpty(cacheKey) ? generateCacheKey() : cacheKey;
        Object cache = CacheManager.getCache(key);
        ApiResponse<T> result=new ApiResponse<>();
        result.status=304;
        result.message="缓存获取成功";
        result.body= (T) cache;
        result.success=true;
        return result;
    }

    private ApiResponse<T> parseResponse(Response response, JsonCallback<T> callback) {
        String message = null;
        int status = response.code();
        boolean success = response.isSuccessful();
        ApiResponse<T> result = new ApiResponse<>();
        Convert convert = ApiService.sConvert;
        try {
            String content = response.body().string();
            if (success) {
                if (callback != null) {
                    ParameterizedType type = (ParameterizedType) callback.getClass().getGenericSuperclass();
                    Type argument = type.getActualTypeArguments()[0];
                    result.body = (T) convert.convert(content, argument);
                } else if (mType != null) {
                    result.body = (T) convert.convert(content, mType);
                } else if (mClaz != null) {
                    result.body = (T) convert.convert(content, mClaz);
                } else {
                    Log.e("request", "parseResponse 无法解析");
                }
            } else {
                message = content;
            }
        } catch (Exception e) {
            message = e.getMessage();
            success = false;
        }
        result.success = success;
        result.message = message;
        result.status = status;
        if (mCacheStrategy != NET_ONLY && result.success && result.body != null && result.body instanceof Serializable) {
            saveCache(result.body);
        }
        return result;
    }

    private void saveCache(T body) {
        String key = TextUtils.isEmpty(cacheKey) ? generateCacheKey() : cacheKey;
        CacheManager.save(key,body);
    }

    private String generateCacheKey() {
        cacheKey = UrlCreater.createUrlFromParams(mUrl, params);
        return cacheKey;
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

    /**
     * 同步请求
     */
    public ApiResponse<T> execute() {
        if (mCacheStrategy==CACHE_ONLY) {
            return readCache();
        }
        ApiResponse<T> result = null;
        try {
            Response response = getCall().execute();
             result = parseResponse(response, null);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            if (result==null) {
                result=new ApiResponse<>();
                result.message=e.getMessage();
            }
        }
        return result;
    }

    @NonNull
    @Override
    public Request clone() throws CloneNotSupportedException {
        return ((Request<T, R>) super.clone());
    }
}
