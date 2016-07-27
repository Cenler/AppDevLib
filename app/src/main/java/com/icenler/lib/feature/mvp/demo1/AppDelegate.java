package com.icenler.lib.feature.mvp.demo1;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Cenler on 2015/11/27.
 * Description: MVP for View
 */
public abstract class AppDelegate implements IDelegate {

    protected abstract int getRootLayoutId();

    private View mRootView;
    private final SparseArray<View> mViews = new SparseArray<View>();

    @Override
    public void create(LayoutInflater inflater, ViewGroup root, Bundle bundle) {
        mRootView = inflater.inflate(getRootLayoutId(), root, false);
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    public <T extends View> T get(int resId) {
        return bindView(resId);
    }

    @Override
    public void initWidth() {
        // TODO　控件初始化
    }

    @Override
    public void setOnClickListener(View.OnClickListener listener, int... resIds) {
        if (listener == null || resIds == null) {
            return;
        }

        for (int id : resIds) {
            get(id).setOnClickListener(listener);
        }
    }


    private <T extends View> T bindView(int resId) {
        T view = (T) mViews.get(resId);
        if (view == null) {
            view = (T) getRootView().findViewById(resId);
            mViews.put(resId, view);
        }

        return view;
    }

}
