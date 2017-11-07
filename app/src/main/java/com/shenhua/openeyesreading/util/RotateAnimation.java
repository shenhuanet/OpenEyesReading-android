package com.shenhua.openeyesreading.util;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 缩放动画类
 * Created by shenhua on 2016/6/2.
 */
public class RotateAnimation extends Animation {

    public static final float DEPTH_Z = 200.0f;//Z轴上最大深度。
    public static final long DURATION = 1000;//动画显示时长。
    private float centerX;// 中心坐标
    private float centerY;// 中心坐标
    private Camera mCamera;
    private InterpolatedTimeListener listener;//用于监听动画进度。当值过半时需更新txtNumber的内容。

    public void setInterpolatedTimeListener(InterpolatedTimeListener listener) {
        this.listener = listener;
    }

    /**
     * 这是一个回调函数告诉Animation目标View的大小参数(就是你要运行空间的宽度和高度.)
     **/
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        // 在构造函数之后、getTransformation()之前调用本方法。
        super.initialize(width, height, parentWidth, parentHeight);
        centerX = width / 2;
        centerY = height / 2;
        setDuration(DURATION);
        mCamera = new Camera();
    }

    @Override
    public boolean getTransformation(long currentTime,
                                     Transformation outTransformation) {
        return super.getTransformation(currentTime, outTransformation);
    }

    /***
     * 这个类需要重写,在绘制动画的时候会反复执行. interpolatedTime：该参数从0渐 变为1. 0:表示动画开始执行. 0.5表示中间值.
     * 1:表示动画结束. Transformation:获取变化的矩阵.matrix
     */
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        float from = 0.0f, to = 180.0f;
        float degree = from + (to - from) * interpolatedTime;//翻转的角度
        if (degree == 180)
            return;
        if (interpolatedTime > 0.5f) {
            // 翻转过半的情况下，为保证显示内容不出现镜面效果就是对称形状，需要将角度逆向回来.
            degree = degree - 180;
        }
        float depth = (0.5f - Math.abs(interpolatedTime - 0.5f)) * DEPTH_Z;//深度

        final Matrix matrix = transformation.getMatrix();
        mCamera.save();
        mCamera.translate(0.0f, 0.0f, depth);
        mCamera.rotateY(degree);
        mCamera.getMatrix(matrix);
        mCamera.restore();
        // 确保图片的翻转过程一直处于组件的中心点位置
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
        // interpolatedTime:动画进度值，范围为[0.0f,1.0f]
        if (listener != null) {
            listener.interpolatedTime(interpolatedTime);
        }
    }

    /**
     * 动画进度监听器。
     */
    public interface InterpolatedTimeListener {
        void interpolatedTime(float interpolatedTime);
    }

}
