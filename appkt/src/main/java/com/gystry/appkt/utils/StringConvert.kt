package com.gystry.appkt.utils

/**
 * @author gystry
 * 创建日期：2021/3/30 14
 * 邮箱：gystry@163.com
 * 描述：
 */
fun convertFeedUgc(count:Int):String{
    if(count<10000){
        return count.toString()
    }
    return "${count/10000}万"
}

fun convertTagFeedList(num:Int):String{
    return if(num<10000){
        "${num}人观看"
    }else{
        "${num/10000}万人观看"
    }
}