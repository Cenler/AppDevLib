package com.icenler.lib.mvp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Cenler on 2015/11/27.
 * Description:
 */
public interface IDelegate {

    void create(LayoutInflater inflater, ViewGroup root, Bundle bundle);

    void initWidth();

    void setOnClickListener(View.OnClickListener listener, int... resIds);

    View getRootView();

}
