package com.shenhua.comlib.base;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * RecyclerView item space
 * Created by Shenhua on 8/21/2016.
 */
public class BaseSpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;// 空间间隔大小
    private boolean mIncludeEdge;// 是否包括边沿

    public BaseSpacesItemDecoration(int space, boolean includeEdge) {
        this.mSpace = space;
        this.mIncludeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        int spanCount;
        if (manager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) manager).getSpanCount();
            setStaggeredGridLayoutItemDecoration(outRect, position, spanCount);
        } else if (manager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) manager).getSpanCount();
            setGridLayoutIemDecoration(outRect, position, spanCount);
        } else if (manager instanceof LinearLayoutManager) {
            setLinearLayoutItemDecoration(outRect, position);
        }
    }

    private void setStaggeredGridLayoutItemDecoration(Rect outRect, int position, int spanCount) {
        outRect.left = 0;
        outRect.right = 0;
        outRect.top = 0;
        outRect.bottom = 0;
        if (position < spanCount)
            outRect.top = mSpace;
    }

    private void setLinearLayoutItemDecoration(Rect outRect, int position) {
        outRect.top = 0;
        outRect.bottom = 0;
        if (mIncludeEdge) {
            if (position == 0) outRect.top = mSpace;
            outRect.bottom = mSpace;
            outRect.left = mSpace;
            outRect.right = mSpace;
        } else {
            if (position != 0) outRect.top = mSpace;
        }
    }

    /**
     * 这里有一个bug 方形view时由于左边右边有间距，导致整个view变小 此情况发生在spanCount大于2的时候
     *
     * @param outRect
     * @param position
     * @param spanCount
     */
    private void setGridLayoutIemDecoration(Rect outRect, int position, int spanCount) {
        outRect.left = mSpace / 2;
        outRect.right = mSpace / 2;
        if (position < spanCount) outRect.top = 0;
        if (mIncludeEdge) {
            outRect.bottom = mSpace;
            if (position < spanCount) outRect.top = mSpace;// 第一行顶部
            if (position % spanCount == 0) //最左边
                outRect.left = mSpace;
            if (position % spanCount == spanCount - 1) //最右边
                outRect.right = mSpace;
        } else { // 不需要边沿
            outRect.top = mSpace;
            outRect.bottom = 0;
            if (position % spanCount == 0) //最左边
                outRect.left = 0;
            if (position % spanCount == spanCount - 1) //最右边
                outRect.right = 0;
        }
    }
}
