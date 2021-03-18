package com.gystry.libnetworkkt

import com.alibaba.fastjson.JSON
import java.lang.reflect.Type

/**
 * @author gystry
 * 创建日期：2021/3/18 15
 * 邮箱：gystry@163.com
 * 描述：
 */
class JsonConvert<Any> :Convert<Any>{
    override fun convert(content: String, type: Type): Any? {
        val parseObject = JSON.parseObject(content)
        val data = parseObject.getJSONObject("data")
        if (data!=null) {
            val data1 = data["data"]
            return JSON.parseObject(data1.toString(),type)
        }
        return null
    }

    override fun convert(content: String, claz: Class<*>): Any? {
        val parseObject = JSON.parseObject(content)
        val data = parseObject.getJSONObject("data")
        if (data!=null) {
            val data1 = data["data"]
            return JSON.parseObject(data1.toString(),claz) as Any?
        }
        return null
    }
}