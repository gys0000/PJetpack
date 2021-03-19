package com.gystry.libnetworkkt

import android.util.JsonReader

/**
 * @author gystry
 * 创建日期：2021/3/19 15
 * 邮箱：gystry@163.com
 * 描述：
 */
class NetTest {
    fun testNet(){
        ApiService.get<JsonReader>("/ugc/toggleFeedLike")
                .addParams("userId", "UserManager.getInstance().getUserId()")
                .addParams("itemId", "feed.itemId")
                .execute(object : JsonCallback<JsonReader>() {
                    override fun onError(response: ApiResponse<JsonReader>?) {
                        super.onError(response)
                    }
                });

    }
}