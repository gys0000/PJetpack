package com.gystry.libnetworkkt

import android.util.Log
import com.alibaba.fastjson.JSON
import com.google.gson.Gson
import org.json.JSONObject
import java.lang.reflect.Type

/**
 * @author gystry
 * 创建日期：2021/3/18 15
 * 邮箱：gystry@163.com
 * 描述：
 */
class JsonConvert<T> :Convert<T>{
    override fun convert(content: String?, type: Type): T? {

        val jsonObject = JSONObject(content)
        val objectData = jsonObject.getJSONObject("data")
        if (objectData!=null) {
            val objectData2 = objectData.getJSONArray("data")
            if (objectData2!=null) {
                return Gson().fromJson(objectData2.toString(), type)
            }
            return null
        }

//        val parseObject = JSON.parseObject(content)
//        val data = parseObject.getJSONObject("data")
//        if (data!=null) {
//            val data1 = data["data"]
//            return JSON.parseObject(data1.toString(),type)
//        }
        return null
    }

    override fun convert(content: String, claz: Class<*>): T? {
        val parseObject = JSON.parseObject(content)
        val data = parseObject.getJSONObject("data")
        if (data!=null) {
            val data1 = data["data"]
            return JSON.parseObject(data1.toString(),claz) as T?
        }
        return null
    }
}