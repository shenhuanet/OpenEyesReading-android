package com.shenhua.openeyesreading.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shenhua.openeyesreading.R;

/**
 * 下拉刷新头
 * Created by Shenhua on 5/13/2016.
 */
public class ListViewHeader extends LinearLayout {

    private LinearLayout mContainer;
    private ImageView mArrowImageView;
    private ProgressBar mProgressBar;
    private TextView mHintTextView;
    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;
    private int mState = STATE_NORMAL;
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;

    public ListViewHeader(Context context) {
        super(context);
        initView(context);
    }

    public ListViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * 初始化view
     *
     * @param context
     */
    private void initView(Context context) {
        //初始状态下，设置下拉刷新view高度为0
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_pull_refresh, null);
        this.addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);
        mArrowImageView = (ImageView) findViewById(R.id.smoothlistview_header_arrow);
        mHintTextView = (TextView) findViewById(R.id.smoothlistview_header_hint_textview);
        mProgressBar = (ProgressBar) findViewById(R.id.smoothlistview_header_progressbar);
        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        final int ROTATE_ANIM_DURATION = 180;// 旋转时间
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    /**
     * 设置可见高度
     *
     * @param height
     */
    public void setVisibleHeight(int height) {
        if (height < 0) height = 0;
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    /**
     * 得到可见高度
     *
     * @return
     */
    public int getVisibleHeight() {
        return mContainer.getHeight();
    }

    /**
     * 设置状态
     *
     * @param state
     */
    public void setState(int state) {
        if (state == mState) return;
        if (state == STATE_REFRESHING) {//正在刷新的状态，显示进度
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mArrowImageView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
        switch (state) {
            case STATE_NORMAL:
                if (mState == STATE_READY) mArrowImageView.startAnimation(mRotateDownAnim);
                if (mState == STATE_REFRESHING) mArrowImageView.clearAnimation();
                mHintTextView.setText("下拉刷新");
                break;
            case STATE_READY:
                if (mState != STATE_READY) {
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mRotateUpAnim);
                    mHintTextView.setText("松开刷新数据");
                }
                break;
            case STATE_REFRESHING:
                mHintTextView.setText("正在加载...");
                break;
        }
        mState = state;
    }
}
