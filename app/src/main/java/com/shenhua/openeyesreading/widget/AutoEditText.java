package com.shenhua.openeyesreading.widget;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.shenhua.openeyesreading.R;

/**
 * 自动限制字数的EditText
 * Created by shenhua on 8/5/2016.
 */
public class AutoEditText extends FrameLayout implements TextWatcher {

    String mCurrentCount = "%d/30";
    int num;
    EditText editText;
    TextView textView;

    private void init() {
        inflate(getContext(), R.layout.view_edittext, this);
        editText = (EditText) findViewById(R.id.ev_content);
        textView = (TextView) findViewById(R.id.tv_num);
        textView.setText(String.format(mCurrentCount, 0));
        editText.addTextChangedListener(this);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        num = editText.getText().length();
        textView.setText(String.format(mCurrentCount, num));
    }

    public AutoEditText(Context context) {
        super(context);
        init();
    }

    public AutoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
}
