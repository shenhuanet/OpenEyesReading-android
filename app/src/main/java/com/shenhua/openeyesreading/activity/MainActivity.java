package com.shenhua.openeyesreading.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

import com.shenhua.bottomtab.BottomTabItem;
import com.shenhua.bottomtab.BottomTabView;
import com.shenhua.bottomtab.OnBottomTabItemClickListener;
import com.shenhua.comlib.annotation.ActivityFragmentInject;
import com.shenhua.comlib.base.BaseActivity;
import com.shenhua.openeyesreading.R;
import com.shenhua.openeyesreading.adapter.FragmentAdapter;
import com.shenhua.openeyesreading.frag.ArticleFragment;
import com.shenhua.openeyesreading.frag.IHistoryFragment;
import com.shenhua.openeyesreading.frag.MoreFragment;
import com.shenhua.openeyesreading.transformer.DepthPage;
import com.shenhua.openeyesreading.widget.CustomViewPager;
import com.shenhua.openeyesreading.widget.FixedSpeedScroller;

import java.lang.reflect.Field;

import butterknife.Bind;
import butterknife.ButterKnife;

@ActivityFragmentInject(contentViewId = R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @Bind(R.id.bottom_tab)
    BottomTabView bottomTabView;
    @Bind(R.id.view_pager)
    CustomViewPager viewPager;
    private FixedSpeedScroller mScroller;
    private boolean first = true;
    private Fragment[] fragments = {new ArticleFragment(), new IHistoryFragment(), new MoreFragment()};
    private long exitTime = 0;

    @Override
    protected void initView(BaseActivity baseActivity) {
        ButterKnife.bind(baseActivity);
        initViewPager();
        initBottomTab();
    }

    private void initViewPager() {
        viewPager.setPageTransformer(true, new DepthPage());
        FragmentAdapter mAdapter = new FragmentAdapter(getSupportFragmentManager());
        mAdapter.setmViewPager(viewPager);
        for (int i = 0; i < 3; i++) {
            Bundle bundle = new Bundle();
            fragments[i].setArguments(bundle);
            mAdapter.add(fragments[i]);
        }
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
        changeViewPagerScroller();
        //初次为用户演示滑动一段距离
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                viewPager.setCurrentItem(1);
//            }
//        }, 1000);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                viewPager.setCurrentIndex(arg0);
                bottomTabView.selectTab(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                float currentPosOfPager = arg0 * viewPager.getWidth() + arg2;
                if (currentPosOfPager >= 200 && !first) {// 初次为用户演示滑动一段距离
                    first = true;
                    viewPager.setCurrentItem(0);
                    //改变viewPager滑动速度
                    mScroller.setDuration(200);
                }
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void initBottomTab() {
        if (bottomTabView != null) {
            bottomTabView.isWithText(false);
            bottomTabView.disableViewPagerSlide();
            bottomTabView.isColoredBackground(true);
            bottomTabView.setItemActiveColorWithoutColoredBackground(ContextCompat.getColor(this, R.color.colorArticle));
        }
        BottomTabItem tabHome = new BottomTabItem(getString(R.string.module_reading), ContextCompat.getColor(this, R.color.colorArticle), R.drawable.icon_read);
        BottomTabItem tabTwo = new BottomTabItem(getString(R.string.module_ihistory), ContextCompat.getColor(this, R.color.colorIHistory), R.drawable.icon_history);
        BottomTabItem tabThree = new BottomTabItem(getString(R.string.module_more), ContextCompat.getColor(this, R.color.colorMore), R.drawable.icon_more);
        bottomTabView.addTab(tabHome);
        bottomTabView.addTab(tabTwo);
        bottomTabView.addTab(tabThree);
        bottomTabView.setOnBottomTabItemClickListener(new OnBottomTabItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                viewPager.setCurrentItem(index);
            }
        });
    }

    private void changeViewPagerScroller() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(viewPager.getContext(), new AccelerateInterpolator(), 100);
            mField.set(viewPager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }
}
