package com.gystry.appkt.model

/**
 * @author gystry
 * 创建日期：2021/3/29 10
 * 邮箱：gystry@163.com
 * 描述：
 */
data class Destination(var isFragment: Boolean,
                       var claszName: String,
                       var asStater: Boolean,
                       var needLogin: Boolean,
                       var pageUrl: String,
                       var id: Int)