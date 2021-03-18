package com.gystry.libnetworkkt

/**
 * @author gystry
 * 创建日期：2021/3/18 15
 * 邮箱：gystry@163.com
 * 描述：
 */
class GetRequest<T>(url:String):Request<T,GetRequest<T>>(url) {
    override fun generateRequest(builder: okhttp3.Request.Builder): okhttp3.Request {
        val urlFormParams = createUrlFromParams(mUrl, params)
        return builder.url(urlFormParams).get().build()
    }
}