package com.shenhua.openeyesreading.adapter.ihistory;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shenhua.openeyesreading.R;
import com.shenhua.openeyesreading.bean.IHistoryClickRank;
import com.shenhua.openeyesreading.callback.OnRecyclerItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 点击排行
 * Created by shenhua on 8/11/2016.
 */
public class ClickRankAdapter extends RecyclerView.Adapter<ClickRankAdapter.ViewHolder> {

    private Context context;
    private List<IHistoryClickRank> datas;
    private OnRecyclerItemClickListener mListener;

    public ClickRankAdapter(Context context, List<IHistoryClickRank> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_list_click_rank, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final IHistoryClickRank data = this.datas.get(position);
        if (data != null) {
            holder.titleTv.setText(data.getTitle());
            holder.numTv.setText(String.valueOf(position + 1));
            if (position == 0) {
                holder.numTv.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTopOne));
                holder.titleTv.setTextColor(ContextCompat.getColor(context, R.color.colorTopOne));
            }
            if (position == 1) {
                holder.numTv.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTopTwo));
                holder.titleTv.setTextColor(ContextCompat.getColor(context, R.color.colorTopTwo));
            }
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
        @Bind(R.id.tv_title)
        TextView titleTv;
        @Bind(R.id.tv_num)
        TextView numTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener listener) {
        this.mListener = listener;
    }

}
