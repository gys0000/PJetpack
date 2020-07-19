package com.gystry.libnetwork.cache;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.gystry.libcommon.AppGlobal;

/**
 * @author gystry
 * 创建日期：2020/7/7 15
 * 邮箱：gystry@163.com
 * 描述：  exportSchema = true 同意导出json文件，文件中包含所有的sql语句，和所有的数据
 */
@Database(entities = {Cache.class}, version = 1,exportSchema = true)
public abstract class CacheDatabase extends RoomDatabase {
    private static final CacheDatabase database;

    static {
        //创建一个内存数据库，这种数据库只存在于内存中，进程被杀，数据随之丢失
//        Room.inMemoryDatabaseBuilder()
        //第三个参数是数据库的名称
        database = Room.databaseBuilder(AppGlobal.getApplication(), CacheDatabase.class, "gystry_cache")
                //是否允许在主线程进行查询
                .allowMainThreadQueries()
                //数据库创建和打开后的回调
//                .addCallback()
                //设置查询的线程池
//                .setQueryExecutor()
//                .openHelperFactory()
                //room的日志模式
//                .setJournalMode()
                //数据库升级异常之后的回滚
//                .fallbackToDestructiveMigration()
                //数据库升级异常后根据指定版本进行回滚
//                .fallbackToDestructiveMigrationFrom()
                //数据库升级操作的入口  当数据库要升级的时候，必须要提供Migration
//                .addMigrations(CacheDatabase.sMigration)
                .build();
    }

    public abstract CacheDao getCache();

    public static CacheDatabase getDatabase() {
        return database;
    }

    //两个参数分别代表 当前数据库版本号和要升级到的数据库版本号
//    static Migration sMigration = new Migration(1, 3) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("alter table teacher rename to student");
//            database.execSQL("alter table teacher add column teacher_age INTEGER NOT NULL default 0 ");
//        }
//    };
}
