package com.shenhua.openeyesreading.model;

import android.util.Log;

import com.shenhua.comlib.callback.OnHttpRequestCallback;
import com.shenhua.openeyesreading.bean.NewsData;
import com.shenhua.openeyesreading.core.HostType;
import com.shenhua.openeyesreading.core.RetrofitManager;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * 新闻列表model实现者
 * Created by shenhua on 8/25/2016.
 */
public class NewsModelImpl implements NewsModel<List<NewsData>> {

    @Override
    public Subscription requestNewsList(final OnHttpRequestCallback<List<NewsData>> callback, String type, final String id, int startPage) {
        return RetrofitManager.getInstance(HostType.NEWS).getNewsListObservable(type, id, startPage)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        callback.OnPreRequest();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("NewsModelImpl", throwable.getLocalizedMessage());
                    }
                }).flatMap(new Func1<Map<String, List<NewsData>>, Observable<NewsData>>() {
                    @Override
                    public Observable<NewsData> call(Map<String, List<NewsData>> map) {
                        return Observable.from(map.get(id));
                    }
                }).toSortedList(new Func2<NewsData, NewsData, Integer>() {
                    @Override
                    public Integer call(NewsData newsData, NewsData newsData2) {
                        return newsData2.ptime.compareTo(newsData.ptime);
                    }
                }).subscribe(new Subscriber<List<NewsData>>() {
                    @Override
                    public void onCompleted() {
                        callback.onPostRequest();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.OnRequestError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<NewsData> datas) {
                        callback.onRequestSuccess(datas);
                    }
                });
    }
}
