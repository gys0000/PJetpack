package com.gystry.appkt.ui.login

/**
 * @author gystry
 * 创建日期：2021/4/6 14
 * 邮箱：gystry@163.com
 * 描述：
 */
class UserManager {
    companion object {
        val instance: UserManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            UserManager()
        }
    }

    public fun getUserId(): Long = 0L
}