package com.shenhua.openeyesreading.bean;

import java.io.Serializable;

/**
 * 文章数据
 * Created by Shenhua on 7/31/2016.
 */
public class ArticleData implements Serializable {

    private static final long serialVersionUID = 2795530302451499783L;
    private String title;
    private String auth;
    private String content;
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
