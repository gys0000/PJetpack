package com.gystry.appkt.model

/**
 * @author gystry
 * 创建日期：2021/3/29 15
 * 邮箱：gystry@163.com
 * 描述：
 */
data class BottomBar(
        var activeColor: String,
        var inActiveColor: String,
        var selectTab: Int,
        var tabs: MutableList<Tabs>
) {
    data class Tabs(
            var size: Int ,
            var enable: Boolean ,
            var index: Int,
            var pageUrl: String,
            var title: String,
            var tintColor: String
    )
}