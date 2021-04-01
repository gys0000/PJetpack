package com.gystry.appkt.model

import java.io.Serializable

/**
 * @author gystry
 * 创建日期：2021/3/30 11
 * 邮箱：gystry@163.com
 * 描述：
 */
data class Ugc(var likeCount: Int = 0,
               var shareCount: Int = 0,
               var commentCount: Int = 0,
               var hasFavorite: Boolean = false,
               var hasLiked: Boolean = false,
               var hasdiss: Boolean = false,
               var hasDissed: Boolean = false) :Serializable{
    override fun equals(obj: Any?): Boolean {
        if (obj == null || obj !is Ugc) return false
        val (likeCount1, shareCount1, commentCount1, hasFavorite1, hasLiked1, hasdiss1) = obj
        return likeCount == likeCount1 && shareCount == shareCount1 && commentCount == commentCount1 && hasFavorite == hasFavorite1 && hasLiked == hasLiked1 && hasdiss == hasdiss1
    }
}