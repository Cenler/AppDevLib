package com.icenler.lib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by iCenler - 2015/7/23：
 * Description：Fragment 基类
 * 0、 实现 getFragmentLayout 初始化布局
 * 1、 通过重写 onViewCreated 方法初始化 Fragment
 */
public class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BaseApplication.getRefWatcher().watch(this);
    }

}
