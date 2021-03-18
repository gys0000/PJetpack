package com.gystry.libnetwork.cache;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * @author gystry
 * 创建日期：2020/7/7 19
 * 邮箱：gystry@163.com
 * 描述：被Dao注解，则可以操作数据库数据
 */
@Dao
public interface CacheDao {

    /**
     * 注解中的内容是发生冲突时候的策略
     * @param cache
     * @return
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save(Cache cache);

    /**
     * 注解中sql语句里边的cache是我们创建的数据库名称，key是数据库中字段名
     * RawQuery 的使用是在调用的时候传入sql语句
     * @param key
     * @return
     */
    @Query("select * from cache where `key`=:key")
    Cache getCache(String key);

    @Delete
    int delete(Cache cache);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(Cache cache);
}
