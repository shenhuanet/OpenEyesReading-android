package com.shenhua.openeyesreading.presenter;

import com.shenhua.openeyesreading.bean.SinaPhotoDetail;
import com.shenhua.openeyesreading.core.BasePresenterImpl;
import com.shenhua.openeyesreading.model.PhotoDetailModel;
import com.shenhua.openeyesreading.model.PhotoDetailModelImpl;
import com.shenhua.openeyesreading.view.IPhotoDetailView;

/**
 * 新浪图片详情代理实现
 * Created by shenhua on 8/25/2016.
 */
public class IPhotoDetailPresenterImpl extends BasePresenterImpl<IPhotoDetailView, SinaPhotoDetail> {

    private PhotoDetailModel<SinaPhotoDetail> model;

    public IPhotoDetailPresenterImpl(IPhotoDetailView mView, String id, SinaPhotoDetail data) {
        super(mView);
        model = new PhotoDetailModelImpl();
        if (data != null) mView.initViewPager(data);
        else mSubscription = model.requestPhotoDetail(this, id);
    }

    @Override
    public void onRequestSuccess(SinaPhotoDetail data) {
        super.onRequestSuccess(data);
        mView.initViewPager(data);
    }
}
