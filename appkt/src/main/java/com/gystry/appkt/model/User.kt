package com.gystry.appkt.model

import android.text.TextUtils
import java.io.Serializable

/**
 * @author gystry
 * 创建日期：2021/3/30 11
 * 邮箱：gystry@163.com
 * 描述：
 */
data class User(var id: Int = 0,
                var userId: Long = 0,
                var name: String,
                var avatar: String,
                var description: String,
                var likeCount: Int = 0,
                var topCommentCount: Int = 0,
                var followCount: Int = 0,
                var followerCount: Int = 0,
                var qqOpenId: String,
                var expires_time: Long = 0,
                var score: Int = 0,
                var historyCount: Int = 0,
                var commentCount: Int = 0,
                var favoriteCount: Int = 0,
                var feedCount: Int = 0,
                var hasFollow: Boolean = false):Serializable {
    override fun equals(obj: Any?): Boolean {
        if (obj == null || obj !is User) return false
        val newUser:User = obj as User
        return (TextUtils.equals(name, newUser.name)
                && TextUtils.equals(avatar, newUser.avatar)
                && TextUtils.equals(description, newUser.description)
                && likeCount == newUser.likeCount && topCommentCount == newUser.topCommentCount && followCount == newUser.followCount && followerCount == newUser.followerCount && qqOpenId != null && newUser.qqOpenId != null && qqOpenId == newUser.qqOpenId
                && expires_time == newUser.expires_time && score == newUser.score && historyCount == newUser.historyCount && commentCount == newUser.commentCount && favoriteCount == newUser.favoriteCount && feedCount == newUser.feedCount && hasFollow == newUser.hasFollow)
    }
}