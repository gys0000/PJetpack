package com.gystry.libnetworkkt

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * @author gystry
 * 创建日期：2021/3/18 15
 * 邮箱：gystry@163.com
 * 描述：
 */
object ApiService {
    public val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
    }
    public var sBaseUrl: String? = null
    public lateinit var sConvert: Convert<*>

    init {
        //配置网络信任所有的证书
        val trustManagers = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {

            }

            override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {

            }

            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                return arrayOfNulls<X509Certificate>(0)
            }

        })

        var ssl: SSLContext? = null
        try {
            ssl = SSLContext.getInstance("SSL")
            ssl.init(null, trustManagers, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(ssl.socketFactory)
            HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun init(baseUrl: String, convert: Convert<*>?) {
        var convertS = convert
        sBaseUrl = baseUrl
        if (convertS == null) {
            convertS = JsonConvert<Any>()
        }
        sConvert = convertS
    }


    //运算符重载
    operator fun <T> get(url: String): GetRequest<T> {
        return GetRequest(sBaseUrl + url)
    }

    fun <T> post(url:String):PostRequest<T>{
        return PostRequest(sBaseUrl+url)
    }

}

