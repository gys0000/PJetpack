package com.gystry.libnetwork;

/**
 * @author gystry
 * 创建日期：2020/7/3 17
 * 邮箱：gystry@163.com
 * 描述：
 */
public class GetRequest<T> extends Request<T, GetRequest> {
    public GetRequest(String url) {
        super(url);
    }

    @Override
    protected okhttp3.Request generateRequest(okhttp3.Request.Builder builder) {
        //将params拼接到url中
        String urlFromParams = UrlCreater.createUrlFromParams(mUrl, params);
        return null;
    }
}
