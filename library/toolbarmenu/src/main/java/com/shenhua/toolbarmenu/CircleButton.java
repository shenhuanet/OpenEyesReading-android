package com.shenhua.toolbarmenu;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class CircleButton extends FrameLayout {

    private Context mContext;
    private FrameLayout frameLayout;
    private ImageButton imageButton;
    private View ripple;
    private TextView textView;

    private int index;
    private int radius = (int)Util.getInstance().dp2px(80) / 2;
    private OnCircleButtonClickListener onCircleButtonClickListener;

    public CircleButton(Context context) {
        this(context, null);
    }

    public CircleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LayoutInflater.from(mContext).inflate(R.layout.circle_button, this, true);
        } else {
            LayoutInflater.from(mContext).inflate(R.layout.circle_button_below_lollipop, this, true);
        }
        frameLayout = (FrameLayout)findViewById(R.id.frame_layout);
        imageButton = (ImageButton)findViewById(R.id.image_button);
        ripple = findViewById(R.id.ripple);
        textView = (TextView)findViewById(R.id.text);
    }

    public void setOnCircleButtonClickListener(OnCircleButtonClickListener onCircleButtonClickListener,int index) {
        this.onCircleButtonClickListener = onCircleButtonClickListener;
        this.index = index;
        setRipple();
    }

    public void setText(String text) {
        if (textView != null) textView.setText(text);
    }

    public FrameLayout getFrameLayout() {
        return frameLayout;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setCircleBackground(int color) {
        Util.getInstance().setCircleButtonStateListDrawable(imageButton, radius,color);
    }

    public void setRipple() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ripple.setVisibility(VISIBLE);
            ripple.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCircleButtonClickListener.onClick(index);
                }
            });
        } else {
            ripple.setVisibility(GONE);
            imageButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCircleButtonClickListener.onClick(index);
                }
            });
        }
    }

    public interface OnCircleButtonClickListener {
        void onClick(int index);
    }
}
