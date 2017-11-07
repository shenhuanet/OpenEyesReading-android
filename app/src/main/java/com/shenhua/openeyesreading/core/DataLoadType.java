package com.shenhua.openeyesreading.core;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 数据加载结果的类型
 * Created by shenhua on 8/22/2016.
 */
public class DataLoadType {

    @DataLoadTypeChecker
    public static final int TYPE_REFRESH_SUCCESS = 1;
    @DataLoadTypeChecker
    public static final int TYPE_REFRESH_FAIL = 2;
    @DataLoadTypeChecker
    public static final int TYPE_LOAD_MORE_SUCCESS = 3;
    @DataLoadTypeChecker
    public static final int TYPE_LOAD_MORE_FAIL = 4;

    //替代枚举的方案，使用IntDef保证类型安全
    @IntDef({TYPE_REFRESH_SUCCESS, TYPE_REFRESH_FAIL, TYPE_REFRESH_SUCCESS, TYPE_REFRESH_FAIL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DataLoadTypeChecker {
    }
}
