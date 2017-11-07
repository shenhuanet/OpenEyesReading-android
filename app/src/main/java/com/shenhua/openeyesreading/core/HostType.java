package com.shenhua.openeyesreading.core;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 请求数据的host类型
 * Created by shenhua on 8/23/2016.
 */
public class HostType {

    public static final int TYPE_COUNT = 3;
    @HostTypeChecker
    public static final int NEWS = 1;
    @HostTypeChecker
    public static final int SINA_PHOTOS = 2;

    @IntDef({NEWS, SINA_PHOTOS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HostTypeChecker {

    }
}
