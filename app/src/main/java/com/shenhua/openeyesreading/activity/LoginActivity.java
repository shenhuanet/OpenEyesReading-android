package com.shenhua.openeyesreading.activity;

import com.shenhua.comlib.annotation.ActivityFragmentInject;
import com.shenhua.comlib.base.BaseActivity;
import com.shenhua.openeyesreading.R;
import com.shenhua.openeyesreading.frag.login.LoginFragment;

import butterknife.ButterKnife;

/**
 * 登录
 * Created by shenhua on 2016/5/20.
 */
@ActivityFragmentInject(contentViewId = R.layout.activity_login, toolbarId = R.id.toolbar,
        toolbarTitle = R.string.login_and_regist, toolbarHomeAsUp = true)
public class LoginActivity extends BaseActivity {

    @Override
    protected void initView(BaseActivity baseActivity) {
        ButterKnife.bind(this);
        setToolbarTitle(R.id.toolbar_title);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, LoginFragment.getInstance()).commit();
    }
}
