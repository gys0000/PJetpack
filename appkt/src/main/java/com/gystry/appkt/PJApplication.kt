package com.gystry.appkt

import android.app.Application
import com.gystry.libnetworkkt.ApiService.init

/**
 * @author gystry
 * 创建日期：2021/4/7 16
 * 邮箱：gystry@163.com
 * 描述：
 */
class PJApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        //外网路径http://123.56.232.18:8080/serverdemo
        init("http://123.56.232.18:8080/serverdemo", null)
    }
}