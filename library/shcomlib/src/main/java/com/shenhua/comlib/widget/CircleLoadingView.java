package com.shenhua.comlib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 圆形大小缩放的LoadingView
 * Created by shenhua on 8/23/2016.
 */
public class CircleLoadingView extends View {

    private static final int DEF_MIN_RADIUS = 5;
    private int drawableColor = Color.BLUE;
    private int maxRadius;
    private int curRadius;
    private boolean isExpanding = false;
    private CircleDrawable mCircle;

    public CircleLoadingView(Context context) {
        this(context, null);
    }

    public CircleLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDrawableColor(int color) {
        drawableColor = color;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int minSide = w > h ? h : w;
        maxRadius = minSide / 2;
        curRadius = maxRadius;
        Rect rect = new Rect(0, 0, w, h);
        mCircle = new CircleDrawable(curRadius, drawableColor);
        mCircle.setBounds(rect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCircle.draw(canvas);
        updateCircle();
        invalidate();
    }

    private void updateCircle() {
        if (isExpanding) curRadius++;
        else curRadius--;
        if (curRadius > maxRadius) {
            curRadius = maxRadius;
            isExpanding = false;
        }
        if (curRadius < DEF_MIN_RADIUS) {
            curRadius = DEF_MIN_RADIUS;
            isExpanding = true;
        }
        mCircle.setRadius(curRadius);
    }

    public class CircleDrawable extends Drawable {

        private Paint mPaint;
        private float radius;

        public CircleDrawable(float radius, int color) {
            this.radius = radius;
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(color);
        }

        @Override
        public void draw(Canvas canvas) {
            Rect bounds = getBounds();
            canvas.drawCircle(bounds.centerX(), bounds.centerY(), radius, mPaint);
        }

        @Override
        public void setAlpha(int alpha) {
            if (alpha != mPaint.getAlpha()) {
                mPaint.setAlpha(alpha);
                invalidateSelf();
            }
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {
            mPaint.setColorFilter(colorFilter);
            invalidateSelf();
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        public int getColor() {
            return mPaint.getColor();
        }

        public void setColor(int color) {
            mPaint.setColor(color);
            invalidateSelf();
        }

        public float getRadius() {
            return radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
            invalidateSelf();
        }
    }

}
