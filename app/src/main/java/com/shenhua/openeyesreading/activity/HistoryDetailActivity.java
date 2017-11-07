package com.shenhua.openeyesreading.activity;

import android.content.Intent;

import com.shenhua.comlib.annotation.ActivityFragmentInject;
import com.shenhua.comlib.base.BaseActivity;
import com.shenhua.openeyesreading.R;

import butterknife.ButterKnife;

/**
 * Created by shenhua on 11/21/2016.
 * Email shenhuanet@126.com
 */
@ActivityFragmentInject(contentViewId = R.layout.activity_history_detail,
        toolbarId = R.id.toolbar, toolbarHomeAsUp = true)
public class HistoryDetailActivity extends BaseActivity {

    @Override
    protected void initView(BaseActivity baseActivity) {
        ButterKnife.bind(baseActivity);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String href = intent.getStringExtra("href");
        setToolbarTitle(title, R.id.toolbar_title);
        showSnackBar(href);
    }
}
