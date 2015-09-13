package com.icenler.lib.view.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.icenler.lib.R;
import com.icenler.lib.utils.helper.StringHelper;

/**
 * Created by iCenler - 2015/9/13：
 * Description：简单加载 DialogFragment
 */
public class SimpleProgressDialog {

    private static DialogFragment mDialoFragment;

    public static DialogFragment getDialog() {
        return mDialoFragment;
    }

    /**
     * 显示加载框
     */
    public static void show(FragmentManager fm) {
        try {
            if (StringHelper.isNull(mDialoFragment) || !mDialoFragment.isVisible()) {
                mDialoFragment = MyCustomDialog.createDialog();
                mDialoFragment.show(fm, "Simple_Dialog");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mDialoFragment = null;
        }
    }

    /**
     * 取消显示
     */
    public static void dismiss() {
        try {
            if (mDialoFragment != null) {
                mDialoFragment.dismiss();
            }
            mDialoFragment = null;
        } catch (Exception e) {
            e.printStackTrace();
            mDialoFragment = null;
        }
    }

    private static class MyCustomDialog extends DialogFragment {
        public static DialogFragment createDialog() {
            MyCustomDialog mDialog = new MyCustomDialog();
            mDialog.setArguments(null);// 可设置参数
            return mDialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog mDialog = new Dialog(getActivity(), R.style.SimpleDialog);

            mDialog.setCancelable(true);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setContentView(R.layout.simple_dialog_layout);
            mDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

            ImageView mLoadingView = (ImageView) mDialog.findViewById(R.id.dialog_loading_iv);
            // 方式一：Frame 动画
//            mLoadingView.setImageResource(R.drawable.frame_anim_dialog_loading);
//            AnimationDrawable anim = (AnimationDrawable) mLoadingView.getDrawable();
//            anim.start();
            // 方式二：Tween 动画（推荐：比较流畅）
            mLoadingView.setImageResource(R.mipmap.loading0);
            Animation mAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_dialog_loading_rotate);
            mAnim.setInterpolator(new LinearInterpolator());
            mLoadingView.startAnimation(mAnim);

            return mDialog;
        }
    }

}
