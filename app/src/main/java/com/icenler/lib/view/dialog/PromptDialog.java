package com.icenler.lib.view.dialog;

import android.app.DialogFragment;
import android.app.FragmentManager;

/**
 * Created by iCenler - 2015/4/14.
 * Description：温馨提示窗口
 */
public class PromptDialog {

    private static DialogFragment mPromptDialog;

    public static DialogFragment getDialog() {
        return mPromptDialog;
    }

    /**
     * 显示加载框
     */
    public static void show(FragmentManager fm) {
        try {
            if (mPromptDialog == null || !mPromptDialog.isVisible()) {
                mPromptDialog = CustonPromptView.createDialog();
                mPromptDialog.show(fm, "Prompt_Dialog");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mPromptDialog = null;
        }
    }

    /**
     * 取消显示
     */
    public static void dismiss() {
        try {
            if (mPromptDialog != null) {
                mPromptDialog.dismiss();
            }
            mPromptDialog = null;
        } catch (Exception e) {
            e.printStackTrace();
            mPromptDialog = null;
        }
    }

}
