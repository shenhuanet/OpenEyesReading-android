package com.shenhua.openeyesreading.presenter;

import com.shenhua.openeyesreading.bean.NewsData;
import com.shenhua.openeyesreading.core.BasePresenterImpl;
import com.shenhua.openeyesreading.core.DataLoadType;
import com.shenhua.openeyesreading.model.NewsModel;
import com.shenhua.openeyesreading.model.NewsModelImpl;
import com.shenhua.openeyesreading.view.NewsView;

import java.util.List;

/**
 * 新闻列表代理接口实现
 * Created by shenhua on 8/25/2016.
 */
public class INewsListPresenterImpl extends BasePresenterImpl<NewsView, List<NewsData>> implements INewsListPresenter {

    private NewsModel<List<NewsData>> model;
    private String type;
    private String id;
    private int page;

    private boolean mIsRefresh = true;
    private boolean mHasInit;

    public INewsListPresenterImpl(NewsView mView, String id, String type) {
        super(mView);
        model = new NewsModelImpl();
        mSubscription = model.requestNewsList(this, type, id, page);
        this.id = id;
        this.type = type;
    }

    @Override
    public void OnPreRequest() {
        super.OnPreRequest();
        if (!mHasInit) mView.showProgress();
    }

    @Override
    public void OnRequestError(String errorInfo) {
        super.OnRequestError(errorInfo);
        mView.updateNewsList(null, mIsRefresh ? DataLoadType.TYPE_REFRESH_FAIL : DataLoadType.TYPE_LOAD_MORE_FAIL);
    }

    @Override
    public void onRequestSuccess(List<NewsData> data) {
        mHasInit = true;
        if (data != null) page += 20;
        mView.updateNewsList(data, mIsRefresh ? DataLoadType.TYPE_REFRESH_SUCCESS : DataLoadType.TYPE_LOAD_MORE_SUCCESS);
    }

    @Override
    public void refreshDatas() {
        page = 0;
        mIsRefresh = true;
        mSubscription = model.requestNewsList(this, type, id, page);
    }

    @Override
    public void loadMoreDatas() {
        mIsRefresh = false;
        mSubscription = model.requestNewsList(this, type, id, page);
    }
}
