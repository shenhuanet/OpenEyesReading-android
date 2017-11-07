package com.shenhua.openeyesreading.model;

import com.shenhua.comlib.callback.OnHttpRequestCallback;
import com.shenhua.openeyesreading.bean.SinaPhotoDetail;
import com.shenhua.openeyesreading.core.HostType;
import com.shenhua.openeyesreading.core.RetrofitManager;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * 图片详情的Model层接口实现
 * Created by shenhua on 8/25/2016.
 */
public class PhotoDetailModelImpl implements PhotoDetailModel<SinaPhotoDetail> {
    @Override
    public Subscription requestPhotoDetail(final OnHttpRequestCallback<SinaPhotoDetail> callback, String id) {
        return RetrofitManager.getInstance(HostType.SINA_PHOTOS).getSinaPhotoDetailObservable(id)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        callback.OnPreRequest();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SinaPhotoDetail>() {
                    @Override
                    public void onCompleted() {
                        callback.onPostRequest();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.OnRequestError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(SinaPhotoDetail sinaPhotoDetail) {
                        callback.onRequestSuccess(sinaPhotoDetail);
                    }
                });
    }
}
