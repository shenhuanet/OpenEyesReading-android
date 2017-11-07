package com.shenhua.openeyesreading.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.shenhua.comlib.widget.CircleLoadingView;
import com.shenhua.openeyesreading.R;

/**
 * 下拉刷新的布局
 * Created by shenhua on 8/24/2016.
 */
public class PullToRefreshLayout extends RelativeLayout implements ViewTreeObserver.OnGlobalLayoutListener {

    private static final int HIDE = 0;// 隐藏的状态
    private static final int PULL_TO_REFRESH = 1;// 下拉刷新的状态
    private static final int RELEASE_TO_REFRESH = 2;// 松开刷新的状态
    private static final int REFRESHING = 3;// 正在刷新的状态

    private Context mContext;
    private int mTouchSlop;
    private CircleLoadingView mCircleLoadingView;
    private int mHeadHeight;
    private RecyclerView mRecyclerView;
    private int[] mVisiblePositions;
    private int mFirstPosition = -1;
    private int mState = HIDE;
    private float mRecordX;// 记录分发事件时的Y坐标，用于在执行刷新动画时往下滑到某一位置停留，等待刷新动画执行完毕自动隐藏时，更新基点
    private ValueAnimator mControlHeadAnimator;
    private float mRatio;
    private float mDownY;
    private float mMoveY;
    private boolean mIsPostDown;
    private boolean mIsDownAction;
    private boolean mMonitorClick;
    private long mOnTouchTime;
    private boolean mIsLoadingPullDown;
    private OnRefreshListener mListener;

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(ViewConfiguration.get(mContext));
        mCircleLoadingView = new CircleLoadingView(mContext);
        addView(mCircleLoadingView);
        mCircleLoadingView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        mHeadHeight = mCircleLoadingView.getMeasuredHeight();
        mCircleLoadingView.setPadding(0, -mHeadHeight, 0, 0);
        mCircleLoadingView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRecyclerView = (RecyclerView) getChildAt(1);
        RelativeLayout.LayoutParams params = (LayoutParams) mRecyclerView.getLayoutParams();
        params.addRule(PullToRefreshLayout.BELOW, R.id.id_refresh_head);
        if (!(mRecyclerView instanceof RecyclerView))
            throw new Error("PullToRefreshLayout childView at position 1 must be RecyclerView!");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        calculateFirstPos();
        mRecordX = ev.getY();
        if (mFirstPosition != 0) {
            mIsPostDown = false;
            // 控制头部动画在执行时，列表不能滚动
            if (mControlHeadAnimator == null || mControlHeadAnimator.isRunning()) {
                mDownY = ev.getY();
//                if (mCircleLoadingView.getVisibility() == VISIBLE)
//                头部在执行刷新的时候列表在往下滑，这时并没有在顶部显示的时候
//                mIsLoadingPullDown = false;
                mIsDownAction = false;
                return super.dispatchTouchEvent(ev);
            }
        }
        if (mMonitorClick) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) ev.setAction(MotionEvent.ACTION_UP);
            mMonitorClick = false;
            return super.dispatchTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mOnTouchTime = System.currentTimeMillis();
                mDownY = ev.getY();
                mMoveY = 0;
                mIsDownAction = true;
                if (mCircleLoadingView.getPaddingTop() > -mHeadHeight || (mControlHeadAnimator != null && mControlHeadAnimator.isRunning()))
                    mIsLoadingPullDown = true;
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveY = ev.getY();
                if (mControlHeadAnimator != null && mControlHeadAnimator.isRunning()) return true;
                if (mMoveY > 0) {
                    if (mMoveY * 0.35f > mHeadHeight) mState = RELEASE_TO_REFRESH;
                    else mState = PULL_TO_REFRESH;
                    if (mIsLoadingPullDown)
                        mCircleLoadingView.setPadding(0, (int) (0 + mMoveY * 0.35f), 0, 0);
                    else {
                        mCircleLoadingView.setPadding(0, (int) (-mHeadHeight + mMoveY * 0.35f), 0, 0);
                        mRatio = mMoveY * 1.0f / 2.5f / mHeadHeight;
//                        头部执行下拉效果
//                        if (!mUcRefreshHead.isLoading()) mUcRefreshHead.performPull(mRatio);
                    }
                    return true;
                } else if (mMoveY < 0) {
                    if (mIsLoadingPullDown && mIsDownAction && mCircleLoadingView.getPaddingTop() > -mHeadHeight) {
                        mCircleLoadingView.setPadding(0, (int) (0 + mMoveY * 0.35f), 0, 0);
                        mState = PULL_TO_REFRESH;
                        return true;
                    } else mState = HIDE;
                    if (!mIsPostDown) {
                        ev.setAction(MotionEvent.ACTION_DOWN);
                        mIsPostDown = true;
                    } else ev.setAction(MotionEvent.ACTION_MOVE);
                    if (mCircleLoadingView.getPaddingTop() > -mHeadHeight)
                        mCircleLoadingView.setPadding(0, -mHeadHeight, 0, 0);
                    return super.dispatchTouchEvent(ev);
                }
                break;
            case MotionEvent.ACTION_UP:
                mIsPostDown = false;
                mIsLoadingPullDown = false;
                mIsDownAction = false;
                if (System.currentTimeMillis() - mOnTouchTime <= 1000 && Math.abs(mMoveY) < mTouchSlop) {
                    mMoveY = 0;
                    mMonitorClick = true;
                    ev.setAction(MotionEvent.ACTION_DOWN);
                    dispatchTouchEvent(ev);
                    return true;
                }
                if (mControlHeadAnimator != null && mControlHeadAnimator.isRunning() || mState == HIDE || mCircleLoadingView.getPaddingTop() == -mHeadHeight)
                    return super.onTouchEvent(ev);
                mControlHeadAnimator = new ValueAnimator();
                if (mState == RELEASE_TO_REFRESH)
                    mControlHeadAnimator.setIntValues(mCircleLoadingView.getPaddingTop(), -mHeadHeight);
                else
                    mControlHeadAnimator.setIntValues(mCircleLoadingView.getPaddingTop(), -mHeadHeight);
                mControlHeadAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mCircleLoadingView.setPadding(0, (Integer) animation.getAnimatedValue(), 0, 0);
//                        if (mState==PULL_TO_REFRESH) mCircleLoadingView.performPull(mRatio * (1 - animation.getAnimatedFraction()));
                    }
                });
                mControlHeadAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (mState == RELEASE_TO_REFRESH) {
                            mState = REFRESHING;
                            if (mListener != null) mListener.onRefreshing();
                        }
                    }
                });
                mControlHeadAnimator.setDuration(260);
                mControlHeadAnimator.start();
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mControlHeadAnimator != null && mControlHeadAnimator.isRunning()) {
            mControlHeadAnimator.removeAllUpdateListeners();
            mControlHeadAnimator.removeAllListeners();
            mControlHeadAnimator.cancel();
            mControlHeadAnimator = null;
        }
    }

    /**
     * 计算RecyclerView当前第一个完全可视位置
     */
    private void calculateFirstPos() {
        RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
        if (mRecyclerView != null && manager != null) {
            if (manager instanceof StaggeredGridLayoutManager) {
                if (mVisiblePositions == null)
                    mVisiblePositions = new int[(((StaggeredGridLayoutManager) manager).getSpanCount())];
                ((StaggeredGridLayoutManager) manager).findFirstCompletelyVisibleItemPositions(mVisiblePositions);
                mFirstPosition = mVisiblePositions[0];
            } else if (manager instanceof GridLayoutManager) {
                mFirstPosition = ((GridLayoutManager) manager).findFirstCompletelyVisibleItemPosition();
            } else {
                mFirstPosition = ((LinearLayoutManager) manager).findFirstCompletelyVisibleItemPosition();
            }
        }
    }

    public void setRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    public interface OnRefreshListener {
        void onRefreshing();
    }
}
