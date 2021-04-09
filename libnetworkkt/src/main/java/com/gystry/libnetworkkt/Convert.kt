package com.gystry.libnetworkkt

import java.lang.reflect.Type

/**
 * @author gystry
 * 创建日期：2021/3/18 11
 * 邮箱：gystry@163.com
 * 描述：
 */
interface Convert<T> {
    fun convert(content: String?, type: Type): T?
    fun convert(content: String, claz: Class<*>): T?
}
