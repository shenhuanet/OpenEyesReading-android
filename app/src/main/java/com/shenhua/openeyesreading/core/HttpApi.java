package com.shenhua.openeyesreading.core;

/**
 * app 业务请求核心
 * Created by Shenhua on 7/29/2016.
 */
public interface HttpApi {

    void toGetBingImgs(String format, String idx, String n, HttpApiCallback callback);

    void toGetArticle(String url, HttpApiCallback callback);

    void toGetIHistory(String url, HttpApiCallback callback);

}
