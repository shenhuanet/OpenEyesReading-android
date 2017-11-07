package com.shenhua.comlib.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * RecyclerView通用适配器Holder
 * Created by Shenhua on 8/21/2016.
 */
public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    //集合类，layout里包含的View,以view的id作为key，value是view对象
    protected SparseArray<View> mViews;
    protected Context mContext;

    public BaseRecyclerViewHolder(Context context,View itemView) {
        super(itemView);
        mContext=context;
        mViews = new SparseArray<>();
    }

    private <T extends View> T findViewById(int viewId){
        View view = mViews.get(viewId);
        if (view==null){
            view = itemView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T) view;
    }

    public View getView(int viewId){
        return findViewById(viewId);
    }

    public BaseRecyclerViewHolder setText(int viewId,String value){
        TextView view = findViewById(viewId);
        view.setText(value);
        return this;
    }

    public BaseRecyclerViewHolder setBackground(int viewId,int resId){
        View view = findViewById(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    public BaseRecyclerViewHolder setOnItemClickListener(int viewId, View.OnClickListener listener){
        View view = findViewById(viewId);
        view.setOnClickListener(listener);
        return this;
    }
}
