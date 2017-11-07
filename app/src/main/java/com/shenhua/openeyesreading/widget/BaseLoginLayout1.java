package com.shenhua.openeyesreading.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.shenhua.comlib.util.MeasureUtils;

/**
 * 包含登录、注册、忘记密码的layout
 * Created by shenhua on 11/22/2016.
 * Email shenhuanet@126.com
 */
public class BaseLoginLayout1 extends LinearLayout {

    private View loginView;// 登录界面
    private View registView;// 注册界面
    private View forgotView;// 忘记密码界面
    private int mScreenW;
    private int margin = 10;

    private int bigW;
    private int bigH;
    private int smallW;
    private int current = 1;// 当前位置

    public BaseLoginLayout1(Context context) {
        this(context, null);
    }

    public BaseLoginLayout1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseLoginLayout1(Context context, AttributeSet attrs, int defStyleAttr) {
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
        LayoutParams paramsCenter = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsCenter.width = bigW;
        paramsCenter.height = bigW;
        paramsCenter.setMargins(margin, margin, margin, margin);
//        paramsCenter.setMargins(margin, 0, margin, 0);
        registView.setLayoutParams(paramsCenter);
        registView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                registView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                bigH = registView.getHeight();
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.height = bigH;
                BaseLoginLayout1.this.setLayoutParams(params);
            }
        });

        LayoutParams paramsLeftEdge = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsLeftEdge.width = smallW;
        paramsLeftEdge.height = smallW;
//        paramsLeftEdge.leftMargin = margin;
        paramsLeftEdge.setMargins(margin, margin, margin, margin);
        loginView.setLayoutParams(paramsLeftEdge);

        LayoutParams paramsRightEdge = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsRightEdge.width = smallW;
        paramsRightEdge.height = smallW;
//        paramsRightEdge.rightMargin = margin;
        paramsRightEdge.setMargins(margin, margin, margin, margin);
        forgotView.setLayoutParams(paramsRightEdge);
        initView();
    }

    private void initView() {
        loginView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == 0) return;
                System.out.println("shenhua sout:" + loginView.getX() + "  " + loginView.getY());
                loginView.setX(mScreenW / 2 - smallW / 2);
                loginView.setY(bigH / 2 - smallW / 2);
//                ObjectAnimator a1 = ObjectAnimator.ofFloat(loginView, "translationX", 0, mScreenW / 2);
//                ObjectAnimator a2 = ObjectAnimator.ofFloat(loginView, "translationY", 0, -bigH / 2);
//                ObjectAnimator a1 = ObjectAnimator.ofFloat(loginView, "translationX", 0, 200);
//                ObjectAnimator a2 = ObjectAnimator.ofFloat(loginView, "translationY", 0, -50);
//                AnimatorSet set = new AnimatorSet();
//                set.setDuration(1000);
//                set.play(a1).with(a2);
//                set.start();
//                set.addListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        toBigAnim(loginView);
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animation) {
//
//                    }
//                });
//                toBigAnim(loginView);
//                toSmallAnimView();
                tos();
                current = 0;
            }
        });

        registView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == 1) return;
//                toBigAnim(registView);
//                toSmallAnimView();
                current = 1;
            }
        });

        forgotView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == 2) return;
//                toBigAnim(forgotView);
//                toSmallAnimView();
                current = 2;
            }
        });
    }

    private void tos() {
        ObjectAnimator a1 = ObjectAnimator.ofFloat(loginView, "ScaleX", 1, 3);
        ObjectAnimator a2 = ObjectAnimator.ofFloat(loginView, "ScaleY", 1, 3);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(1000);
        set.play(a1).with(a2);
        set.start();
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
        final LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(smallW, bigW);
        valueAnimator.setDuration(1000);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.width = params.height = (int) animation.getAnimatedValue();
                params.setMargins(margin, margin, margin, margin);
                view.setLayoutParams(params);
            }
        });
    }

    private void toSmallAnim(final View view) {
        final LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(bigW, smallW);
        valueAnimator.setDuration(1000);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.width = params.height = (int) animation.getAnimatedValue();
                params.setMargins(margin, margin, margin, margin);
                view.setLayoutParams(params);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("shenhua sout:  x:" + event.getX() + "   y:" + event.getY());
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 抛物线
     *
     * @param view
     */
    public void paowuxian(View view) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(1000);
        valueAnimator.setObjectValues(new PointF(500, 300));
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {
            // fraction = t / duration
            @Override
            public PointF evaluate(float fraction, PointF startValue,
                                   PointF endValue) {
                // x方向200px/s ，则y方向0.5 * 10 * t
                PointF point = new PointF();
//                point.x = 200 * fraction * 3;
//                point.y = 0.5f * 200 * (fraction * 3) * (fraction * 3);
                point.x = fraction + 1;
                point.y = fraction + 1;
                return point;
            }
        });
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                loginView.setX(point.x);
                loginView.setY(point.y);
            }
        });
    }

    public void roundLoad(View myView) {
        int cx = (myView.getLeft() + myView.getRight()) / 2;
        int cy = (myView.getTop() + myView.getBottom()) / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight());
        AnimatorSet animatorSet = new AnimatorSet();
        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
        anim.setDuration(1000);
        anim.setInterpolator(new AccelerateInterpolator());

        Animator anim1 = ObjectAnimator.ofFloat(myView, "translationZ", 0f, 50f);
        anim1.setDuration(1500);
        anim1.setInterpolator(new AccelerateInterpolator());

        animatorSet.play(anim).with(anim1);
        // make the view visible and start the animation
        myView.setVisibility(View.VISIBLE);
        animatorSet.start();
    }
}
