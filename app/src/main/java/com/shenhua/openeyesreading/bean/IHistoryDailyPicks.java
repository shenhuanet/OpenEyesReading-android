package com.shenhua.openeyesreading.bean;

import java.io.Serializable;

/**
 * 爱历史每日推荐实体类
 * Created by shenhua on 8/11/2016.
 */
public class IHistoryDailyPicks implements Serializable {

    private static final long serialVersionUID = 1762717001379263893L;
    private String title;
    private String time;
    private String discuss;
    private String describe;
    private String href;
    private String imgHref;
    private String pageList;

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDiscuss() {
        return discuss;
    }

    public void setDiscuss(String discuss) {
        this.discuss = discuss;
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
}
