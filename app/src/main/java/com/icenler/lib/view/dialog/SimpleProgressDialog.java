package com.icenler.lib.view.dialog;

import android.app.DialogFragment;
import android.app.FragmentManager;

/**
 * Created by iCenler - 2015/9/13：
 * Description：简单加载 DialogFragment
 */
public class SimpleProgressDialog {

    private static DialogFragment mDialog;

    public static DialogFragment getDialog() {
        return mDialog;
    }

    /**
     * 显示加载框
     */
    public static void show(FragmentManager fm) {
        try {
            if (mDialog == null || !mDialog.isVisible()) {
                mDialog = CustomLoadingView.createDialog();
                mDialog.show(fm, "Simple_Loading_Dialog");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mDialog = null;
        }
    }

    /**
     * 取消显示
     */
    public static void dismiss() {
        try {
            if (mDialog != null) {
                mDialog.dismiss();
            }
            mDialog = null;
        } catch (Exception e) {
            e.printStackTrace();
            mDialog = null;
        }
    }

}
