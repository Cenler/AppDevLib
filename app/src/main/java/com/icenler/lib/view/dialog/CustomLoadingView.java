package com.icenler.lib.view.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.icenler.lib.R;

/**
 * Created by iCenler - 2015/9/13：
 * Description：
 */
public class CustomLoadingView extends DialogFragment {

    public static DialogFragment createDialog() {
        DialogFragment mDialog = new CustomLoadingView();
        mDialog.setArguments(null);// 可设置参数
        return mDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog mDialog = new Dialog(getActivity(), R.style.BaseDialog);

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