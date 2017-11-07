package com.shenhua.openeyesreading.presenter;

import com.shenhua.openeyesreading.bean.SinaPhotosList;
import com.shenhua.openeyesreading.core.BasePresenterImpl;
import com.shenhua.openeyesreading.core.DataLoadType;
import com.shenhua.openeyesreading.model.PhotoModel;
import com.shenhua.openeyesreading.model.PhotoModelImpl;
import com.shenhua.openeyesreading.view.PhotoList;

import java.util.List;

/**
 * 图片代理接口实现
 * Created by shenhua on 8/23/2016.
 */
public class IPhotoListPresenterImpl extends BasePresenterImpl<PhotoList, List<SinaPhotosList.DataEntity.PhotoListEntity>> implements IPhotoListPresenter {

    private PhotoModel<List<SinaPhotosList.DataEntity.PhotoListEntity>> mPhotoModel;
    private String mPhotoId;
    private int mStartPage;
    private boolean mIsRefresh = true;
    private boolean mHasInit;

    public IPhotoListPresenterImpl(PhotoList mView, String photoId, int startPage) {
        super(mView);
        mPhotoId = photoId;
        mStartPage = startPage;
        mPhotoModel = new PhotoModelImpl();
        mSubscription = mPhotoModel.requestPhotoList(this, mPhotoId, mStartPage);
    }

    @Override
    public void refreshDatas() {
        mStartPage = 1;
        mIsRefresh = true;
        mSubscription = mPhotoModel.requestPhotoList(this, mPhotoId, mStartPage);
    }

    @Override
    public void loadMoreDatas() {
        mIsRefresh = false;
        mSubscription = mPhotoModel.requestPhotoList(this, mPhotoId, mStartPage);
    }

    @Override
    public void OnPreRequest() {
        if (!mHasInit) mView.showProgress();
    }

    @Override
    public void OnRequestError(String errorInfo) {
        super.OnRequestError(errorInfo);
        mView.updatePhotoList(null, mIsRefresh ? DataLoadType.TYPE_REFRESH_FAIL : DataLoadType.TYPE_LOAD_MORE_FAIL);
    }

    @Override
    public void onRequestSuccess(List<SinaPhotosList.DataEntity.PhotoListEntity> data) {
        mHasInit = true;
        if (data != null && data.size() > 0) mStartPage++;
        mView.updatePhotoList(data, mIsRefresh ? DataLoadType.TYPE_REFRESH_SUCCESS : DataLoadType.TYPE_LOAD_MORE_SUCCESS);
    }
}
