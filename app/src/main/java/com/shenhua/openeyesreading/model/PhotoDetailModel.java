package com.shenhua.openeyesreading.model;

import com.shenhua.comlib.callback.OnHttpRequestCallback;

import rx.Subscription;

/**
 * 新浪图片详情model层接口
 * Created by shenhua on 8/25/2016.
 */
public interface PhotoDetailModel<T> {

    Subscription requestPhotoDetail(OnHttpRequestCallback<T> callback, String id);
}
