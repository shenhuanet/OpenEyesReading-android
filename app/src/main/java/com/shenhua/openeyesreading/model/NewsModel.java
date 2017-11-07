package com.shenhua.openeyesreading.model;

import com.shenhua.comlib.callback.OnHttpRequestCallback;

import rx.Subscription;

/**
 * 新闻列表model层接口
 * Created by shenhua on 8/25/2016.
 */
public interface NewsModel<T> {

    Subscription requestNewsList(OnHttpRequestCallback<T> callback, String type, String id, int startPage);

}
