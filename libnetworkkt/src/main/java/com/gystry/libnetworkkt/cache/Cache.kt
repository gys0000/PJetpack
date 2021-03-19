package com.gystry.libnetworkkt.cache

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * @author gystry
 * 创建日期：2021/3/18 18
 * 邮箱：gystry@163.com
 * 描述：
 */
@Entity(tableName = "cache")
data class Cache(@PrimaryKey(autoGenerate = false)
                 @NonNull
                 public var key: String,
                 public var data: ByteArray?) : Serializable
