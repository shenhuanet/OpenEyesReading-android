package com.shenhua.openeyesreading.presenter;

import com.shenhua.openeyesreading.core.BasePresenter;

/**
 * 图片代理接口
 * Created by shenhua on 8/23/2016.
 */
public interface IPhotoListPresenter extends BasePresenter {

    void refreshDatas();

    void loadMoreDatas();
}
