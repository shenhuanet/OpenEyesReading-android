package com.shenhua.openeyesreading.presenter;

import com.shenhua.openeyesreading.core.BasePresenterImpl;
import com.shenhua.openeyesreading.view.IPhotoView;

/**
 * 图片代理实现
 * Created by shenhua on 8/24/2016.
 */
public class IPhotoPresenterImpl extends BasePresenterImpl<IPhotoView, Void> {

    public IPhotoPresenterImpl(IPhotoView mView) {
        super(mView);
        mView.initViewPager();
    }
}
