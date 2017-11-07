package com.shenhua.openeyesreading.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shenhua.openeyesreading.App;
import com.shenhua.openeyesreading.R;
import com.shenhua.openeyesreading.bean.BingImagesData;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 主页listview数据适配器
 * Created by shenhua on 2016/5/17.
 */
public class HomeDataAdapter extends BaseAdapter {

    private Context mContext;
    private List<BingImagesData> datas;
    private App app;
    private boolean isNoData;
    private int mHeight;

    public HomeDataAdapter(App app, Context context, List<BingImagesData> datas) {
        this.app = app;
        this.mContext = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // 暂无数据
//        if (isNoData) {
//            convertView = mInflater.inflate(R.layout.item_no_data_layout, null);
//            AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeight);
//            RelativeLayout rootView = ButterKnife.findById(convertView, R.id.rl_root_view);
//            rootView.setLayoutParams(params);
//            return convertView;
//        }
        // 正常数据
        final ViewHolder holder;
        if (convertView != null && convertView instanceof FrameLayout) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_content, (ViewGroup) convertView, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        BingImagesData data = datas.get(position);
        ImageLoader.getInstance().displayImage(data.getImgUrl(), holder.ivImg, App.getDefaultOptionsBuilder());
        holder.tvTitle.setText(data.getImgTitle());
        holder.tvSubTitle.setText(data.getImgSubTitle());
        holder.tvDate.setText(data.getImgDate());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, Integer.toString(position), Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.layout_item)
        LinearLayout item;
        @Bind(R.id.img_content)
        ImageView ivImg;
        @Bind(R.id.txt_title)
        TextView tvTitle;
        @Bind(R.id.txt_sub_title)
        TextView tvSubTitle;
        @Bind(R.id.txt_date)
        TextView tvDate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
