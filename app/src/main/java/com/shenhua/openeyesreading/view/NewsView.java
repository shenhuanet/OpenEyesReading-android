package com.shenhua.openeyesreading.view;

import com.shenhua.openeyesreading.bean.NewsData;
import com.shenhua.openeyesreading.core.DataLoadType;

import java.util.List;

/**
 * 网易新闻列表
 * Created by shenhua on 8/25/2016.
 */
public interface NewsView extends BaseView {

    void updateNewsList(List<NewsData> datas, @DataLoadType.DataLoadTypeChecker int type);
}
