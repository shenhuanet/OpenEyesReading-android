package com.shenhua.openeyesreading.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by shenhua on 2016/6/13.
 */
public class CustomLayout extends LinearLayout implements View.OnTouchListener {

    private LinearLayout top;
    private ScrollView sv;
    private boolean isfrist = true;
    private float y1, y2;
    private int hight = 60;
    private Scroller mScroller;

    protected void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(0, mScroller.getFinalY(), 0, dy);
        invalidate();
    }

    protected void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(0, dy);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && isfrist) {// 只需实例化一次
            sv = (ScrollView) getChildAt(0);// 该自定义布局写入xml文件时，其子布局的第一个必须是ScrollView时，这里才能getChildAt(0) 实例化ScrollView
            sv.setOverScrollMode(View.OVER_SCROLL_NEVER);// 去掉ScrollView 滑动到底部继续滑动时会出现杂色块
            sv.setOnTouchListener(this);
            isfrist = false;
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {// 判断mScroller滚动是否完成
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());// 这里调用View的scrollTo方法完成实际的滚动
            postInvalidate();
        }
        super.computeScroll();
    }

    public CustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        setClickable(true);
        setLongClickable(true);
        mScroller = new Scroller(context);
    }

    public CustomLayout(Context context) {
        super(context);
    }

    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                y2 = event.getY();
                int scrollY = v.getScrollY();
                int height = v.getHeight();
                int scrollViewMeasuredHeight = sv.getChildAt(0).getMeasuredHeight();
                if ((y2 - y1) > 0 && v.getScrollY() <= 0) {// 头部回弹效果
                    smoothScrollTo(0, -(int) ((y2 - y1) / 2));
                    System.out.println("topMargin = " + (int) ((y2 - y1 / 2)));
                    return false;
                }
                if ((y2 - y1) < 0 && (scrollY + height) == scrollViewMeasuredHeight) {// 底部回弹效果
                    smoothScrollTo(0, -(int) ((y2 - y1) / 2));
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                smoothScrollTo(0, 0);// 自动回滚
                break;
        }
        return false;
    }
}
