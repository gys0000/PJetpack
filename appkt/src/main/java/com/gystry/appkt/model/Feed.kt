package com.gystry.appkt.model

import java.io.Serializable

/**
 * @author gystry
 * 创建日期：2021/3/30 11
 * 邮箱：gystry@163.com
 * 描述：
 */
data class Feed(var id: Int = 0,
                var itemId: Long = 0,
                var itemType: Int = 0,
                var createTime: Long = 0,
                var duration: Double = 0.0,
                var feeds_text: String,
                var authorId: Long = 0,
                var activityIcon: String,
                var activityText: String,
                var width: Int = 0,
                var height: Int = 0,
                var url: String,
                var cover: String,

                var author: User,
                var topComment: Comment,
                var ugc: Ugc):Serializable {
}