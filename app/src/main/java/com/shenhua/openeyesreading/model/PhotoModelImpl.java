package com.shenhua.openeyesreading.model;

import android.util.Log;

import com.shenhua.comlib.callback.OnHttpRequestCallback;
import com.shenhua.openeyesreading.bean.SinaPhotosList;
import com.shenhua.openeyesreading.core.HostType;
import com.shenhua.openeyesreading.core.RetrofitManager;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * 照片列表model层接口实现
 * Created by shenhua on 8/23/2016.
 */
public class PhotoModelImpl implements PhotoModel<List<SinaPhotosList.DataEntity.PhotoListEntity>> {

    @Override
    public Subscription requestPhotoList(final OnHttpRequestCallback<List<SinaPhotosList.DataEntity.PhotoListEntity>> callback, String id, int startPage) {
        return RetrofitManager.getInstance(HostType.SINA_PHOTOS).getSinaPhotoListObservable(id, startPage).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                callback.OnPreRequest();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e("PhotoModelImpl", throwable.getLocalizedMessage());
            }
        }).flatMap(new Func1<SinaPhotosList, Observable<SinaPhotosList.DataEntity.PhotoListEntity>>() {
            @Override
            public Observable<SinaPhotosList.DataEntity.PhotoListEntity> call(SinaPhotosList sinaPhotosList) {
                return Observable.from(sinaPhotosList.data.list);
            }
        }).toSortedList(new Func2<SinaPhotosList.DataEntity.PhotoListEntity, SinaPhotosList.DataEntity.PhotoListEntity, Integer>() {
            @Override
            public Integer call(SinaPhotosList.DataEntity.PhotoListEntity photoListEntity, SinaPhotosList.DataEntity.PhotoListEntity photoListEntity2) {
                return photoListEntity2.pubDate > photoListEntity.pubDate ? 1 : photoListEntity.pubDate == photoListEntity2.pubDate ? 0 : -1;
            }
        }).subscribe(new Subscriber<List<SinaPhotosList.DataEntity.PhotoListEntity>>() {
            @Override
            public void onCompleted() {
                callback.onPostRequest();
            }

            @Override
            public void onError(Throwable e) {
                callback.OnRequestError(e + "\n" + e.getLocalizedMessage());
            }

            @Override
            public void onNext(List<SinaPhotosList.DataEntity.PhotoListEntity> photoListEntities) {
                callback.onRequestSuccess(photoListEntities);
            }

        });
    }
}
