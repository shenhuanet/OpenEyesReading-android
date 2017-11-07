package com.shenhua.openeyesreading.bean;

import java.io.Serializable;

/**
 * 爱历史点击排行实体类
 * Created by shenhua on 8/11/2016.
 */
public class IHistoryClickRank implements Serializable {

    private static final long serialVersionUID = 8400888287381894174L;
    private String title;
    private String href;

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
}
