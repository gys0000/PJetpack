package com.gystry.libnetworkkt

import okhttp3.FormBody

/**
 * @author gystry
 * 创建日期：2021/3/18 15
 * 邮箱：gystry@163.com
 * 描述：
 */
class PostRequest<T>(url:String):Request<T,PostRequest<T>>(url) {
    override fun generateRequest(builder: okhttp3.Request.Builder): okhttp3.Request {
        val bodyBuilder = FormBody.Builder()
        for (entry in params.entries) {
            bodyBuilder.add(entry.key,entry.value.toString())
        }
        return builder.url(mUrl).post(bodyBuilder.build()).build()
    }
}