package com.gystry.designmodes.singleton_pattern;

/**
 * 描述：枚举
 * 优点：不仅能避免多线程同步问题，而且还能防止反序列化重新创建新的对象。
 * 缺点：JDK 1.5之后才能使用。
 */
public enum EnumSingleton {
    ENUM_SINGLETON;
}
