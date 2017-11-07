package com.shenhua.openeyesreading.frag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shenhua.openeyesreading.R;
import com.shenhua.openeyesreading.activity.HistoryDetailActivity;
import com.shenhua.openeyesreading.adapter.ihistory.ClickRankAdapter;
import com.shenhua.openeyesreading.adapter.ihistory.DailyPicksAdapter;
import com.shenhua.openeyesreading.adapter.ihistory.HistoryNewsAdapter;
import com.shenhua.openeyesreading.adapter.ihistory.OldPhotoAdapter;
import com.shenhua.openeyesreading.bean.IHistoryBean;
import com.shenhua.openeyesreading.bean.IHistoryClickRank;
import com.shenhua.openeyesreading.bean.IHistoryDailyPicks;
import com.shenhua.openeyesreading.bean.IHistoryHistoryNews;
import com.shenhua.openeyesreading.bean.IHistoryOldPhoto;
import com.shenhua.openeyesreading.callback.OnRecyclerItemClickListener;
import com.shenhua.openeyesreading.core.HttpApiImpl;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 四块分类
 * Created by shenhua on 8/19/2016.
 */
public class ClassifyFragment extends Fragment {

    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private View view;

    public static ClassifyFragment newInstance(IHistoryBean bean, int position) {
        ClassifyFragment fragment = new ClassifyFragment();
        Bundle args = new Bundle();
        args.putSerializable("iHistory", bean);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.frag_ihistory_rev, container, false);
            ButterKnife.bind(this, view);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final int position = getArguments().getInt("position");
        final IHistoryBean bean = (IHistoryBean) getArguments().getSerializable("iHistory");
        if (bean == null) return;
        switch (position) {
            case 0:
                set01(bean);
                break;
            case 1:
                set02(bean);
                break;
            case 2:
                set03(bean);
                break;
            case 3:
                set04(bean);
                break;
        }
    }

    private void set04(IHistoryBean bean) {
        List<IHistoryHistoryNews> datas = HttpApiImpl.getInstance().takeHistoryNews(bean.getDocString());
        HistoryNewsAdapter adapter = new HistoryNewsAdapter(getContext(), datas);
        adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void itemClicked(int position, String title, String href) {
                toDetail(title, href);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
    }

    private void set03(IHistoryBean bean) {
        List<IHistoryClickRank> datas = HttpApiImpl.getInstance().takeClickRank(bean.getDocString());
        ClickRankAdapter adapter = new ClickRankAdapter(getContext(), datas);
        adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void itemClicked(int position, String title, String href) {
                toDetail(title, href);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
    }

    private void set02(IHistoryBean bean) {
        List<IHistoryOldPhoto> datas = HttpApiImpl.getInstance().takeProposeRead(bean.getDocString());
        OldPhotoAdapter adapter = new OldPhotoAdapter(getContext(), datas);
        adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void itemClicked(int position, String title, String href) {
                toDetail(title, href);
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setAdapter(adapter);
    }

    private void set01(IHistoryBean bean) {
        List<IHistoryDailyPicks> datas = HttpApiImpl.getInstance().takeDailyPick(bean.getDocString());
        DailyPicksAdapter adapter = new DailyPicksAdapter(getContext(), datas);
        adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {

            @Override
            public void itemClicked(int position, String title, String href) {

            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void itemClicked(int position, String title, String href) {
                toDetail(title, href);
            }
        });
    }

    private void toDetail(String title, String href) {
        startActivity(new Intent(getActivity(), HistoryDetailActivity.class)
                .putExtra("title", title)
                .putExtra("href", href));
    }
}
