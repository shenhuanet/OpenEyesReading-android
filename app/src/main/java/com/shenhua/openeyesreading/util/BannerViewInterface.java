package com.shenhua.openeyesreading.util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.ListView;

import java.util.List;

/**
 * banner视图接口
 * Created by shenhua on 2016/5/17.
 */
public abstract class BannerViewInterface<T> {

    protected Activity mContext;
    protected LayoutInflater mInflate;
    protected T mEntity;//listview数据实体

    public BannerViewInterface(Activity context) {
        this.mContext = context;
        mInflate = LayoutInflater.from(context);
    }

    /**
     * 填充listview数据
     *
     * @param t
     * @param listView
     * @return
     */
    public boolean fillView(T t, ListView listView) {
        if (t == null) {
            return false;
        }
        if ((t instanceof List) && ((List) t).size() == 0) {
            return false;
        }
        this.mEntity = t;
        getView(t, listView);
        return true;
    }

    /**
     * 根据传来的泛型填充listview
     *
     * @param t
     * @param listView
     */
    protected abstract void getView(T t, ListView listView);
}
