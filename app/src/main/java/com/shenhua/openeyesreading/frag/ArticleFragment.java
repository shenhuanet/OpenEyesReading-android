package com.shenhua.openeyesreading.frag;

import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shenhua.comlib.AppUtils;
import com.shenhua.comlib.annotation.ActivityFragmentInject;
import com.shenhua.comlib.base.BaseFragment;
import com.shenhua.openeyesreading.R;
import com.shenhua.openeyesreading.bean.ArticleData;
import com.shenhua.openeyesreading.core.HttpApiCallback;
import com.shenhua.openeyesreading.core.HttpApiImpl;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS;
import static android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL;
import static com.shenhua.openeyesreading.R.id.toolbar;

/**
 * 每日文章阅读
 * Created by Shenhua on 5/13/2016.
 */
@ActivityFragmentInject(contentViewId = R.layout.frag_article, toolbarId = toolbar,
        toolbarTitle = R.string.toolbar_title_null, hasOptionsMenu = true, menuId = R.menu.menu_one)
public class ArticleFragment extends BaseFragment {

    @Bind(R.id.appbar)
    AppBarLayout mAppbar;
    @Bind(R.id.iv_home_bg)
    ImageView mBgIv;
    @Bind(R.id.layout_null)
    View mNullView;
    @Bind(R.id.layout_content)
    NestedScrollView mContentView;
    @Bind(R.id.webview)
    WebView webview;
    private AppBarLayout.LayoutParams mAppbarParams;
    private String imgUrl;

    @Override
    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        mContentView.setVisibility(View.GONE);
        mNullView.setVisibility(View.VISIBLE);
//        initDatas(AppUtils.Constants.URL_ARTICLE);
        mAppbarParams = (AppBarLayout.LayoutParams) mAppbar.getChildAt(0).getLayoutParams();
        updateToolbarScroll(false);
    }

    private void initDatas(String p) {
//        HttpApiImpl.getInstance().toGetBingImgs("js", "0", "12", new HttpApiCallback() {
//            @Override
//            public void onSuccess(Object o) {
//
//            }
//
//            @Override
//            public void onFailed(int errorCode, String msg) {
//
//            }
//        });
        HttpApiImpl.getInstance().toGetArticle(p, new HttpApiCallback() {
            @Override
            public void onSuccess(Object o) {
                ArticleData data = (ArticleData) o;
                String content = data.getContent();
                setToolbarTitle(data.getTitle(), R.id.toolbar_title);
                ImageLoader.getInstance().displayImage(data.getImgUrl(), mBgIv);
                imgUrl = data.getImgUrl();
                mContentView.setVisibility(View.VISIBLE);
                mNullView.setVisibility(View.GONE);
                webview.setBackgroundColor(Color.parseColor("#50ffffff")); // 设置背景色
                webview.clearCache(false);
                webview.getSettings().setDefaultTextEncodingName("UTF-8");
                webview.loadData(content, "text/html; charset=UTF-8", null);
                updateToolbarScroll(true);
            }

            @Override
            public void onFailed(int errorCode, String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                updateToolbarScroll(false);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_img:
                showSnackBar(imgUrl);
                break;
            case R.id.menu_random:
                mContentView.scrollTo(0, 0);
                webview.scrollTo(0, 0);
                initDatas(AppUtils.Constants.URL_ARTICLE_RANDOM);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 判断是否有文章，如果有则toolbar可滚动，否则不可滚动。
     *
     * @param hasArticle true有文章
     */
    private void updateToolbarScroll(boolean hasArticle) {
        mAppbarParams.setScrollFlags(hasArticle ? (SCROLL_FLAG_SCROLL | SCROLL_FLAG_ENTER_ALWAYS)
                : SCROLL_FLAG_ENTER_ALWAYS);
    }
}
