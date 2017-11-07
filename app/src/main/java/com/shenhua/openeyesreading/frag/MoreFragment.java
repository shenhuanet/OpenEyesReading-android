package com.shenhua.openeyesreading.frag;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.shenhua.comlib.AppUtils;
import com.shenhua.comlib.annotation.ActivityFragmentInject;
import com.shenhua.comlib.base.BaseFragment;
import com.shenhua.comlib.base.BaseSpacesItemDecoration;
import com.shenhua.openeyesreading.R;
import com.shenhua.openeyesreading.activity.LoginActivity;
import com.shenhua.openeyesreading.activity.NewsActivity;
import com.shenhua.openeyesreading.activity.SinaPhotoActivity;
import com.shenhua.openeyesreading.adapter.MoreAdapter;
import com.shenhua.openeyesreading.bean.MoreData;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 更多
 * Created by shenhua on 2016/5/7.
 */
@ActivityFragmentInject(contentViewId = R.layout.frag_more,
        toolbarId = R.id.toolbar, toolbarTitle = R.string.module_more,
        hasOptionsMenu = true, menuId = R.menu.menu_more)
public class MoreFragment extends BaseFragment {

    @Bind(R.id.layout_toolbar)
    LinearLayout mToolbarLayout;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.scrollView)
    NestedScrollView mScrollView;
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.layout_photo)
    LinearLayout mPhotoLayout;

    private MoreAdapter adapter;
    private List<MoreData> datas;
    String[] str = {
            "http://5.66825.com/download/pic/000/325/93b883b3a81fad6c729640a6a03a03e8.jpg",
            "http://pic2.ooopic.com/12/44/26/26bOOOPICff.jpg",
            "http://go.cndesign.com/upload/works/20130123_3FB5E634945344456963285.jpg",
            "http://www.neovida.cn/images/403.jpg",
            "http://pic2.ooopic.com/01/09/12/60b1OOOPICe9.jpg",
            "http://i.liveport.cn/images/201303/600x600/8628_P_1362708324714.jpg",
            "http://www.yooyoo360.com/photo/2009-1-1/20090112195839925.jpg",
            "http://www.gdpx.com.cn/files/modimg/3733_big.jpg"};

    @Override
    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        setToolbarTitle(R.id.toolbar_title);
        mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) { // 450
                if (scrollY >= 0) {
                    float alpha = (float) scrollY / 450;
                    if (alpha >= 1) {
                        alpha = 1;
                    }
                    mToolbarLayout.setAlpha(alpha);
                    mToolbarLayout.setVisibility(View.VISIBLE);
                } else {
                    mToolbarLayout.setVisibility(View.GONE);
                }
            }
        });
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.addItemDecoration(new BaseSpacesItemDecoration(4, false));
        initAdapter();
        initDatas();

    }

    private void initAdapter() {
        adapter = new MoreAdapter(getContext(), datas);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MoreAdapter.OnItemClickListener() {
            @Override
            public void itemClicked(int position, String href) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(getActivity(), NewsActivity.class);
                        intent.putExtra("title", "头条");
                        intent.putExtra("id", AppUtils.Constants.HEADLINE_ID);
                        intent.putExtra("type", AppUtils.Constants.HEADLINE_TYPE);
                        startActivity(intent);
                        break;
                    case 1:
                        startActivity(new Intent(getContext(), SinaPhotoActivity.class));
                        break;
                }
            }
        });
    }

    private void initDatas() {
        String[] titles = getResources().getStringArray(R.array.titles_more);
        if (datas == null) datas = new ArrayList<>();
        datas.clear();
        for (int i = 0; i < str.length; i++) {
            MoreData data = new MoreData();
            data.setTitle(titles[i]);
            data.setId(i);
            data.setImg(str[i]);
            datas.add(data);
        }
        adapter.setDatas(datas);
    }

    private void updateDatas(List<MoreData> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.iv_photo, R.id.layout_photo})
    public void clicks(View v) {
        switch (v.getId()) {
            case R.id.iv_photo:
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            case R.id.layout_photo:

                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_more) {
            showSnackBar("1231321313");
        }
        return super.onOptionsItemSelected(item);
    }
}
