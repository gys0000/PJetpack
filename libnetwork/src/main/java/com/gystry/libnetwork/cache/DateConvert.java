package com.gystry.libnetwork.cache;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * @author gystry
 * 创建日期：2020/7/7 21
 * 邮箱：gystry@163.com
 * 描述：
 */
public class DateConvert {
    @TypeConverter
    public static Long date2Long(Date date){
        return date.getTime();
    }

    @TypeConverter
    public static Date long2Date(Long date){
        return new Date(date);
    }
}
