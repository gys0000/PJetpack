package com.gystry.libnetworkkt

/**
 * @author gystry
 * 创建日期：2021/3/18 11
 * 邮箱：gystry@163.com
 * 描述：
 */
data class ApiResponse<T>(var success: Boolean=false,
                          var status: Int=0,
                          var message: String="",
                          var body: T? =null)