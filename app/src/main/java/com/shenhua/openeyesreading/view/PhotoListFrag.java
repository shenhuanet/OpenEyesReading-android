package com.shenhua.openeyesreading.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shenhua.comlib.annotation.ActivityFragmentInject;
import com.shenhua.comlib.base.BaseFragment;
import com.shenhua.comlib.base.BaseRecyclerAdapter;
import com.shenhua.comlib.base.BaseRecyclerViewHolder;
import com.shenhua.comlib.base.BaseSpacesItemDecoration;
import com.shenhua.comlib.callback.OnItemClickListener;
import com.shenhua.comlib.util.MeasureUtils;
import com.shenhua.comlib.widget.CircleLoadingView;
import com.shenhua.openeyesreading.App;
import com.shenhua.openeyesreading.R;
import com.shenhua.openeyesreading.activity.SinaPhotoDetailActivity;
import com.shenhua.openeyesreading.bean.SinaPhotosList;
import com.shenhua.openeyesreading.core.DataLoadType;
import com.shenhua.openeyesreading.presenter.IPhotoListPresenter;
import com.shenhua.openeyesreading.presenter.IPhotoListPresenterImpl;

import java.util.List;
import java.util.Random;

/**
 * 新浪图片frag
 * Created by shenhua on 8/23/2016.
 */
@ActivityFragmentInject(contentViewId = R.layout.frag_photo_content)
public class PhotoListFrag extends BaseFragment implements PhotoList, SwipeRefreshLayout.OnRefreshListener {

    protected static final String PHOTO_ID = "photo_id";
    protected static final String POSITION = "position";
    protected String mPhotoId;
    protected int mPosition;
    private BaseRecyclerAdapter<SinaPhotosList.DataEntity.PhotoListEntity> mAdapter;
    private SwipeRefreshLayout mSwipRefreshLayout;
    private RecyclerView mRecyclerView;
    private CircleLoadingView mCircleLoadingView;
    private IPhotoListPresenter mPresenter;
    private int[] mVisiblePositions;
    private static final int STATE_MORE_LOADING = 1;
    private static final int STATE_MORE_LOADED = 2;
    private static final int STATE_ALL_LOADED = 3;
    private int mCurrentState = STATE_MORE_LOADED;
    private OnLoadMoreListener mOnLoadMoreListener;

    public static PhotoListFrag newInstance(String id, int position) {
        PhotoListFrag frag = new PhotoListFrag();
        Bundle bundle = new Bundle();
        bundle.putString(PHOTO_ID, id);
        bundle.putInt(POSITION, position);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPhotoId = getArguments().getString(PHOTO_ID);
            mPosition = getArguments().getInt(POSITION);
        }
    }

    @Override
    public void initView(View rootView) {
        mOnLoadMoreListener = new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                mPresenter.loadMoreDatas();
//                mAdapter.showFooter();
                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        };
        mSwipRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefreshlayout);
        mSwipRefreshLayout.setColorSchemeResources(R.color.colorArticle, R.color.colorIHistory, R.color.colorMore);
        mSwipRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
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

        mCircleLoadingView = (CircleLoadingView) rootView.findViewById(R.id.circle_loading);
        mCircleLoadingView.setDrawableColor(R.color.colorAccent);
        mPresenter = new IPhotoListPresenterImpl(this, mPhotoId, 1);
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
    public void updatePhotoList(List<SinaPhotosList.DataEntity.PhotoListEntity> data, @DataLoadType.DataLoadTypeChecker int type) {
        switch (type) {
            case DataLoadType.TYPE_REFRESH_SUCCESS:
                if (mAdapter == null) initNewList(data);
                else mAdapter.setDatas(data);
                if (isAllLoaded()) notifyAllLoaded();
                break;
            case DataLoadType.TYPE_REFRESH_FAIL:
                // 加载失败
                showSnackBar("TYPE_REFRESH_FAIL 加载失败");
                break;
            case DataLoadType.TYPE_LOAD_MORE_FAIL:
                notifyMoreLoaded();
                break;
            case DataLoadType.TYPE_LOAD_MORE_SUCCESS:
                if (data == null || data.size() == 0) {
                    // 全部加载完成
                    notifyAllLoaded();
                } else {
                    mAdapter.addMoreItem(data);
                    notifyMoreLoaded();
                }
                break;
        }
    }

    private void initNewList(List<SinaPhotosList.DataEntity.PhotoListEntity> data) {
        final StaggeredGridLayoutManager sta = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new BaseRecyclerAdapter<SinaPhotosList.DataEntity.PhotoListEntity>(getActivity(), data) {

            Random random = new Random();

            @Override
            public int getItemViewId() {
                return R.layout.item_photo_summary;
            }

            @Override
            public void bindData(BaseRecyclerViewHolder holder, int position, SinaPhotosList.DataEntity.PhotoListEntity item) {
                final ImageView imageView = (ImageView) holder.getView(R.id.iv_photo_summary);
                final TextView textView = (TextView) holder.getView(R.id.tv_photo_summary);
                final ViewGroup.LayoutParams params = imageView.getLayoutParams();
                if (item.picWidth == -1 && item.picHeight == -1) {
                    item.picWidth = MeasureUtils.getScreenSize(getActivity()).x / 2 - MeasureUtils.dp2px(getActivity(), 4) * 2 - MeasureUtils.dp2px(getActivity(), 2);
                    item.picHeight = (int) (item.picWidth * (random.nextFloat() / 2 + 1));
                }
                params.width = item.picWidth;
                params.height = item.picHeight;
                imageView.setLayoutParams(params);
                textView.setText(item.title);
                ImageLoader.getInstance().displayImage(item.kpic, imageView, App.getDefaultOptionsBuilder());
            }
        };
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), SinaPhotoDetailActivity.class);
                intent.putExtra("photoId", mAdapter.getDatas().get(position).id);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
                ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
            }
        });
        mRecyclerView.setLayoutManager(sta);
        mRecyclerView.addItemDecoration(new BaseSpacesItemDecoration(MeasureUtils.dp2px(getActivity(), 4), false));
        mRecyclerView.setAdapter(mAdapter);
    }

    public interface OnLoadMoreListener {
        void loadMore();
    }

    @Override
    public void toast(String msg) {

    }

    @Override
    public void showProgress() {
        mCircleLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mCircleLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) mPresenter.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.onDestroy();
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
