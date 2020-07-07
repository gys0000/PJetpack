package com.gystry.libnetwork.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CacheManager {

    public static <T> void save(String cacheKey, T body) {
        Cache cache = new Cache();
        cache.key = cacheKey;
        cache.data = toByteArray(body);
        CacheDatabase.getDatabase().getCache().save(cache);
    }

    public static Object getCache(String key) {
        Cache cache = CacheDatabase.getDatabase().getCache().getCache(key);
        if (cache != null && cache.data != null) {
            return toObject(cache.data);
        }
        return null;
    }

    public static <T> void delete(String key, T body) {
        Cache cache = new Cache();
        cache.key = key;
        cache.data = toByteArray(body);
        CacheDatabase.getDatabase().getCache().delete(cache);
    }

    /**
     * 反序列，将二进制数据转换成object对象
     *
     * @param data
     * @return
     */
    private static Object toObject(byte[] data) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 序列化存储数据需要转换成二进制
     *
     * @param body
     * @param <T>
     * @return
     */
    private static <T> byte[] toByteArray(T body) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(body);
            oos.flush();
            return baos.toByteArray();
        } catch (Exception e) {

        }
        return new byte[0];
    }
}
