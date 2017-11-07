package com.shenhua.toolbarmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * ToolbarMenu
 * Created by shenhua on 2016/8/17.
 */
public class ToolbarMenu extends FrameLayout implements CircleButton.OnCircleButtonClickListener {

    private final static int STATE_CLOSED = 0;
    private final static int STATE_OPENING = 1;
    private final static int STATE_OPEN = 2;
    private final static int STATE_CLOSING = 3;
    private final static int BTN_NUM = 8;
    private final static int COLOR_SELECT = Color.parseColor("#FFFF00");
    private final static int COLOR_NORMAL = Color.parseColor("#F9F9F9");

    private ViewGroup animationLayout = null;
    private FrameLayout frameLayout;
    private View ripple;
    private Dot[] dots = new Dot[BTN_NUM];
    private CircleButton[] circleButtons = new CircleButton[BTN_NUM];
    private int[][] startLocations = new int[BTN_NUM][2];
    private int[][] endLocations = new int[BTN_NUM][2];
    private int state = STATE_CLOSED;
    private int frames = 80;
    private int duration = 800;
    private int delay = 100;
    private final static int LAYOUT_WH = 112;
    private final static int DOT_WH = 10;
    private int buttonWidth = (int) Util.getInstance().dp2px(88);
    private int rotateDegree = 720;
    private boolean isAnimating = false;
    private boolean cancelable = true;

    private Context mContext;
    private AnimatorListener animatorListener = null;
    private OnCirButtonClickListener onCirButtonClickListener = null;

    public ToolbarMenu(Context context) {
        this(context, null);
    }

    public ToolbarMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LayoutInflater.from(context).inflate(R.layout.toolbar_menu_layout, this, true);
        } else {
            LayoutInflater.from(context).inflate(R.layout.toolbar_menu_layout_below_lollipop, this, true);
        }
        frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        ripple = findViewById(R.id.ripple);
        setRipple();
        setWillNotDraw(false);
    }

    public void init(String[] strings, int currentIndex) {
        frameLayout.removeAllViews();
        FrameLayout.LayoutParams[] ps = Util.getInstance().getDotParams(LAYOUT_WH, LAYOUT_WH, DOT_WH, DOT_WH);
        for (int i = 0; i < BTN_NUM; i++) {
            circleButtons[i] = new CircleButton(mContext);
            circleButtons[i].setOnCircleButtonClickListener(this, i);
            if (strings != null) circleButtons[i].setText(strings[i]);
            dots[i] = new Dot(mContext);
            if (currentIndex == i) {
                circleButtons[i].setCircleBackground(COLOR_SELECT);
                dots[i].setColor(COLOR_SELECT);
            } else {
                circleButtons[i].setCircleBackground(COLOR_NORMAL);
                dots[i].setColor(COLOR_NORMAL);
            }
            frameLayout.addView(dots[i], ps[i]);
        }
    }

    private void shoot() {
        if (isAnimating) return;
        isAnimating = true;
        dimAnimationLayout();
        startShowAnimations();
    }

    private ViewGroup createAnimationLayout() {
        ViewGroup rootView = (ViewGroup) ((Activity) mContext).getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(layoutParams);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    private View setViewLocationInAnimationLayout(final View view, int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(buttonWidth, buttonWidth);
        lp.leftMargin = x;
        lp.topMargin = y;
        animationLayout.addView(view, lp);
        return view;
    }

    private void startShowAnimations() {
        if (animationLayout != null) animationLayout.removeAllViews();
        int width = Util.getInstance().getScreenWidth(mContext);
        int height = Util.getInstance().getScreenHeight(mContext);
        endLocations = Util.getInstance().getEndLocations(width, height, buttonWidth);
        for (int i = 0; i < BTN_NUM; i++) {
            dots[i].getLocationOnScreen(startLocations[i]);
            startLocations[i][0] -= (buttonWidth - dots[i].getWidth()) / 2;
            startLocations[i][1] -= (buttonWidth - dots[i].getHeight()) / 2;
            setShowAnimation(dots[i], circleButtons[i], startLocations[i], endLocations[i], i);
        }
    }

    private void startHideAnimations() {
        isAnimating = true;
        lightAnimationLayout();
        for (int i = 0; i < BTN_NUM; i++) {
            setHideAnimation(dots[i], circleButtons[i], endLocations[i], startLocations[i], i);
        }
    }

    private void setShowAnimation(final View dot, final View btn, int[] start, int[] end, final int index) {
        btn.bringToFront();
        final View view = setViewLocationInAnimationLayout(btn, start);
        float[] sl = new float[2];
        float[] el = new float[2];
        sl[0] = start[0] * 1.0f;
        sl[1] = start[1] * 1.0f;
        el[0] = end[0] * 1.0f;
        el[1] = end[1] * 1.0f;
        float[] xs = new float[frames + 1];
        float[] ys = new float[frames + 1];
        getShowXY(sl, el, xs, ys);
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(view, "x", xs).setDuration(duration);
        xAnimator.setStartDelay(delay * index);
        xAnimator.setInterpolator(new Util.BLVInterpolator(EaseOutBack.class));
        xAnimator.start();
        ObjectAnimator yAnimator = ObjectAnimator.ofFloat(view, "y", ys).setDuration(duration);
        yAnimator.setStartDelay(delay * index);
        yAnimator.setInterpolator(new Util.BLVInterpolator(EaseOutBack.class));
        yAnimator.start();
        float scaleW = DOT_WH * 1.0f / buttonWidth;
        float scaleH = DOT_WH * 1.0f / buttonWidth;
        view.setScaleX(scaleW);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", scaleW, 1f).setDuration(duration);
        scaleXAnimator.setStartDelay(delay * index);
        scaleXAnimator.setInterpolator(new Util.BLVInterpolator(EaseOutBack.class));
        scaleXAnimator.start();
        view.setScaleY(scaleH);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", scaleH, 1f).setDuration(duration);
        scaleYAnimator.setStartDelay(delay * index);
        scaleYAnimator.setInterpolator(new Util.BLVInterpolator(EaseOutBack.class));
        scaleYAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                dot.setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimating = false;
            }
        });
        scaleYAnimator.start();
        View textView = ((CircleButton) btn).getTextView();
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f).setDuration(duration);
        alphaAnimator.setStartDelay(delay * index);
        alphaAnimator.setInterpolator(new Util.BLVInterpolator(EaseOutBack.class));
        alphaAnimator.start();
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(((CircleButton) view).getFrameLayout(), "rotation", 0, rotateDegree).setDuration(duration);
        rotateAnimator.setStartDelay(delay * index);
        rotateAnimator.setInterpolator(new Util.BLVInterpolator(EaseOutBack.class));
        rotateAnimator.start();
    }

    private void setHideAnimation(final View dot, final View btn, int[] start, int[] end, final int index) {
        float[] sl = new float[2];
        float[] el = new float[2];
        sl[0] = start[0] * 1.0f;
        sl[1] = start[1] * 1.0f;
        el[0] = end[0] * 1.0f;
        el[1] = end[1] * 1.0f;
        float[] xs = new float[frames + 1];
        float[] ys = new float[frames + 1];
        getHideXY(sl, el, xs, ys);
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(btn, "x", xs).setDuration(duration);
        xAnimator.setStartDelay(index * delay);
        xAnimator.setInterpolator(new Util.BLVInterpolator(EaseOutCircle.class));
        xAnimator.start();
        ObjectAnimator yAnimator = ObjectAnimator.ofFloat(btn, "y", ys).setDuration(duration);
        yAnimator.setStartDelay(index * delay);
        yAnimator.setInterpolator(new Util.BLVInterpolator(EaseOutCircle.class));
        yAnimator.start();
        float scaleW = DOT_WH * 1.0f / buttonWidth;
        float scaleH = DOT_WH * 1.0f / buttonWidth;
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(btn, "scaleX", 1f, scaleW).setDuration(duration);
        scaleXAnimator.setStartDelay(index * delay);
        scaleXAnimator.setInterpolator(new Util.BLVInterpolator(EaseOutCircle.class));
        scaleXAnimator.start();
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(btn, "scaleY", 1f, scaleH).setDuration(duration);
        scaleYAnimator.setStartDelay(index * delay);
        scaleYAnimator.setInterpolator(new Util.BLVInterpolator(EaseOutCircle.class));
        scaleYAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                dot.setVisibility(VISIBLE);
            }
        });
        scaleYAnimator.start();
        View textView = ((CircleButton) btn).getTextView();
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(textView, "alpha", 1f, 0f).setDuration(duration);
        alphaAnimator.setStartDelay(delay * index);
        alphaAnimator.setInterpolator(new Util.BLVInterpolator(EaseOutCircle.class));
        alphaAnimator.start();
        if (btn instanceof CircleButton) {
            ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(((CircleButton) btn).getFrameLayout(), "rotation", 0, -rotateDegree).setDuration(duration);
            rotateAnimator.setStartDelay(index * delay);
            rotateAnimator.setInterpolator(new Util.BLVInterpolator(EaseLinear.class));
            rotateAnimator.start();
        }
    }

    private void lightAnimationLayout() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(animationLayout, "backgroundColor",
                Color.parseColor("#66000000"),
                Color.parseColor("#00000000"))
                .setDuration(duration + delay * (BTN_NUM - 1));
        objectAnimator.setEvaluator(new ArgbEvaluator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (animatorListener != null) animatorListener.toHide();
                state = STATE_CLOSING;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animationLayout.removeAllViews();
                animationLayout.setVisibility(GONE);
                isAnimating = false;
                if (animatorListener != null) animatorListener.hided();
                state = STATE_CLOSED;
            }
        });
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animatorListener != null)
                    animatorListener.hiding(animation.getAnimatedFraction());
            }
        });
        objectAnimator.start();
    }

    private void dimAnimationLayout() {
        if (animationLayout == null) {
            animationLayout = createAnimationLayout();
            animationLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isAnimating) return;
                    if (cancelable) startHideAnimations();
                }
            });
        }
        animationLayout.setVisibility(VISIBLE);
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(animationLayout, "backgroundColor",
                Color.parseColor("#00000000"),
                Color.parseColor("#66000000"))
                .setDuration(duration + delay * (BTN_NUM - 1));
        objectAnimator.setEvaluator(new ArgbEvaluator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (animatorListener != null) animatorListener.toShow();
                state = STATE_OPENING;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (animatorListener != null) animatorListener.showed();
                state = STATE_OPEN;
            }
        });
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animatorListener != null)
                    animatorListener.showing(animation.getAnimatedFraction());
            }
        });
        objectAnimator.start();
    }

    private void setRipple() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && ripple != null) {
            ripple.setVisibility(VISIBLE);
            ripple.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shoot();
                }
            });
        } else {
            if (ripple != null) ripple.setVisibility(GONE);
            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shoot();
                }
            });
        }
    }

    private void getShowXY(float[] startP, float[] endP, float[] xs, float[] ys) {
        float x1 = startP[0];
        float y1 = startP[1];
        float x2 = endP[0];
        float y2 = endP[1];
        float k = (y2 - y1) / (x2 - x1);
        float b = y1 - x1 * k;
        float per = 1f / frames;
        float xx = endP[0] - startP[0];
        for (int i = 0; i <= frames; i++) {
            float offset = i * per;
            xs[i] = startP[0] + offset * xx;
            ys[i] = k * xs[i] + b;
        }
    }

    private void getHideXY(float[] startPoint, float[] endPoint, float[] xs, float[] ys) {
        float x1 = endPoint[0];
        float y1 = endPoint[1];
        float x3 = startPoint[0];
        float y3 = startPoint[1];
        float x2 = x3 * 2 - x1;
        float a, b, c;
        a = (y1 * (x2 - x3) + y1 * (x3 - x1) + y3 * (x1 - x2)) / (x1 * x1 * (x2 - x3) + x2 * x2 * (x3 - x1) + x3 * x3 * (x1 - x2));
        b = (y1 - y1) / (x1 - x2) - a * (x1 + x2);
        c = y1 - (x1 * x1) * a - x1 * b;
        float per = 1f / frames;
        float xx = endPoint[0] - startPoint[0];
        for (int i = 0; i <= frames; i++) {
            float offset = i * per;
            xs[i] = startPoint[0] + offset * xx;
            ys[i] = a * xs[i] * xs[i] + b * xs[i] + c;
        }
    }

    public interface OnClickListener {
        void onClick();
    }

    public interface AnimatorListener {
        void toShow();

        void showing(float fraction);

        void showed();

        void toHide();

        void hiding(float fraction);

        void hided();
    }

    public interface OnCirButtonClickListener {
        void onBtnClick(int btnIndex);
    }

    public void setOnCirButtonClickListener(OnCirButtonClickListener onCirButtonClickListener) {
        this.onCirButtonClickListener = onCirButtonClickListener;
    }

    public void setCurrentCirBtn(int index) {
        for (int i = 0; i < BTN_NUM; i++) {
            circleButtons[i].setCircleBackground(COLOR_NORMAL);
            dots[i].setColor(COLOR_NORMAL);
        }
        circleButtons[index].setCircleBackground(COLOR_SELECT);
        dots[index].setColor(COLOR_SELECT);
    }

    @Override
    public void onClick(int index) {
        if (state != STATE_OPEN) return;
        if (onCirButtonClickListener != null) onCirButtonClickListener.onBtnClick(index);
        if (!isAnimating) startHideAnimations();
    }

}
