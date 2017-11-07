package com.shenhua.openeyesreading.view;

import com.shenhua.openeyesreading.bean.SinaPhotosList;
import com.shenhua.openeyesreading.core.DataLoadType;

import java.util.List;

/**
 * 图片列表接口
 * Created by shenhua on 8/22/2016.
 */
public interface PhotoList extends BaseView {

    void updatePhotoList(List<SinaPhotosList.DataEntity.PhotoListEntity> data, @DataLoadType.DataLoadTypeChecker int type);

}
