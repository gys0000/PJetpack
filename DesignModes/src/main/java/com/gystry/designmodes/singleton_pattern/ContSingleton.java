package com.gystry.designmodes.singleton_pattern;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：容器类管理
 * 优点：在程序的初始，将多种单例类型注入到一个统一的管理类中，在使用时根据key获取对象对应类型的对象。
 * 这种方式使得我们可以管理多种类型的单例，并且在使用时可以通过统一的接口进行获取操作，
 * 降低了用户的使用成本，也对用户隐藏了具体实现，降低了耦合度。
 * 缺点：不常用，有些麻烦
 */
public class ContSingleton {
    private static Map<String,Object> objectMap=new HashMap<>();
    private ContSingleton(){}

    public static void put(String key,Object instance){
        if (!objectMap.containsKey(key)) {
            objectMap.put(key,instance);
        }
    }

    public static Object get(String key){
        return objectMap.get(key);
    }
}
