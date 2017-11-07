package com.shenhua.openeyesreading.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shenhua.comlib.widget.SquareLayout;
import com.shenhua.openeyesreading.App;
import com.shenhua.openeyesreading.R;
import com.shenhua.openeyesreading.bean.MoreData;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * DailyPicksAdapter
 * Created by shenhua on 8/11/2016.
 */
public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.ViewHolder> {

    private Context context;
    private List<MoreData> datas;
    private OnItemClickListener mListener;
    private Animation alpha;
    private Animation alpha2;

    public MoreAdapter(Context context, List<MoreData> datas) {
        this.context = context;
        this.datas = datas;
        alpha = AnimationUtils.loadAnimation(context, R.anim.anim_alpha);
        alpha2 = AnimationUtils.loadAnimation(context, R.anim.anim_alpha2);
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_list_more, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MoreData data = this.datas.get(position);
        if (data != null) {
            holder.titleTv.setText(data.getTitle());
            ImageLoader.getInstance().displayImage(data.getImg(), holder.picIv, App.getDefaultOptionsBuilder());
            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.itemClicked(position, data.getImg());
                }
            });
            holder.rootView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            holder.titleTv.startAnimation(alpha);
                            break;
                        case MotionEvent.ACTION_UP:
                            holder.titleTv.startAnimation(alpha2);
                            break;
                        case MotionEvent.ACTION_CANCEL:// Animation在listView中焦点移动到控件之外
                            holder.titleTv.startAnimation(alpha2);
                            break;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (datas == null || datas.size() == 0)
            return 0;
        return datas.size();
    }

    public void setDatas(List<MoreData> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.square_item)
        SquareLayout rootView;
        @Bind(R.id.iv_pic)
        ImageView picIv;
        @Bind(R.id.tv_title)
        TextView titleTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void itemClicked(int position, String href);
    }
}
