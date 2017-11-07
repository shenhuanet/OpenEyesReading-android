package com.shenhua.openeyesreading.frag.login;

import android.view.View;

import com.shenhua.openeyesreading.R;

import butterknife.OnClick;

/**
 * Created by shenhua on 11/25/2016.
 * Email shenhuanet@126.com
 */
public class ForgotFragment extends BaseLoginFrag {

    private static ForgotFragment instance;

    public static ForgotFragment getInstance() {
        if (instance == null) instance = new ForgotFragment();
        return instance;
    }

    @Override
    public int getLayoutViewId() {
        return R.layout.view_login_forgot;
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.tv_register, R.id.tv_login})
    void clicks(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out)
                        .replace(R.id.fragment, RegisterFragment.getInstance())
                        .commit();
                break;
            case R.id.tv_login:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out)
                        .replace(R.id.fragment, LoginFragment.getInstance())
                        .commit();
                break;
        }
    }
}
