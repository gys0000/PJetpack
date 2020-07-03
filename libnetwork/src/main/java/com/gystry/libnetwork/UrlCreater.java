package com.gystry.libnetwork;

import java.util.Map;

/**
 * @author gystry
 * 创建日期：2020/7/3 18
 * 邮箱：gystry@163.com
 * 描述：
 */
public class UrlCreater {
    public static String createUrlFromParams(String url, Map<String, Object> params) {
        StringBuilder builder = new StringBuilder();
        builder.append(url);

        return builder.toString();
    }
}
