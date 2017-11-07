package com.shenhua.openeyesreading.bean;

import java.io.Serializable;

/**
 * 必应图片数据
 * Created by shenhua on 2016/5/9.
 */
public class BingImagesData implements Serializable {

    private static final long serialVersionUID = 8844106242267403853L;

    private String imgUrl;//图片地址
    private String imgTitle;//图片标题
    private String imgSubTitle;//图片副标题
    private String imgDate;//图片日期
    private String imgLink;//图片链接

    public String getImgSubTitle() {
        return imgSubTitle;
    }

    public void setImgSubTitle(String imgSubTitle) {
        this.imgSubTitle = imgSubTitle;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgTitle() {
        return imgTitle;
    }

    public void setImgTitle(String imgTitle) {
        this.imgTitle = imgTitle;
    }

    public String getImgDate() {
        return imgDate;
    }

    public void setImgDate(String imgDate) {
        this.imgDate = imgDate;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }
}
