package com.shenhua.openeyesreading.activity;

import android.animation.ObjectAnimator;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.shenhua.comlib.annotation.ActivityFragmentInject;
import com.shenhua.comlib.base.BaseActivity;
import com.shenhua.comlib.util.StringUtils;
import com.shenhua.comlib.widget.RichTextView;
import com.shenhua.openeyesreading.R;
import com.shenhua.openeyesreading.adapter.PhotoDetailAdapter;
import com.shenhua.openeyesreading.bean.SinaPhotoDetail;
import com.shenhua.openeyesreading.presenter.IPhotoDetailPresenterImpl;
import com.shenhua.openeyesreading.view.IPhotoDetailView;
import com.shenhua.openeyesreading.widget.HackyViewPager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 新浪图片详情界面
 * Created by shenhua on 8/25/2016.
 */
@ActivityFragmentInject(contentViewId = R.layout.activity_photo_detail,
        toolbarId = R.id.toolbar, toolbarTitle = R.string.toolbar_title_photo_detail, toolbarHomeAsUp = true)
public class SinaPhotoDetailActivity extends BaseActivity implements IPhotoDetailView {

    @Bind(R.id.view_pager)
    HackyViewPager viewPager;
    @Bind(R.id.tv_photo_detail_title)
    TextView tvTitle;
    @Bind(R.id.tv_photo_detail_page)
    TextView tvPage;
    @Bind(R.id.tv_photo_detail_content)
    RichTextView tvContent;
    private IPhotoDetailPresenterImpl mPresenter;
    private int tvContentWidth;

    @Override
    protected void initView(BaseActivity baseActivity) {
        ButterKnife.bind(baseActivity);
        setToolbarTitle(R.id.toolbar_title);
        String photoId = getIntent().getStringExtra("photoId");
        SinaPhotoDetail data = (SinaPhotoDetail) getIntent().getSerializableExtra("net");
        mPresenter = new IPhotoDetailPresenterImpl(this, photoId, data);
    }

    @Override
    public void initViewPager(SinaPhotoDetail detail) {
        tvTitle.setText(detail.data.title);
        tvTitle.setTag(true);
        final PhotoDetailAdapter adapter = new PhotoDetailAdapter(this, detail.data.pics);
        viewPager.setAdapter(adapter);
        final OnPageChangeListenerAdapter onPageChangeListenerAdapter = new OnPageChangeListenerAdapter(detail, adapter);
        viewPager.addOnPageChangeListener(onPageChangeListenerAdapter);
        tvContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tvContentWidth = tvContent.getMeasuredWidth();
                onPageChangeListenerAdapter.onPageSelected(0);
                tvContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private class OnPageChangeListenerAdapter implements ViewPager.OnPageChangeListener {

        SinaPhotoDetail detail;
        PhotoDetailAdapter adapter;

        OnPageChangeListenerAdapter(SinaPhotoDetail detail, PhotoDetailAdapter adapter) {
            this.detail = detail;
            this.adapter = adapter;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            String s = getString(R.string.photo_page, position + 1, detail.data.pics.size());
            tvPage.setText(s);
            try {
                String alt = detail.data.pics.get(position).alt;
                if (!TextUtils.isEmpty(alt) && !alt.equals(tvContent.getText().toString().trim())) {
                    ObjectAnimator.ofFloat(tvContent, "alpha", 0.5f, 1).setDuration(500).start();
                    tvContent.setRichText(getString(R.string.photo_detail_content, alt));
                    dynamicSetTextViewGravity();
                }
            } catch (IndexOutOfBoundsException e) {
                showSnackBar("图片不存在");
                toast("图片不存在");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void dynamicSetTextViewGravity() {
        if ((tvContent.getPaint().measureText(tvContent.getText().toString()) < tvContentWidth)) {
            tvContent.setGravity(Gravity.CENTER);
            tvContent.setRichText(StringUtils.replaceBlank(tvContent.getText().toString()));
        } else {
            tvContent.setGravity(Gravity.TOP | Gravity.START);
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
