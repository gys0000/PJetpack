package com.gystry.libnetwork.cache;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * @author gystry
 * 创建日期：2020/7/7 16
 * 邮箱：gystry@163.com
 * 描述：Entity 注解的作用是将一个class对象映射成数据库中一个数据表
 */
@Entity(tableName = "cache"
        //foreignKeys外键 这里将cache中的key和User中的id字段进行关联，并设置当cache中的key被删除和被更新时，user中的id的状态操作
//        ,foreignKeys = {@ForeignKey(entity = User.class,parentColumns = "id",childColumns = "key",onDelete = ForeignKey.RESTRICT,onUpdate = ForeignKey.RESTRICT)}
        )
public class Cache implements Serializable {
    //PrimaryKey 主键约束
    @PrimaryKey
    @NonNull
    public String key;

    //在映射到数据库中的时候，修改列的名字
//    @ColumnInfo(name = "_data")
//    @Ignore  这个注解表示在生成数据表的时候不将被注解的字段设置为列名
    public byte[] data;

    //Embedded 注解的作用是，当cache被映射到数据表的时候，user中的字段也会被映射到cache表中
//    @Embedded
//    public User user;
}
