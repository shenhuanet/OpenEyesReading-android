package com.shenhua.toolbarmenu;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.DisplayMetrics;
import android.util.StateSet;
import android.view.Display;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

/**
 * Util
 * Created by shenhua on 2016/8/17.
 */
public class Util {

    private static Util ourInstance = new Util();

    private Util() {
    }

    public static Util getInstance() {
        return ourInstance;
    }

    public float dp2px(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public FrameLayout.LayoutParams[] getDotParams(int btnW, int btnH, int dotW, int dotH) {
        FrameLayout.LayoutParams[] ps = new FrameLayout.LayoutParams[8];
        int dis1 = btnW / 12;
        int dis2 = (int) (dis1 * Math.sqrt(3));
        ps[0] = new FrameLayout.LayoutParams(dotW, dotH);
        ps[0].leftMargin = btnW / 3 - dotW / 2;
        ps[0].topMargin = btnH / 2 - dis2 - dotW / 2;
        ps[1] = new FrameLayout.LayoutParams(dotW, dotH);
        ps[1].leftMargin = btnW / 2 - dotW / 2;
        ps[1].topMargin = btnH / 2 - dis2 - dotW / 2;
        ps[2] = new FrameLayout.LayoutParams(dotW, dotH);
        ps[2].leftMargin = btnW * 2 / 3 - dotW / 2;
        ps[2].topMargin = btnH / 2 - dis2 - dotW / 2;
        ps[3] = new FrameLayout.LayoutParams(dotW, dotH);
        ps[3].leftMargin = btnW / 2 - dis1 - dotW / 2;
        ps[3].topMargin = btnH / 2 - dotW / 2;
        ps[4] = new FrameLayout.LayoutParams(dotW, dotH);
        ps[4].leftMargin = btnW / 2 + dis1 - dotW / 2;
        ps[4].topMargin = btnH / 2 - dotW / 2;
        ps[5] = new FrameLayout.LayoutParams(dotW, dotH);
        ps[5].leftMargin = btnW / 3 - dotW / 2;
        ps[5].topMargin = btnH / 2 + dis2 - dotW / 2;
        ps[6] = new FrameLayout.LayoutParams(dotW, dotH);
        ps[6].leftMargin = btnW / 2 - dotW / 2;
        ps[6].topMargin = btnH / 2 + dis2 - dotW / 2;
        ps[7] = new FrameLayout.LayoutParams(dotW, dotH);
        ps[7].leftMargin = btnW * 2 / 3 - dotW / 2;
        ps[7].topMargin = btnH / 2 + dis2 - dotW / 2;

        return ps;
    }

    public int getScreenWidth(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public int getScreenHeight(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public int[][] getEndLocations(int screenW, int screenH, int buttonW) {
        int[][] endLocations = new int[9][2];
        int dis1 = buttonW * 9 / 8;
        int dis2 = (int) (dis1 / 2 * Math.sqrt(3));
        endLocations[0][0] = screenW / 2 - dis1 - buttonW / 2;
        endLocations[0][1] = screenH / 2 - dis2 - buttonW / 2;
        endLocations[1][0] = screenW / 2 - buttonW / 2;
        endLocations[1][1] = screenH / 2 - dis2 - buttonW / 2;
        endLocations[2][0] = screenW / 2 + dis1 - buttonW / 2;
        endLocations[2][1] = screenH / 2 - dis2 - buttonW / 2;
        endLocations[3][0] = screenW / 2 - dis1 / 2 - buttonW / 2;
        endLocations[3][1] = screenH / 2 - buttonW / 2;
        endLocations[4][0] = screenW / 2 + dis1 / 2 - buttonW / 2;
        endLocations[4][1] = screenH / 2 - buttonW / 2;
        endLocations[5][0] = screenW / 2 - dis1 - buttonW / 2;
        endLocations[5][1] = screenH / 2 + dis2 - buttonW / 2;
        endLocations[6][0] = screenW / 2 - buttonW / 2;
        endLocations[6][1] = screenH / 2 + dis2 - buttonW / 2;
        endLocations[7][0] = screenW / 2 + dis1 - buttonW / 2;
        endLocations[7][1] = screenH / 2 + dis2 - buttonW / 2;
        return endLocations;
    }

    public void setCircleButtonStateListDrawable(View circleButton, int radius,int color) {
        WeakReference<Bitmap> imageNormal = new WeakReference<>(Bitmap.createBitmap(2 * radius, 2 * radius, Bitmap.Config.ARGB_8888));
        Canvas canvasNormal = new Canvas(imageNormal.get());
        Paint paintNormal = new Paint();
        paintNormal.setAntiAlias(true);
        paintNormal.setColor(color);
        canvasNormal.drawCircle(radius, radius, radius, paintNormal);
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(StateSet.WILD_CARD, new BitmapDrawable(circleButton.getContext().getResources(), imageNormal.get()));
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            circleButton.setBackground(stateListDrawable);
        } else {
            circleButton.setBackgroundDrawable(stateListDrawable);
        }

    }

    public static class BLVInterpolator implements Interpolator {

        public BLVInterpolator(Class easingType) {
            this.easingType = easingType;
        }

        @Override
        public float getInterpolation(float input) {

            return getOffset(input);
        }

        private Class easingType;

        public float getOffset(float offset) {
            try {
                return ((CubicBezier) easingType.getConstructor().newInstance()).getOffset(offset);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Error("CubicBezier init error.");
            }
        }
    }

}