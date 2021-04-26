package com.gystry.designmodes.singleton_pattern;

/**
 * 描述：懒汉式 线程安全
 * 优点：懒加载，只有使用的时候才会加载，获取单例方法加了同步锁，保证线程安全
 * 缺点：效率太低了，每个线程都想获取类的实例的 时候，执行getInstance的时候都要进行同步
 */
public class LazySingleton2 {
    private LazySingleton2(){
    }
    private static LazySingleton2 lazySingleton2;

    public static synchronized LazySingleton2 getInstance(){
        if (lazySingleton2==null) {
            lazySingleton2=new LazySingleton2();
        }
        return lazySingleton2;
    }

}
