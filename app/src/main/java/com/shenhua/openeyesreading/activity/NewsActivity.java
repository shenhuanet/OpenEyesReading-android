package com.shenhua.openeyesreading.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shenhua.comlib.annotation.ActivityFragmentInject;
import com.shenhua.comlib.base.BaseActivity;
import com.shenhua.comlib.base.BaseRecyclerAdapter;
import com.shenhua.comlib.base.BaseRecyclerViewHolder;
import com.shenhua.comlib.base.BaseSpacesItemDecoration;
import com.shenhua.comlib.callback.OnItemClickListener;
import com.shenhua.comlib.util.MeasureUtils;
import com.shenhua.openeyesreading.App;
import com.shenhua.openeyesreading.R;
import com.shenhua.openeyesreading.bean.NewsData;
import com.shenhua.openeyesreading.bean.SinaPhotoDetail;
import com.shenhua.openeyesreading.core.DataLoadType;
import com.shenhua.openeyesreading.presenter.INewsListPresenterImpl;
import com.shenhua.openeyesreading.view.NewsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 新闻界面
 * Created by shenhua on 8/25/2016.
 */
@ActivityFragmentInject(contentViewId = R.layout.activity_news,
        toolbarId = R.id.toolbar, toolbarTitle = R.string.toolbar_title_news, toolbarHomeAsUp = true)
public class NewsActivity extends BaseActivity implements NewsView, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.swiperefreshlayout)
    SwipeRefreshLayout mSwipRefreshLayout;
    private SinaPhotoDetail mSinaPhotoDetail;
    //    private CircleLoadingView mCircleLoadingView;
    private INewsListPresenterImpl mPresenter;
    private BaseRecyclerAdapter<NewsData> adapter;
    private static final int STATE_MORE_LOADING = 1;
    private static final int STATE_MORE_LOADED = 2;
    private static final int STATE_ALL_LOADED = 3;
    private int mCurrentState = STATE_MORE_LOADED;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int[] mVisiblePositions;

    @Override
    protected void initView(BaseActivity baseActivity) {
        ButterKnife.bind(baseActivity);
        setToolbarTitle(R.id.toolbar_title);
        String id = getIntent().getStringExtra("id");
        String type = getIntent().getStringExtra("type");
        mSwipRefreshLayout.setColorSchemeResources(R.color.colorArticle, R.color.colorIHistory, R.color.colorMore);
        mSwipRefreshLayout.setOnRefreshListener(this);
        initListener();
        mPresenter = new INewsListPresenterImpl(this, id, type);
    }

    private void initListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mCurrentState == STATE_MORE_LOADED && RecyclerView.SCROLL_STATE_IDLE == newState && calculateRecyclerViewFirstPosition() == mRecyclerView.getAdapter().getItemCount() - 1 && mOnLoadMoreListener != null) {
                    mOnLoadMoreListener.loadMore();
                    mCurrentState = STATE_MORE_LOADING;
                }
            }
        });
        mOnLoadMoreListener = new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                mPresenter.loadMoreDatas();
//                adapter.showFooter();
                mRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
        };
    }

    private boolean isAllLoaded() {
        return mCurrentState == STATE_ALL_LOADED;
    }

    private boolean isMoreLoaded() {
        return mCurrentState == STATE_MORE_LOADED;
    }

    public void notifyMoreLoaded() {
        mCurrentState = STATE_MORE_LOADED;
    }

    public void notifyAllLoaded() {
        mCurrentState = STATE_ALL_LOADED;
    }

    @Override
    public void updateNewsList(List<NewsData> datas, @DataLoadType.DataLoadTypeChecker int type) {
        switch (type) {
            case DataLoadType.TYPE_REFRESH_SUCCESS:
                if (adapter == null) initNewsList(datas);
                else adapter.setDatas(datas);
                if (isAllLoaded()) notifyAllLoaded();
                break;
            case DataLoadType.TYPE_REFRESH_FAIL:
                showSnackBar("TYPE_REFRESH_FAIL 加载失败");
                break;
            case DataLoadType.TYPE_LOAD_MORE_SUCCESS:
                if (datas == null || datas.size() == 0) {
                    // 全部加载完成
                    notifyAllLoaded();
                } else {
                    adapter.addMoreItem(datas);
                    notifyMoreLoaded();
                }
                break;
            case DataLoadType.TYPE_LOAD_MORE_FAIL:
                notifyMoreLoaded();
                break;
        }
    }

    private void initNewsList(final List<NewsData> datas) {
        final LinearLayoutManager lym = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new BaseRecyclerAdapter<NewsData>(this, datas) {
            @Override
            public int getItemViewId() {
                return R.layout.item_news_summary;
            }

            @Override
            public void bindData(BaseRecyclerViewHolder holder, int position, NewsData item) {
                TextView tvTitle = (TextView) holder.getView(R.id.tv_news_summary_title);
                TextView tvDigest = (TextView) holder.getView(R.id.tv_news_summary_digest);
                TextView tvPtime = (TextView) holder.getView(R.id.tv_news_summary_ptime);
                ImageView imageView = (ImageView) holder.getView(R.id.iv_news_summary_photo);
                tvTitle.setText(item.title);
                tvDigest.setText(item.digest);
                tvPtime.setText(item.ptime);
                ImageLoader.getInstance().displayImage(item.imgsrc, imageView, App.getDefaultOptionsBuilder());
            }
        };
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                view = view.findViewById(R.id.iv_news_summary_photo);
                if (adapter.getDatas().get(position).postid == null) {
                    toast("此文章已被移除");
                    return;
                }
                if (!TextUtils.isEmpty(adapter.getDatas().get(position).digest)) {
//                    Intent intent = new Intent(this, NewsDetailActivity.class);
//                    intent.putExtra("postid", adapter.getDatas().get(position).postid);
//                    intent.putExtra("imgsrc", adapter.getDatas().get(position).imgsrc);
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
//                                NewsActivity.this, view.findViewById(R.id.iv_news_summary_photo), "photos");
//                        NewsActivity.this.startActivity(intent, options.toBundle());
//                    } else {
//                        // 让新的Activity从一个小的范围扩大到全屏
//                        ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(
//                                view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
//                        ActivityCompat.startActivity(NewsActivity.this, intent, options.toBundle());
//                    }
                } else {
                    mSinaPhotoDetail = new SinaPhotoDetail();
                    mSinaPhotoDetail.data = new SinaPhotoDetail.SinaPhotoDetailDataEntity();
                    mSinaPhotoDetail.data.title = datas.get(position).title;
                    mSinaPhotoDetail.data.content = "";
                    mSinaPhotoDetail.data.pics = new ArrayList<>();
                    if (datas.get(position).ads != null) {
                        for (NewsData.AdsEntity entity : datas.get(position).ads) {
                            SinaPhotoDetail.SinaPhotoDetailPicsEntity sinaPicsEntity = new SinaPhotoDetail.SinaPhotoDetailPicsEntity();
                            sinaPicsEntity.pic = entity.imgsrc;
                            sinaPicsEntity.alt = entity.title;
                            sinaPicsEntity.kpic = entity.imgsrc;
                            mSinaPhotoDetail.data.pics.add(sinaPicsEntity);
                        }
                    } else if (datas.get(position).imgextra != null) {
                        for (NewsData.ImgextraEntity entity : datas.get(position).imgextra) {
                            SinaPhotoDetail.SinaPhotoDetailPicsEntity sinaPicsEntity = new SinaPhotoDetail.SinaPhotoDetailPicsEntity();
                            sinaPicsEntity.pic = entity.imgsrc;
                            sinaPicsEntity.kpic = entity.imgsrc;
                            mSinaPhotoDetail.data.pics.add(sinaPicsEntity);
                        }
                    }
                    Intent intent = new Intent(NewsActivity.this, SinaPhotoDetailActivity.class);
                    intent.putExtra("net", mSinaPhotoDetail);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
                    ActivityCompat.startActivity(NewsActivity.this, intent, options.toBundle());
                }
            }
        });
        mRecyclerView.setLayoutManager(lym);
        mRecyclerView.addItemDecoration(new BaseSpacesItemDecoration(MeasureUtils.dp2px(this, 4), false));
        mRecyclerView.setAdapter(adapter);
    }

    private int calculateRecyclerViewFirstPosition() {
        RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
        if (manager instanceof StaggeredGridLayoutManager) {
            if (mVisiblePositions == null)
                mVisiblePositions = new int[((StaggeredGridLayoutManager) manager).getSpanCount()];
            ((StaggeredGridLayoutManager) manager).findLastCompletelyVisibleItemPositions(mVisiblePositions);
            int max = -1;
            for (int pos : mVisiblePositions) {
                max = Math.max(max, pos);
            }
            return max;
        } else if (manager instanceof GridLayoutManager) {
            return ((GridLayoutManager) manager).findFirstCompletelyVisibleItemPosition();
        } else {
            return ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();
        }
    }

    @Override
    public void toast(String msg) {

    }

    @Override
    public void showProgress() {
//        mCircleLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
//        mCircleLoadingView.setVisibility(View.GONE);
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

    public interface OnLoadMoreListener {
        void loadMore();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.refreshDatas();
                mSwipRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }
}
