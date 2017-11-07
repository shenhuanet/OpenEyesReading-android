package com.shenhua.openeyesreading.widget;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 此类用于修改ViewPager滑动速度
 */
public class FixedSpeedScroller extends Scroller {

    private int duration = 600;//默认划屏时间

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator,int duration) {
        super(context, interpolator);
        this.duration = duration;
    }

    /**
     * 重写此方法修改滑动速度
     * @param startX
     * @param startY
     * @param dx
     * @param dy
     * @param duration
     */
    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, this.duration);//注意“this.”
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, duration);
    }

}
