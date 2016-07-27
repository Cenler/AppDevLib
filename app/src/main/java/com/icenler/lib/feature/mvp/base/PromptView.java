package com.icenler.lib.feature.mvp.base;

import android.view.View;

public interface PromptView {

    void toast(String msg);

    void snackbar(View parent, String msg);

}
