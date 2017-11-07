package com.shenhua.openeyesreading.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.shenhua.comlib.AppUtils;
import com.shenhua.comlib.annotation.ActivityFragmentInject;
import com.shenhua.comlib.base.BaseActivity;
import com.shenhua.comlib.base.BaseFragment;
import com.shenhua.comlib.util.MeasureUtils;
import com.shenhua.openeyesreading.R;
import com.shenhua.openeyesreading.adapter.FragmentAdapter;
import com.shenhua.openeyesreading.presenter.IPhotoPresenterImpl;
import com.shenhua.openeyesreading.view.IPhotoView;
import com.shenhua.openeyesreading.view.PhotoListFrag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 新浪图片界面
 * Created by shenhua on 8/23/2016.
 */
@ActivityFragmentInject(contentViewId = R.layout.activity_photo, toolbarId = R.id.toolbar,
        toolbarTitle = R.string.toolbar_title_sina_photo, toolbarHomeAsUp = true)
public class SinaPhotoActivity extends BaseActivity implements IPhotoView {

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    private IPhotoPresenterImpl mPresenter;

    @Override
    protected void initView(BaseActivity baseActivity) {
        ButterKnife.bind(baseActivity);
        setToolbarTitle(R.id.toolbar_title);
        mPresenter = new IPhotoPresenterImpl(this);
    }

    @Override
    public void initViewPager() {
        List<BaseFragment> fragments = new ArrayList<>();
        final List<String> title = Arrays.asList("精选", "趣图", "美图", "故事");
        fragments.add(PhotoListFrag.newInstance(AppUtils.Constants.SINA_PHOTO_CHOICE_ID, 0));
        fragments.add(PhotoListFrag.newInstance(AppUtils.Constants.SINAD_PHOTO_FUN_ID, 1));
        fragments.add(PhotoListFrag.newInstance(AppUtils.Constants.SINAD_PHOTO_PRETTY_ID, 2));
        fragments.add(PhotoListFrag.newInstance(AppUtils.Constants.SINA_PHOTO_STORY_ID, 3));
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.setmViewPager(viewPager);
        for (int i = 0; i < title.size(); i++) {
            adapter.add(fragments.get(i));
        }
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < title.size(); i++) {
            tabLayout.getTabAt(i).setText(title.get(i));
        }
        dynamicSetTabLayoutMode(tabLayout);
    }

    /**
     * 动态修改tab的模式
     *
     * @param tabLayout tabLayout
     */
    public static void dynamicSetTabLayoutMode(TabLayout tabLayout) {
        int tabTotalWidth = 0;
        for (int i = 0; i < tabLayout.getChildCount(); i++) {
            final View view = tabLayout.getChildAt(i);
            view.measure(0, 0);
            tabTotalWidth += view.getMeasuredWidth();
        }
        if (tabTotalWidth <= MeasureUtils.getScreenSize(tabLayout.getContext()).x) {
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) mPresenter.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.onDestroy();
    }

    @Override
    public void toast(String msg) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
