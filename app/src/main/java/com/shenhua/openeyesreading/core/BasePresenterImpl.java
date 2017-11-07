package com.shenhua.openeyesreading.core;

import com.shenhua.comlib.callback.OnHttpRequestCallback;
import com.shenhua.openeyesreading.view.BaseView;

import rx.Subscription;

/**
 * 代理的基类实现
 * Created by shenhua on 8/23/2016.
 */
public class BasePresenterImpl<T extends BaseView, V> implements BasePresenter, OnHttpRequestCallback<V> {

    protected Subscription mSubscription;
    protected T mView;

    public BasePresenterImpl(T mView) {
        this.mView = mView;
    }

    @Override
    public void OnPreRequest() {
        mView.showProgress();
    }

    @Override
    public void onPostRequest() {
        mView.hideProgress();
    }

    @Override
    public void OnRequestError(String errorInfo) {
        mView.toast(errorInfo);
        mView.hideProgress();
    }

    @Override
    public void onRequestSuccess(V data) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
        mView = null;
    }
}
