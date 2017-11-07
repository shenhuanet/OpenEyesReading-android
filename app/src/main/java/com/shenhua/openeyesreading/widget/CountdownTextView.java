package com.shenhua.openeyesreading.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * 倒计时textview
 * Created by shenhua on 8/5/2016.
 */
public class CountdownTextView extends TextView implements View.OnClickListener {

    private int time;
    private String format;

    private void init(Context context) {
        setText("获取验证码");
        setOnClickListener(this);
    }

    public void setTime(int time) {
        this.time = time;
        this.format = "请%ds后重试";
    }

    public CountdownTextView(Context context) {
        super(context);
        init(context);
    }

    public CountdownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CountdownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public void onClick(View v) {
        TimeCount timer = new TimeCount(time, 1000);
        timer.start();
    }


    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            setText("获取验证码");
            setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            setClickable(false);
            setText(String.format(format, millisUntilFinished / 1000));
        }
    }
}