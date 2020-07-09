package com.gystry.pjetpack;

import android.app.Application;

import com.gystry.libnetwork.ApiService;

/**
 * @author gystry
 * 创建日期：2020/7/9 16
 * 邮箱：gystry@163.com
 * 描述：
 */
public class PJApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApiService.init("http://ip地址:8080/serverdemo", null);
    }
}
