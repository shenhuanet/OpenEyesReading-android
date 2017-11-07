package com.shenhua.openeyesreading.widget;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shenhua.comlib.util.MeasureUtils;
import com.shenhua.openeyesreading.App;
import com.shenhua.openeyesreading.R;
import com.shenhua.openeyesreading.adapter.BannerAdAdapter;
import com.shenhua.openeyesreading.util.BannerViewInterface;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 轮播图视图
 */
public class BannerView extends BannerViewInterface<List<String>> {

    @Bind(R.id.vp_ad)
    ViewPager mViewPager;
    @Bind(R.id.ll_index_container)
    LinearLayout llIndexContainer;

    private static final int TYPE_CHANGE_AD = 0;
    private static final int CHANGE_DURTION = 3000;// banner切换速度
    private Thread mThread;
    private List<ImageView> ivLists;
    private boolean isStopThread = false;// 是否停止线程
    private App app;

    public BannerView(Activity context) {
        super(context);
        ivLists = new ArrayList<>();
        app = (App) context.getApplication();
    }

    public void setupView(List<String> list) {
        ivLists.clear();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ivLists.add(createImageView(list.get(i)));
        }
        BannerAdAdapter photoAdapter = new BannerAdAdapter(mContext, ivLists, list);
//        mViewPager.setPageTransformer(true, new DepthPage());
//        changeViewPagerScroller(mViewPager);
        mViewPager.setAdapter(photoAdapter);
        addIndicatorImageViews(size);
        setViewPagerChangeListener(size);
        startADRotate();
    }

    /**
     * 通过反射改变banner的切换速度
     */
    private void changeViewPagerScroller(ViewPager mViewPager) {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            //用自定义的类替换mScroller
            FixedSpeedScroller fixedSpeedScroller = new FixedSpeedScroller(mViewPager.getContext(), new AccelerateInterpolator(), 600);
            mField.set(mViewPager, fixedSpeedScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建要显示的ImageView
     */
    private ImageView createImageView(String url) {
        ImageView imageView = new ImageView(mContext);
        // AbsListView 用于实现条目的虚拟列表的基类. 这里的列表没有空间的定义。 例如，该类的子类可以以网格的形式、走马灯的形式显示，或者作为堆栈等等。
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // 加载图片
        ImageLoader.getInstance().displayImage(url, imageView, App.getDefaultOptionsBuilder());
        return imageView;
    }

    /**
     * 增加banner dots 指示器
     *
     * @param size
     */
    private void addIndicatorImageViews(int size) {
        llIndexContainer.removeAllViews();
        for (int i = 0; i < size; i++) {
            ImageView iv = new ImageView(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(MeasureUtils.dp2px(mContext, 5), MeasureUtils.dp2px(mContext, 5));
            if (i != 0) {
                lp.leftMargin = MeasureUtils.dp2px(mContext, 7);
            }
            iv.setLayoutParams(lp);
            iv.setBackgroundResource(R.drawable.banner_dot_bg);
            iv.setEnabled(false);
            if (i == 0) {
                iv.setEnabled(true);
            }
            llIndexContainer.addView(iv);
        }
    }

    /**
     * 为ViewPager设置监听器
     *
     * @param size
     */
    private void setViewPagerChangeListener(final int size) {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (ivLists != null && ivLists.size() > 0) {
                    int newPosition = position % size;
                    for (int i = 0; i < size; i++) {
                        llIndexContainer.getChildAt(i).setEnabled(false);
                        if (i == newPosition) {
                            llIndexContainer.getChildAt(i).setEnabled(true);
                        }
                    }
                }
            }

            @Override
            public void onPageScrolled(int position, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    /**
     * 启动轮播广告的线程
     */
    private void startADRotate() {
        if (ivLists == null || ivLists.size() <= 1) {// 当广告数量为一个时不进行轮播
            return;
        }
        if (mThread == null) {
            mThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    while (!isStopThread) {// 当没离开该页面时就一直轮播
                        SystemClock.sleep(CHANGE_DURTION);
                        mHandler.sendEmptyMessage(TYPE_CHANGE_AD);// 在主线程更新界面
                    }
                }
            });
            mThread.start();
        }
    }

    /**
     * 停止轮播广告的线程，清空消息队列
     */
    public void stopADRotate() {
        isStopThread = true;
        if (mHandler != null && mHandler.hasMessages(TYPE_CHANGE_AD)) {
            mHandler.removeMessages(TYPE_CHANGE_AD);
        }
    }

    /**
     * 轮播线程
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == TYPE_CHANGE_AD) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            }
        }
    };

    @Override
    protected void getView(List<String> list, ListView listView) {
        View view = mInflate.inflate(R.layout.view_banner, listView, false);
        ButterKnife.bind(this, view);
        setupView(list);
        listView.addHeaderView(view);
    }

}
