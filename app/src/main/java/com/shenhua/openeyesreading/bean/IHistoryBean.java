package com.shenhua.openeyesreading.bean;

import java.io.Serializable;

/**
 * 爱历史实体
 * Created by shenhua on 8/11/2016.
 */
public class IHistoryBean implements Serializable {
    private static final long serialVersionUID = 2587699410578392349L;
    private String docString;

    public String getDocString() {
        return docString;
    }

    public void setDocString(String docString) {
        this.docString = docString;
    }
}
