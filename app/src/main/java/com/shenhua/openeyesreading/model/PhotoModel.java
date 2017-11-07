package com.shenhua.openeyesreading.model;

import com.shenhua.comlib.callback.OnHttpRequestCallback;

import rx.Subscription;

/**
 * 照片列表model层接口
 * Created by shenhua on 8/22/2016.
 */
public interface PhotoModel<T> {

    Subscription requestPhotoList(OnHttpRequestCallback<T> callback, String id, int startPage);
}
