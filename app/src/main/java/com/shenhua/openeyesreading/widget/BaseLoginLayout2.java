package com.shenhua.openeyesreading.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.shenhua.comlib.util.MeasureUtils;

/**
 * 包含登录、注册、忘记密码的layout
 * Created by shenhua on 11/22/2016.
 * Email shenhuanet@126.com
 */
public class BaseLoginLayout2 extends LinearLayout {

    private View loginView;// 登录界面
    private View registView;// 注册界面
    private View forgotView;// 忘记密码界面
    private int mScreenW;
//    private int margin = 20;

    private int bigW;
    private int bigH;
    private int smallW;
    private int current = 1;// 当前位置

    public BaseLoginLayout2(Context context) {
        this(context, null);
    }

    public BaseLoginLayout2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseLoginLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        setOrientation(HORIZONTAL);
        mScreenW = new Point(MeasureUtils.getScreenSize(context)).x;
//        Toast.makeText(context, "" + mScreenW, Toast.LENGTH_LONG).show();
//        System.out.println("shenhua sout:--->" + mScreenW); // 1080      720
        bigW = (int) (mScreenW * 0.65);
//        smallW = (int) (mScreenW * 0.175) - margin;
        smallW = (int) (mScreenW * 0.175);
    }

    @Override
    protected void onFinishInflate() {
        loginView = this.getChildAt(0);
        registView = this.getChildAt(1);
        forgotView = this.getChildAt(2);
        LinearLayout.LayoutParams paramsCenter = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsCenter.width = bigW;
        paramsCenter.height = bigW;
        paramsCenter.setMargins(10, 10, 10, 10);
//        paramsCenter.setMargins(margin, 0, margin, 0);
        registView.setLayoutParams(paramsCenter);
        registView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                registView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                bigH = registView.getHeight();
                LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.height = bigH;
                BaseLoginLayout2.this.setLayoutParams(params);
            }
        });

        LinearLayout.LayoutParams paramsLeftEdge = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsLeftEdge.width = smallW;
        paramsLeftEdge.height = smallW;
//        paramsLeftEdge.leftMargin = margin;
        paramsLeftEdge.setMargins(10, 10, 10, 10);
        loginView.setLayoutParams(paramsLeftEdge);

        LinearLayout.LayoutParams paramsRightEdge = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsRightEdge.width = smallW;
        paramsRightEdge.height = smallW;
//        paramsRightEdge.rightMargin = margin;
        paramsRightEdge.setMargins(10, 10, 10, 10);
        forgotView.setLayoutParams(paramsRightEdge);
        initView();
    }

    private void initView() {
        loginView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == 0) return;
                toBigAnim(loginView);
                toSmallAnimView();
                current = 0;
            }
        });

        registView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == 1) return;
                toBigAnim(registView);
                toSmallAnimView();
                current = 1;
            }
        });

        forgotView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == 2) return;
                toBigAnim(forgotView);
                toSmallAnimView();
                current = 2;
            }
        });
    }

    private void toSmallAnimView() {
        View view = null;
        switch (current) {
            case 0:
                view = loginView;
                break;
            case 1:
                view = registView;
                break;
            case 2:
                view = forgotView;
                break;
        }
        toSmallAnim(view);
    }

    private void toBigAnim(final View view) {
        final LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(smallW, bigW);
        valueAnimator.setDuration(1000);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.width = params.height = (int) animation.getAnimatedValue();
                params.setMargins(10, 10, 10, 10);
                view.setLayoutParams(params);
            }
        });
    }

    private void toSmallAnim(final View view) {
        final LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(bigW, smallW);
        valueAnimator.setDuration(1000);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.width = params.height = (int) animation.getAnimatedValue();
                params.setMargins(10, 10, 10, 10);
                view.setLayoutParams(params);
            }
        });
    }

}
