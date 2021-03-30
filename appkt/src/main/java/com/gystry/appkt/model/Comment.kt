package com.gystry.appkt.model

import java.io.Serializable

/**
 * @author gystry
 * 创建日期：2021/3/30 11
 * 邮箱：gystry@163.com
 * 描述：
 */
data class Comment(var id: Int = 0,
                   var itemId: Long = 0,
                   var commentId: Long = 0,
                   var userId: Long = 0,
                   var commentType: Int = 0,
                   var createTime: Long = 0,
                   var commentCount: Int = 0,
                   var likeCount: Int = 0,
                   var commentText: String,
                   var imageUrl: String,
                   var videoUrl: String,
                   var width: Int = 0,
                   var height: Int = 0,
                   var hasLiked: Boolean = false,
                   var author: User,
                   var ugc: Ugc,): Serializable {
}