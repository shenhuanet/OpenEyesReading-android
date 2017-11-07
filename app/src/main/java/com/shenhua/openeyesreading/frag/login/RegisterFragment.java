package com.shenhua.openeyesreading.frag.login;

import android.view.View;

import com.shenhua.openeyesreading.R;

import butterknife.OnClick;

/**
 * Created by shenhua on 11/25/2016.
 * Email shenhuanet@126.com
 */
public class RegisterFragment extends BaseLoginFrag {

    private static RegisterFragment instance;

    public static RegisterFragment getInstance() {
        if (instance == null) instance = new RegisterFragment();
        return instance;
    }

    @Override
    public int getLayoutViewId() {
        return R.layout.view_login_register;
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.tv_forgot, R.id.tv_login})
    void clicks(View v) {
        switch (v.getId()) {
            case R.id.tv_forgot:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out)
                        .replace(R.id.fragment, ForgotFragment.getInstance())
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
