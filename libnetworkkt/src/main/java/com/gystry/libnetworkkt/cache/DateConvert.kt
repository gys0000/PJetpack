package com.gystry.libnetworkkt.cache

import androidx.room.TypeConverter
import java.util.*

/**
 * @author gystry
 * 创建日期：2021/3/18 18
 * 邮箱：gystry@163.com
 * 描述：
 */
object DateConvert {
    @TypeConverter
    fun date2Long(date: Date):Long=date.time

    @TypeConverter
    fun long2Date(date:Long):Date= Date(date)
}