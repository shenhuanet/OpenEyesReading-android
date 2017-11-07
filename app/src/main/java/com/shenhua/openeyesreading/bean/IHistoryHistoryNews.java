package com.shenhua.openeyesreading.bean;

import java.io.Serializable;

/**
 * 爱历史历史新闻实体类
 * Created by shenhua on 8/11/2016.
 */
public class IHistoryHistoryNews implements Serializable {
    private static final long serialVersionUID = 2002191844002057798L;
    private String title;
    private String href;
    private String time;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
