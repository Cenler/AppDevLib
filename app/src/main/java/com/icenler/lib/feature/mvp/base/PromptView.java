package com.icenler.lib.feature.mvp.base;

import android.view.View;

public interface PromptView {

    void showToast(String msg);

    void showSnackbar(View parent, String msg);

}
