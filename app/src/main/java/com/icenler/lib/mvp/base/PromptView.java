package com.icenler.lib.mvp.base;

import android.view.View;

/**
 * Created by Fangde on 2016/3/5.
 * Description:
 */
public interface PromptView {

    void toast(String msg);

    void snackbar(View parent, String msg);

}
