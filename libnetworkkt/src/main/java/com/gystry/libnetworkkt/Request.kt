package com.gystry.libnetworkkt

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Log
import androidx.annotation.IntDef
import androidx.arch.core.executor.ArchTaskExecutor
import com.alibaba.fastjson.JSONObject
import com.gystry.libnetworkkt.cache.CacheManager
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.io.Serializable
import java.lang.annotation.RetentionPolicy
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author gystry
 * 创建日期：2021/3/18 11
 * 邮箱：gystry@163.com
 * 描述：
 */

abstract class Request<T, R : Request<T, R>>(url: String) : Cloneable {
    protected val headers = HashMap<String, String>()
    protected val params = HashMap<String, Any>()

    private var cacheKey: String? = null

    private var mType: Type? = null
    private var mClaz: Class<*>? = null
    private var mCacheStrategy: Int = 0
    public var mUrl = url

    @IntDef(CACHE_ONLY, CACHE_FIRST, NET_CACHE, NET_ONLY)
    public annotation class CacheStrategy

    public fun addHeader(key: String, value: String): R {
        headers[key] = value
        return this as R
    }

    public fun addParams(key: String, value: Any?): R {
        if (value == null) {
            return this as R
        }
        try {
            if (value.javaClass == String::class.java) {
                params[key] = value
            }
            var field = value.javaClass.getField("TYPE")
            var clazz = field.get(null) as Class<*>
            if (clazz.isPrimitive) {
                params[key] = value
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return this as R
    }

    public fun cacheKey(key: String): R {
        cacheKey = key
        return this as R
    }

    public fun cacheStrategy(@CacheStrategy cacheStrategy: Int): R {
        mCacheStrategy = cacheStrategy
        return this as R
    }

    public fun responseType(type: Type): R {
        mType = type
        return this as R
    }

    public fun responseType(claz: Class<*>): R {
        mClaz = claz
        return this as R
    }

    @SuppressLint("RestrictedApi")
    @JvmName("execute")
    public fun execute(callback: JsonCallback<T>?) {
        if (mCacheStrategy != NET_ONLY) {
            //缓存模式不是只使用网络，那么是可以使用本地缓存，那么就先把本地缓存拿出来使用
            ArchTaskExecutor.getIOThreadExecutor().execute{
                val response = readCache()
                callback?.onCacheSuccess(response)
            }
        }

        if (mCacheStrategy != CACHE_ONLY) {
            //缓存模式不是只有缓存，那么就可以使用网络
            getCall().enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    var apiResponse = ApiResponse<T>()
                    apiResponse.message = e.message.toString()
                    callback?.onError(apiResponse)
                }

                override fun onResponse(call: Call, response: Response) {
                    var parsrResponse = parsrResponse(response, callback)
                    if (parsrResponse.success) {
                        callback?.onSuccess(parsrResponse)
                    } else {
                        callback?.onError(parsrResponse)
                    }
                }

            })
        }
    }

    public fun execute(): ApiResponse<T> {
        if (mCacheStrategy == CACHE_ONLY) {
            return readCache()
        }
        var result: ApiResponse<T>? = null
        try {
            var response = getCall().execute()
            result = parsrResponse(response, null)
            return result!!
        } catch (e: IOException) {
            e.printStackTrace()
            if (result == null) {
                result = ApiResponse()
                result.message = e.message.toString()
            }
        }
        return result!!
    }

    private fun parsrResponse(response: Response, callback: JsonCallback<T>?): ApiResponse<T> {
        var message: String? = null
        val status = response.code
        var successful = response.isSuccessful
        var result = ApiResponse<T>()
        var sConvert = ApiService.sConvert
        try {
            var content = response.body.toString()
            if (successful) {
                if (callback != null) {
                    var type = callback.javaClass.genericSuperclass as ParameterizedType
                    var argument = type.actualTypeArguments[0]
                    result.body = sConvert.convert(content, argument) as T
                } else if (mType != null){
                    result.body = sConvert.convert(content, mType!!) as T
                }else if(mClaz!=null){
                    result.body=sConvert.convert(content, mClaz!!) as T
                }else{
                    Log.e("request", "parseResponse 无法解析")
                }
            }else{
                message=content
            }
        } catch (e: Exception) {
            e.printStackTrace()
            message=e.message.toString()
            successful=false
        }
        result.success=successful
        result.message= message!!
        result.status=status
        if (mCacheStrategy!= NET_ONLY&&result.success&&result.body!=null&&result.body is Serializable) {
            saveCache(result.body!!)
        }
        return result
    }

    private fun saveCache(body: T){
       var key= if (cacheKey.isNullOrEmpty()) {
            generateCacheKey()
        }else{
            cacheKey
        }
        CacheManager.save(key!!, body)
    }

    private fun readCache():ApiResponse<T>{
      val key=  if (cacheKey.isNullOrEmpty()) {
            generateCacheKey()
        }else{
            cacheKey
        }
        var cache = CacheManager.getCache(key!!)
        val result=ApiResponse<T>()
        result.success=true
        result.status=304
        result.message="缓存获取成功"
        result.body=cache as T
        return result
    }

    private fun generateCacheKey(): String {
        cacheKey =createUrlFromParams(mUrl, params)
        return cacheKey as String
    }

    private fun getCall(): okhttp3.Call {
        val builder = okhttp3.Request.Builder()
        addHeader(builder)
        val request = generateRequest(builder)
        return ApiService.okHttpClient.newCall(request)
    }

    abstract fun generateRequest(builder: okhttp3.Request.Builder): okhttp3.Request

    private fun addHeader(builder: okhttp3.Request.Builder) {
        for (entry in headers.entries) {
            builder.addHeader(entry.key, entry.value)
        }
    }

    override public fun clone(): Request<*, *> {
        return super.clone() as Request<*, *>
    }
}