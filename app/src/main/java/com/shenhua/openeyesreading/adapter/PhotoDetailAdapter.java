package com.shenhua.openeyesreading.adapter;

import android.content.Context;
import android.graphics.RectF;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shenhua.comlib.util.MeasureUtils;
import com.shenhua.openeyesreading.App;
import com.shenhua.openeyesreading.R;
import com.shenhua.openeyesreading.bean.SinaPhotoDetail;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 照片详情viewpager适配器
 * Created by shenhua on 8/25/2016.
 */
public class PhotoDetailAdapter extends PagerAdapter {

    private final int mWidth;
    private List<SinaPhotoDetail.SinaPhotoDetailPicsEntity> mPics;
    private Context mContext;
    private OnPhotoExpandListener mOnPhotoExpandListenter;

    public PhotoDetailAdapter(Context mContext, List<SinaPhotoDetail.SinaPhotoDetailPicsEntity> pics) {
        mPics = pics == null ? new ArrayList<SinaPhotoDetail.SinaPhotoDetailPicsEntity>() : pics;
        this.mContext = mContext;
        mWidth = MeasureUtils.getScreenSize(mContext).x;
    }

    @Override
    public int getCount() {
        return mPics.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final PhotoView photoView = new PhotoView(mContext);
        final String kpic = mPics.get(position).kpic;
        if (kpic.contains("gif")) {
            ImageLoader.getInstance().displayImage(kpic, photoView, App.getDefaultOptionsBuilder());
            photoView.setZoomable(false);
        } else {
            ImageLoader.getInstance().displayImage(kpic, photoView, App.getDefaultOptionsBuilder());
            photoView.setZoomable(true);
        }
        photoView.setTag(R.id.img_tag, position);
        photoView.setOnMatrixChangeListener(new PhotoViewAttacher.OnMatrixChangedListener() {
            @Override
            public void onMatrixChanged(RectF rect) {
                if (mOnPhotoExpandListenter != null && rect.left < -20 && rect.right > mWidth + 20) {
                    mOnPhotoExpandListenter.onExpand(false, (Integer) photoView.getTag(R.id.img_tag));
                    mPics.get(position).showTitle = false;
                } else if (mOnPhotoExpandListenter != null && rect.left >= -20 && rect.right <= mWidth + 20) {
                    mOnPhotoExpandListenter.onExpand(true, (Integer) photoView.getTag(R.id.img_tag));
                    mPics.get(position).showTitle = true;
                }
            }
        });
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public List<SinaPhotoDetail.SinaPhotoDetailPicsEntity> getPics() {
        return mPics;
    }

    public void setOnPhotoExpandListenter(OnPhotoExpandListener listenter) {
        this.mOnPhotoExpandListenter = listenter;
    }

    public interface OnPhotoExpandListener {
        void onExpand(boolean show, int positon);
    }
}
