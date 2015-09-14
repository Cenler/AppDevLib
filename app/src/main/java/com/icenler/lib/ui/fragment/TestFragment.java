package com.icenler.lib.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icenler.lib.R;

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
    private List mData;

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
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_blue);
        mAdapter = new CardItemAdapter(getActivity(), mData);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

        }
    };

    private class CardItemAdapter extends RecyclerView.Adapter<CardViewHolder> {

        private Context mContext;
        private List mData;

        public CardItemAdapter(Context context, List data) {
            mContext = context;
            mData = data;
        }

        @Override
        public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CardViewHolder(LayoutInflater.from(mContext).inflate(R.layout.content_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(CardViewHolder holder, int position) {
            mData.get(position);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    private class CardViewHolder extends RecyclerView.ViewHolder {

        public CardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }
    }
}
