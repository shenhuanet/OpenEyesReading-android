package com.shenhua.openeyesreading.adapter.ihistory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shenhua.openeyesreading.R;
import com.shenhua.openeyesreading.bean.IHistoryOldPhoto;
import com.shenhua.openeyesreading.callback.OnRecyclerItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 推荐阅读 OldPhotoAdapter
 * Created by shenhua on 8/11/2016.
 */
public class OldPhotoAdapter extends RecyclerView.Adapter<OldPhotoAdapter.ViewHolder> {

    private Context context;
    private List<IHistoryOldPhoto> datas;
    private OnRecyclerItemClickListener mListener;

    public OldPhotoAdapter(Context context, List<IHistoryOldPhoto> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_list_propose_read, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final IHistoryOldPhoto data = this.datas.get(position);
        if (data != null) {
            holder.titleTv.setText(data.getTitle());
            ImageLoader.getInstance().displayImage(data.getImgHref(), holder.picIv);
            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.itemClicked(position, data.getTitle(), data.getHref());
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

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.rootView)
        LinearLayout rootView;
        @Bind(R.id.iv_pic)
        ImageView picIv;
        @Bind(R.id.tv_title)
        TextView titleTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener listener) {
        this.mListener = listener;
    }

}
