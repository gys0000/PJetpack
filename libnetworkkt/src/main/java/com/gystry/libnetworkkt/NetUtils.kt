package com.gystry.libnetworkkt

import java.net.URLEncoder

/**
 * @author gystry
 * 创建日期：2021/3/18 11
 * 邮箱：gystry@163.com
 * 描述：
 */
fun createUrlFromParams(url: String, params: Map<String, Any>):String {
    val strBuilder = StringBuilder()
    strBuilder.append(url)
    if (url.indexOf("?") > 0 || url.indexOf("&") > 0) {
            strBuilder.append("&")
    }else{
        strBuilder.append("?")
    }
    for (entry in params.entries) {
        val value = URLEncoder.encode(entry.value.toString(), "UTF-8")
        strBuilder.append(entry.key).append("=").append(value).append("&")
    }
    strBuilder.deleteCharAt(strBuilder.length-1)
    return strBuilder.toString()
}
