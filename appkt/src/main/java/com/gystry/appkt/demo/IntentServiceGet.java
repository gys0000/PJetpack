package com.gystry.appkt.demo;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

/**
 * @author gystry
 * 创建日期：2021/4/28 17
 * 邮箱：gystry@163.com
 * 描述：
 */
public class IntentServiceGet extends IntentService {
    /**
     * @param name
     * @deprecated
     */
    public IntentServiceGet(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
