package com.gystry.libnetworkkt.cache

import androidx.room.*

/**
 * @author gystry
 * 创建日期：2021/3/18 18
 * 邮箱：gystry@163.com
 * 描述：
 */
@Dao
open interface CacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
   open fun save(cache: Cache): Long

    @Query("select * from cache where `key`=:key")
    open fun getCache(key: String): Cache?

//    @RawQuery()
//    fun getCacheBySql(key: String): Cache
//  @Transaction
//    @RawQuery(observedEntities = {UserRelateDevice.class})
//    LiveData<List<DeviceAndManufacturer>> rawQueryDevice(SupportSQLiteQuery query);
//

    @Delete
    open fun delete(cache: Cache): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    open fun update(cache: Cache): Int
}