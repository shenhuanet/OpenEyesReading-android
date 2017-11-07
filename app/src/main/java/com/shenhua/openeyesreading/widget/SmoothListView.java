package com.shenhua.openeyesreading.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.shenhua.openeyesreading.R;
import com.shenhua.openeyesreading.widget.ListViewHeader;

/**
 * Created by Shenhua on 5/13/2016.
 */
public class SmoothListView extends ListView implements AbsListView.OnScrollListener {

    private float mLastY = -1; // 保存Y坐标值
    private Scroller mScroller; // 用于scroll back
    private OnScrollListener mScrollListener; // 滚动监听
    private ISmoothListViewListener mListViewListener;//listview监听器
    private ListViewHeader mHeaderView;// 下拉刷新头
    private RelativeLayout mHeaderViewContent;// 下拉刷新头的布局
    private TextView mHeaderTimeView;// 用于显示上次更新时间的tv
    private int mHeaderViewHeight; // 下拉刷新头的高度
    private boolean mEnablePullRefresh = true;// 是否允许下拉刷新
    private boolean mPullRefreshing = false; // 是否正在刷新
    private int mScrollBack;
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLL_DURATION = 400; // 显示时间
    private final static float OFFSET_RADIO = 1.8f;

    public SmoothListView(Context context) {
        super(context);
        initWithContext(context);
    }

    public SmoothListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public SmoothListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    private void initWithContext(Context context) {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        super.setOnScrollListener(this);
        // 初始化下拉刷新的view
        mHeaderView = new ListViewHeader(context);
        mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.smoothlistview_header_content);
        mHeaderTimeView = (TextView) mHeaderView.findViewById(R.id.smoothlistview_header_time);
        addHeaderView(mHeaderView);
        // 得到下拉刷新头的高度，加入观察者
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mHeaderViewHeight = mHeaderViewContent.getHeight();
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    /**
     * 停止刷新，重置下拉刷新高度
     */
    public void stopRefresh() {
        if (mPullRefreshing) {
            mPullRefreshing = false;
            resetHeaderHeight();
        }
    }

    /**
     * 设置最后一次下拉刷新时间
     *
     * @param time
     */
    public void setRefreshTime(String time) {
        mHeaderTimeView.setText(time);
    }

    /**
     * 调用OnScrolling
     */
    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnSmoothScrollListener) {
            OnSmoothScrollListener l = (OnSmoothScrollListener) mScrollListener;
            l.onSmoothScrolling(this);
        }
    }

    /***
     * 更新下拉刷新头的高度
     *
     * @param delta
     */
    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisibleHeight((int) delta + mHeaderView.getVisibleHeight());
        if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
            if (mHeaderView.getVisibleHeight() > mHeaderViewHeight)
                mHeaderView.setState(ListViewHeader.STATE_READY);
            else
                mHeaderView.setState(ListViewHeader.STATE_NORMAL);
        }
        setSelection(0); // 回到顶部
    }

    /**
     * 重置下拉刷新头部的高度
     */
    private void resetHeaderHeight() {
        int height = mHeaderView.getVisibleHeight();
        if (height == 0) return;
        if (mPullRefreshing && height <= mHeaderViewHeight) return;// 小于下拉刷新高度，返回。
        int finalHeight = 0;
        if (mPullRefreshing && height > mHeaderViewHeight)
            finalHeight = mHeaderViewHeight;
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        invalidate();
    }

    public void setSmoothListViewListener(ISmoothListViewListener l) {
        mListViewListener = l;
    }

    public interface OnSmoothScrollListener extends OnScrollListener {
        void onSmoothScrolling(View view);
    }

    public interface ISmoothListViewListener {
        void onRefresh();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) mLastY = ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisibleHeight() > 0 || deltaY > 0)) {
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                    invokeOnScrolling();
                }
                break;
            default:
                mLastY = -1; // 重置
                if (getFirstVisiblePosition() == 0) {
                    // 调用刷新
                    if (mEnablePullRefresh && mHeaderView.getVisibleHeight() > mHeaderViewHeight) {
                        mPullRefreshing = true;
                        mHeaderView.setState(ListViewHeader.STATE_REFRESHING);
                        if (mListViewListener != null)
                            mListViewListener.onRefresh();
                    }
                    resetHeaderHeight();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER)
                mHeaderView.setVisibleHeight(mScroller.getCurrY());
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mScrollListener != null)
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
    }

}
