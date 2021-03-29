package com.gystry.appkt.ui.publish

import android.app.Activity
import android.os.Bundle
import com.gystry.appkt.R
import com.gystry.libnavannotation.ActivityDestination

/**
 * @author gystry
 * 创建日期：2021/3/29 10
 * 邮箱：gystry@163.com
 * 描述：
 */
@ActivityDestination(pageUrl = "main/tabs/publish",asStarter = false)
class PublishActivity:Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_dashboard)
    }
}