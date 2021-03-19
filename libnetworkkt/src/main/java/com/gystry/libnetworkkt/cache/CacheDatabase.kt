package com.gystry.libnetworkkt.cache

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gystry.libcommon.AppGlobal

/**
 * @author gystry
 * 创建日期：2021/3/18 18
 * 邮箱：gystry@163.com
 * 描述：
 */
@Database(entities = [Cache::class], version = 1, exportSchema = true)
public abstract class CacheDatabase : RoomDatabase() {

    companion object {
        public val database: CacheDatabase =
                Room.databaseBuilder(AppGlobal.getApplication(), CacheDatabase::class.java,
                        "gystry_cache_kt")
                        .allowMainThreadQueries()
                        .build()

        public fun getDataBase(): CacheDatabase = database
    }

    abstract fun getCache(): CacheDao
}

