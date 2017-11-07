package com.shenhua.openeyesreading.bean;

import java.io.Serializable;

/**
 * 爱历史老照片实体类
 * Created by shenhua on 8/11/2016.
 */
public class IHistoryOldPhoto implements Serializable {
    private static final long serialVersionUID = -1475087482489597023L;
    private String title;
    private String href;
    private String imgHref;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getImgHref() {
        return imgHref;
    }

    public void setImgHref(String imgHref) {
        this.imgHref = imgHref;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
