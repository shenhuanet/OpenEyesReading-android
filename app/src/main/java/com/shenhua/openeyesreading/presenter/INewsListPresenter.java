package com.shenhua.openeyesreading.presenter;

import com.shenhua.openeyesreading.core.BasePresenter;

/**
 * 网易新闻列表代理接口
 * Created by shenhua on 8/25/2016.
 */
public interface INewsListPresenter extends BasePresenter {

    void refreshDatas();

    void loadMoreDatas();
}
