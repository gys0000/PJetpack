package com.gystry.designmodes.singleton_pattern;

/**
 * 描述：双重检查DCL
 * 优点：线程安全，延迟加载，效率较高
 * 缺点：JDK1.5之前不可用，由于volatile关键字会屏蔽Java虚拟机所做的一些代码优化，可能会导致系统运行效率较低
 * 而JDK1.5以及之后的版本修复了这个问题
 */
public class DCLSingleton {
    private DCLSingleton(){
    }
    private static volatile DCLSingleton dclSingleton;

    public static DCLSingleton getInstance(){
        if (dclSingleton ==null) {
            synchronized (DCLSingleton.class){
                if (dclSingleton ==null) {
                    dclSingleton =new DCLSingleton();
                }
            }

        }
        return dclSingleton;
    }

}
