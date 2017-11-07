package com.shenhua.comlib.callback;

/**
 * Http请求事件回调
 * Created by Shenhua on 8/21/2016.
 */
public interface OnHttpRequestCallback<T> {

    void OnPreRequest();//在请求之前调用

    void onPostRequest();//在请求之后调用

    void OnRequestError(String errorInfo);//在请求错误时调用

    void onRequestSuccess(T data);//在请求成功时调用

}
