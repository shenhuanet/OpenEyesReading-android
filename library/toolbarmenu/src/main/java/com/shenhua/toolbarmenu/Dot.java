package com.shenhua.toolbarmenu;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

public class Dot extends View {
    public Dot(Context context) {
        this(context, null);
    }

    public Dot(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundResource(R.drawable.dot);
    }

    public void setColor(int color) {
        ((GradientDrawable)getBackground()).setColor(color);
    }

}
