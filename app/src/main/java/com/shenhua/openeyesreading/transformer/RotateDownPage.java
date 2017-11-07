package com.shenhua.openeyesreading.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * 扇形旋转的，类似于酷狗翻转的view
 * Created by shenhua on 2016/5/7.
 */
public class RotateDownPage implements ViewPager.PageTransformer {

    private static final float ROT_MAX = 20.0f;
    private float mRot;

    @Override
    public void transformPage(View page, float position) {
        if (position < -1) {
            ViewHelper.setRotation(page, 0);
        } else if (position <= 1) {
            if (position < 0) {
                mRot = (ROT_MAX * position);
                ViewHelper.setPivotX(page, page.getMeasuredWidth() * 0.5f);
                ViewHelper.setPivotY(page, page.getMeasuredHeight());
                ViewHelper.setRotation(page, mRot);
            } else {
                mRot = (ROT_MAX * position);
                ViewHelper.setPivotX(page, page.getMeasuredWidth() * 0.5f);
                ViewHelper.setPivotY(page, page.getMeasuredHeight());
                ViewHelper.setRotation(page, mRot);
            }
        } else {
            ViewHelper.setRotation(page, 0);
        }
    }
}
