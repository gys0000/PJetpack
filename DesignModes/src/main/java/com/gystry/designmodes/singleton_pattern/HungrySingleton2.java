package com.gystry.designmodes.singleton_pattern;

/**
 * @author gystry
 * 创建日期：2021/4/25 17
 * 邮箱：gystry@163.com
 * 描述：饿汉式 静态代码块
 * 优点：写法简单，在类装载的时候完成了实例化，为什么叫饿汉式，就是比较急切的创建了实例。避免了线程同步问题
 * 缺点：在类装载的时候就完成实例化，没有达到lazy loading的效果，如果从始至终从未使用过这个实例，则会造成内存浪费
 */
public class HungrySingleton2 {
    private static HungrySingleton2 hungrySingleton2;

    static {
        hungrySingleton2 = new HungrySingleton2();
    }

    public static HungrySingleton2 getHungrySingleton2() {
        return hungrySingleton2;
    }

    private HungrySingleton2() {

    }
}
