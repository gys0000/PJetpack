package com.gystry.libnetwork;

/**
 * @author gystry
 * 创建日期：2020/7/3 17
 * 邮箱：gystry@163.com
 * 描述：
 */
public class ApiResponse<T> {
    public boolean success;
    public int status;
    public String message;
    public T body;
}
