package com.icenler.lib.view.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;

import com.icenler.lib.R;

/**
 * Created by iCenler - 2015/9/13：
 * Description：
 */
public class CustonPromptView extends DialogFragment {

    public static DialogFragment createDialog() {
        return createDialog(null);
    }

    public static DialogFragment createDialog(Bundle bundle) {
        DialogFragment mDialog = new CustonPromptView();
        mDialog.setArguments(bundle);
        return mDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog mDialog = new Dialog(getActivity(), R.style.BaseDialog);

        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(false);
        // TODO 布局待完善添加
        mDialog.setContentView(0);
        mDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

        return mDialog;
    }

}
