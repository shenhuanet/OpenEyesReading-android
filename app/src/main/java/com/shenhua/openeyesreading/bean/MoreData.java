package com.shenhua.openeyesreading.bean;

import java.io.Serializable;

/**
 * 更多实体类
 * Created by shenhua on 8/12/2016.
 */
public class MoreData implements Serializable {
    private static final long serialVersionUID = -4580798786253149697L;

    private String title;
    private String img;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
