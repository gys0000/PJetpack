package com.gystry.designmodes.singleton_pattern;

/**
 * 描述：饿汉式 静态常量
 * 优点：写法简单，在类装载的时候完成了实例化，为什么叫饿汉式，就是比较急切的创建了实例。避免了线程同步问题
 * 缺点：在类装载的时候就完成实例化，没有达到lazy loading的效果，如果从始至终从未使用过这个实例，则会造成内存浪费
 */
public class HungrySingleton {
    private HungrySingleton() {

    }

    private static final HungrySingleton hungrySingleton = new HungrySingleton();

    public static HungrySingleton getInstance() {
        return hungrySingleton;
    }
}
