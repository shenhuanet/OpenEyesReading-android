package com.shenhua.openeyesreading.frag;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.shenhua.comlib.AppUtils;
import com.shenhua.comlib.annotation.ActivityFragmentInject;
import com.shenhua.comlib.base.BaseFragment;
import com.shenhua.openeyesreading.R;
import com.shenhua.openeyesreading.bean.IHistoryBean;
import com.shenhua.openeyesreading.core.HttpApiCallback;
import com.shenhua.openeyesreading.core.HttpApiImpl;
import com.shenhua.toolbarmenu.ToolbarMenu;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 爱历史
 * Created by shenhua on 2016/5/7.
 */
@ActivityFragmentInject(contentViewId = R.layout.frag_ihistory, toolbarId = R.id.toolbar)
public class IHistoryFragment extends BaseFragment implements TabLayout.OnTabSelectedListener, ToolbarMenu.OnCirButtonClickListener {

    @Bind(R.id.appbar)
    AppBarLayout mAppbar;
    @Bind(R.id.tabLayout_top)
    TabLayout mTabLayout;
    @Bind(R.id.frame_content)
    FrameLayout mContentFrame;
    @Bind(R.id.toolbar_btn)
    ToolbarMenu toolbarBoom;
    // 每日精选 推荐阅读 本周点击排行 历史新闻
    private String[] tab_name_array = {"推荐阅读", "老照片", "本周排行", "历史秘闻"};
    private String[] itemS = {"每日精选", "历史揭秘", "历史焦点", "八卦历史", "社会万象", "历史人物", "历史印象", "战史风云"};
    private IHistoryBean iHistoryBean;
    private int currentTab = 0;

    @Override
    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        for (String aTab_name_array : tab_name_array) {
            mTabLayout.addTab(mTabLayout.newTab().setText(aTab_name_array));
        }
        if (itemS.length != 8) {
            throw new IndexOutOfBoundsException("itemS size must be 8.");
        }
        toolbarBoom.init(itemS, 0);
        toolbarBoom.setOnCirButtonClickListener(this);
        mTabLayout.setOnTabSelectedListener(this);
        initData(AppUtils.Constants.URL_MEIRIJINGXUAN);
        setToolbarTitle(itemS[0], R.id.toolbar_title);
    }

    private void initData(String url) {
        HttpApiImpl.getInstance().toGetIHistory(url, new HttpApiCallback() {
            @Override
            public void onSuccess(Object o) {
                iHistoryBean = (IHistoryBean) o;
                mTabLayout.getTabAt(currentTab).select();
                // TODO: 8/19/2016 这里将获取到的数据异步存储到file  synchronized
            }

            @Override
            public void onFailed(int errorCode, String msg) {
                AppUtils.showToast(getActivity(), msg);
                // TODO 从本地file中读取   iHistoryBean =
            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        onTabUpdate(tab);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        onTabUpdate(tab);
    }

    private void onTabUpdate(TabLayout.Tab tab) {
        currentTab = tab.getPosition();
        Fragment fragment = ClassifyFragment.newInstance(iHistoryBean, tab.getPosition());
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_content, fragment);
        ft.commit();
    }

    @Override
    public void onBtnClick(int btnIndex) {
        switch (btnIndex) {
            case 0:
                initData(AppUtils.Constants.URL_MEIRIJINGXUAN);
                break;
            case 1:
                initData(AppUtils.Constants.URL_LISHIJIEMI);
                break;
            case 2:
                initData(AppUtils.Constants.URL_LISHIJAIODIAN);
                break;
            case 3:
                initData(AppUtils.Constants.URL_BAGUALISHI);
                break;
            case 4:
                initData(AppUtils.Constants.URL_SHEHUIWANXIANG);
                break;
            case 5:
                initData(AppUtils.Constants.URL_LISHIRENWU);
                break;
            case 6:
                initData(AppUtils.Constants.URL_LISHIYINXIANG);
                break;
            case 7:
                initData(AppUtils.Constants.URL_ZHANSHIFENGYUN);
                break;
        }
        setToolbarTitle(itemS[btnIndex], R.id.toolbar_title);
        toolbarBoom.setCurrentCirBtn(btnIndex);
    }
}
