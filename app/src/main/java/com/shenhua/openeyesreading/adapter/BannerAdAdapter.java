package com.shenhua.openeyesreading.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

/**
 * 轮播图广告适配器
 * Created by shenhua on 2016/5/17.
 */
public class BannerAdAdapter extends PagerAdapter implements View.OnClickListener {

    private Context mContext;
    private List<ImageView> imageViews;// 每张轮播图的imageview
    private List<String> list;// 每张轮播图的image url
    private int mAdCount = 1;// 轮播广告的数量
    private int ivPosition;// 保存imageview的位置

    public BannerAdAdapter(Context mContext, List<ImageView> imageViews, List<String> list) {
        this.mContext = mContext;
        this.imageViews = imageViews;
        this.list = list;
        if (imageViews != null && imageViews.size() > 0) {
            mAdCount = imageViews.size();
        }
    }

    @Override
    public int getCount() {
        if (mAdCount <= 0) return 0;
        if (mAdCount == 1) return 1;
        else return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final int newPosition = position % mAdCount;
        ivPosition = newPosition;
        // 先移除再添加，更新图片在container中的位置（把iv放至container末尾）
        final ImageView iv = imageViews.get(newPosition);
        container.removeView(iv);
        container.addView(iv);
        // 这里处理图片的跳转事件
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int realPosition = 0;
                switch (ivPosition) {
                    case 0:
                        realPosition = 2;
                        break;
                    case 1:
                        realPosition = 0;
                        break;
                    case 2:
                        realPosition = 1;
                        break;
                }
                System.out.println(realPosition + "---" + list.get(realPosition));
                Toast.makeText(mContext, list.get(realPosition), Toast.LENGTH_SHORT).show();
            }
        });
        return iv;
    }

    @Override
    public void onClick(View v) {

    }
}
