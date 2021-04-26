package com.gystry.designmodes.singleton_pattern;

/**
 * 描述：懒汉式 线程不安全
 * 优点：懒加载，只有使用的时候才会加载
 * 缺点：只能单线程使用，多线程情况下，一个线程进入了if (lazySingleton==null) 判断语句块，
 * 还未来得及往下执行，另一个线程也通过了这个判断语句，这是便会产生多个实例
 * 所以在多线程环境下不可以使用这个方法
 */
public class LazySingleton {

    private LazySingleton(){

    }

    private static LazySingleton lazySingleton;

    public static LazySingleton getInstance(){
        if (lazySingleton==null) {
            lazySingleton=new LazySingleton();
        }
        return lazySingleton;
    }
}
