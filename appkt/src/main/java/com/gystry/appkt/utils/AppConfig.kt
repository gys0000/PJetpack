package com.gystry.appkt.utils

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gystry.appkt.model.BottomBar
import com.gystry.appkt.model.Destination
import com.gystry.libcommon.AppGlobal
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

/**
 * @author gystry
 * 创建日期：2021/3/29 15
 * 邮箱：gystry@163.com
 * 描述：
 */
var sDestConfig:HashMap<String, Destination>?=null
var bottemBar:BottomBar?=null

fun getDestConfig():HashMap<String, Destination>{
    if (sDestConfig==null) {
        val parseFile = parseFile("destnation.json")
       sDestConfig= Gson().fromJson(parseFile,object : TypeToken<HashMap<String?, Destination?>>()
       {}.type)
    }
    return sDestConfig!!
}

fun getBottomBar():BottomBar{
    if (bottemBar==null) {
        val parseFile = parseFile("main_tabs_config.json")
        Log.e("TAG", "getBottomBar: ${parseFile}")
       bottemBar= Gson().fromJson(parseFile, BottomBar::class.java)
    }
    return bottemBar!!
}


fun parseFile(fileName: String): String {
    val assets = AppGlobal.getApplication().resources.assets
    val buffer = StringBuffer()
    try {
        assets.open(fileName).use { ins ->
            InputStreamReader(ins).use { isr ->
                BufferedReader(isr).use { br ->
                    var line: String? = null
                    while (br.readLine().also {
                                line = it
                            } != null) {
                        buffer.append(line)
                    }
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return buffer.toString()
}

fun dp2px(size: Int): Int {
    val v: Float = AppGlobal.getApplication().resources.displayMetrics.density * size + 0.5f
    return v.toInt()
}

fun getPageId(pageUrl: String): Int {
    val destConfig: HashMap<String, Destination> = getDestConfig()
    val destination = destConfig[pageUrl] ?: return -1
    return destination.id
}