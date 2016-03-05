package com.icenler.lib.feature.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icenler.lib.R;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TestFragment extends Fragment {

    public static final String TITLE = "title";

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private String mTitle = "";

    public static Fragment newFragment(Bundle bundle) {
        Fragment fragment = new TestFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (null != bundle) {
            // 获取参数
            mTitle = bundle.getString(TITLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_list_layout, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mSwipeRefreshLayout.setOnRefreshListener(mRefreshListener);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_blue_assist);
        mAdapter = new CardItemAdapter(getActivity(), Arrays.asList(R.mipmap.img01, R.mipmap.img02, R.mipmap.img03));
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 2500);
        }
    };

    private class CardItemAdapter extends RecyclerView.Adapter<CardViewHolder> {

        private Context mContext;
        private List<Integer> mData;

        public CardItemAdapter(Context context, List<Integer> data) {
            mContext = context;
            mData = data;
        }

        @Override
        public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CardViewHolder(LayoutInflater.from(mContext).inflate(R.layout.content_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(CardViewHolder holder, int position) {
            holder.mCardLayout.setBackgroundResource(mData.get(position % 3));
        }

        @Override
        public int getItemCount() {
            return mData.size() * 10;
        }
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_ll)
        ViewGroup mCardLayout;

        public CardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
