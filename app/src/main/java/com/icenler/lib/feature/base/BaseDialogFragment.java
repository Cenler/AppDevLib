package com.icenler.lib.feature.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.icenler.lib.R;

/**
 * Description: 应用弹窗组件基类, 通过继承使用, 并重写特定方法进行如下配置
 * 1. 应用主题 getDialogTheme()
 * 2. 是否自动隐藏, 默认 Yes isCancelable()
 * 3. 是否可以返回隐藏, 默认 Yes isCanceledOnTouchOutside()
 * 4. 窗口位置设置, 默认 居中 getGravity() (ref: Gravity)
 * 5. 窗口类型  (ref: WindowManager.LayoutParams.TYPE_xxx)
 * 6. 窗口宽度  默认 wrap_content
 * 7. 窗口高度  默认 wrap_content
 */
public abstract class BaseDialogFragment extends DialogFragment {

    protected abstract int doGetContentViewId();

    protected abstract void doInit(View root);

    protected void doInitData() {
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDialogDismissListener) {
        this.mDismissListener = onDialogDismissListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getDialogTheme());

        View root = LayoutInflater.from(getActivity()).inflate(doGetContentViewId(), null);

        dialog.setCancelable(isCancelable());
        dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside());
        dialog.setContentView(root);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();

        layoutParams.width = getDefaultWidth();
        layoutParams.height = getDefaultHeight();

        window.setGravity(getGravity());
        window.setAttributes(layoutParams);
        window.setType(getWindowType());
        // window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        doInit(root);

        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        doInitData();
    }

    /**
     * 弹窗主题
     */
    protected int getDialogTheme() {
        return R.style.BaseDialog;
    }

    /**
     * 窗口外点击是否隐藏
     */
    protected boolean isCanceledOnTouchOutside() {
        return true;
    }

    /**
     * 窗口位置
     */
    protected int getGravity() {
        return Gravity.CENTER;
    }

    /**
     * 窗口类型
     */
    protected int getWindowType() {
        return WindowManager.LayoutParams.TYPE_APPLICATION;
    }

    /**
     * 窗口宽度
     */
    protected int getDefaultWidth() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    /**
     * 窗口高度
     */
    protected int getDefaultHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    private DialogInterface.OnDismissListener mDismissListener;

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mDismissListener != null)
            mDismissListener.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (mDismissListener != null)
            mDismissListener.onDismiss(dialog);
    }

}
