package com.shenhua.comlib.annotation;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.StringRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Activity、Fragment初始化的用到的注解
 * Created by Shenhua on 8/21/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ActivityFragmentInject {

    @LayoutRes int contentViewId() default -1;// 根布局

    @IdRes int toolbarId() default -1;// toolbar Id

    boolean toolbarHomeAsUp() default false;// 是否可返回

    @StringRes int toolbarTitle() default -1;// toolbar 标题

    boolean hasOptionsMenu() default false;// 是否允许溢出菜单（仅Fragment中使用）

    @MenuRes int menuId() default -1;// 菜单Id
}
