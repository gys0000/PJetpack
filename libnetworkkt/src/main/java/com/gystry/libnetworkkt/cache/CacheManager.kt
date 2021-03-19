package com.gystry.libnetworkkt.cache

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * @author gystry
 * 创建日期：2021/3/19 10
 * 邮箱：gystry@163.com
 * 描述：
 */
object CacheManager {
    fun <T> save(cacheKey: String, body: T){
        val cache=Cache()
        cache.key=cacheKey
        cache.data= toByteArray(body)
        CacheDatabase.getDataBase().getCache().save(cache)
    }

    fun getCache(key:String):Any?{
        val cache = CacheDatabase.getDataBase().getCache().getCache(key)
        if (cache!=null&&cache.data!=null) {
            return toObject(cache.data!!)
        }
        return null
    }

    fun <T> delete(key:String,body:T){
        val cache = Cache()
        cache.data= toByteArray(body)
        cache.key=key
        CacheDatabase.getDataBase().getCache().delete(cache)
    }


    /**
     * 反序列化，将二进制数据转换成Any对象
     */
    private fun toObject(data: ByteArray): Any? {
        try {
          ByteArrayInputStream(data).use {
              ObjectInputStream(it).use { it1 ->
                  return it1.readObject()
              }
          }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 序列化，将对象转换成二进制数据
     */
    private fun <T> toByteArray(body:T):ByteArray{
        try {
            ByteArrayOutputStream().use { baos->
                ObjectOutputStream(baos).use {
                    it.writeObject(body)
                    it.flush()
                    return baos.toByteArray()
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return ByteArray(0)
    }
}