package com.icenler.lib.feature.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icenler.lib.feature.App;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by iCenler - 2015/7/23：
 * Description：Fragment 基类
 * 0、 实现 getFragmentLayout 初始化布局
 * 1、 通过重写 onViewCreated 方法初始化 Fragment
 */
public abstract class BaseFragment extends Fragment {

    protected View mRootView;

    @LayoutRes
    protected abstract int doGetLayoutResId();

    protected abstract void doInit(View root);

    private Unbinder mUnbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(doGetLayoutResId(), container, false);

            mUnbinder = ButterKnife.bind(this, mRootView);

            doInit(mRootView);
        }

        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mUnbinder.unbind();

        App.getRefWatcher().watch(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
