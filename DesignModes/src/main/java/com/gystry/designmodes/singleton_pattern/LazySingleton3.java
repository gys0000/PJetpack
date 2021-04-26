package com.gystry.designmodes.singleton_pattern;

/**
 * 描述：懒汉式 线程安全 同步代码块
 * 优点：改进了第四种效率低的问题
 * 缺点：不能完全保证单例，假如一个线程进入了 if (lazySingleton2==null) 判断语句块，还未来得及往下执行，
 * 另一个线程也通过了这个判断语句，这是便会产生多个实例
 */
public class LazySingleton3 {
    private LazySingleton3(){
    }
    private static LazySingleton3 lazySingleton2;

    public static LazySingleton3 getInstance(){
        if (lazySingleton2==null) {
            synchronized (LazySingleton3.class){
//                if (lazySingleton2==null) {
//                    lazySingleton2=new LazySingleton3();
//                }
                lazySingleton2=new LazySingleton3();
            }

        }
        return lazySingleton2;
    }

}
