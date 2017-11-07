package com.shenhua.openeyesreading.frag.login;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shenhua.openeyesreading.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by shenhua on 11/25/2016.
 * Email shenhuanet@126.com
 */
public abstract class BaseLoginFrag extends Fragment {

    public View rootView;
    @Bind(R.id.btn_done)
    Button btnDone;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutViewId(), container, false);
            ButterKnife.bind(this, rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        initView();
        return rootView;
    }

    public abstract
    @LayoutRes
    int getLayoutViewId();

    protected abstract void initView();

    @OnClick(R.id.btn_done)
    public void click(View view) {

    }
}
