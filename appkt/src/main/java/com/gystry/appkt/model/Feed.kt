package com.gystry.appkt.model

import android.text.TextUtils
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
    companion object{
        const val TYPE_IMAGE=1
        const val TYPE_VIDEO=2
    }
    override fun equals(@androidx.annotation.Nullable other : Any?): Boolean {
        if (other == null || other !is Feed) return false
        val newFeed:Feed = other
        return (id == newFeed.id && itemId == newFeed.itemId && itemType == newFeed.itemType && createTime == newFeed.createTime && duration == newFeed.duration
                && TextUtils.equals(feeds_text, newFeed.feeds_text)
                && authorId == newFeed.authorId && TextUtils.equals(activityIcon, newFeed.activityIcon)
                && TextUtils.equals(activityText, newFeed.activityText)
                && width ==newFeed. width && height == newFeed.height && TextUtils.equals(url, newFeed.url)
                && TextUtils.equals(cover, newFeed.cover)
                && author != null && newFeed.author != null && author == newFeed.author
                && topComment != null && topComment == newFeed.topComment
                && ugc != null && ugc == newFeed.ugc)
    }
}