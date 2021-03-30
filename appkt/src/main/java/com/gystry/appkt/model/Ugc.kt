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
}