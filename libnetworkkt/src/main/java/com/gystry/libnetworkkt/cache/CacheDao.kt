package com.gystry.libnetworkkt.cache

import androidx.room.*

/**
 * @author gystry
 * 创建日期：2021/3/18 18
 * 邮箱：gystry@163.com
 * 描述：
 */
@Dao
interface CacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(cache: Cache): Long

    @Query("select * from cache where 'key'=:key")
    fun getCache(key: String): Cache?

//    @RawQuery()
//    fun getCacheBySql(key: String): Cache
//  @Transaction
//    @RawQuery(observedEntities = {UserRelateDevice.class})
//    LiveData<List<DeviceAndManufacturer>> rawQueryDevice(SupportSQLiteQuery query);
//

    @Delete
    fun delete(cache: Cache): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(cache: Cache): Int
}