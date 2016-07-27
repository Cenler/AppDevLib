package com.icenler.lib.feature.base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.icenler.lib.R;
import com.icenler.lib.utils.manager.SystemBarTintManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by iCenler - 2015/7/14.
 * Description：Activity 基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mUnbinder;

    @LayoutRes
    protected abstract int doGetLayoutResId();

    protected abstract void doInit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(doGetLayoutResId());
        mUnbinder = ButterKnife.bind(this);

        initSystemBar();

        doInit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /* extend api */
    protected void startActivityAfterFinish(Intent intent) {
        startActivityAfterFinish(intent, null);
    }

    protected void startActivityAfterFinish(Intent intent, @NonNull Bundle bundle) {
        startActivity(intent, bundle);
        finish();
    }

    /**
     * @return 默认状态栏颜色
     */
    @ColorRes
    protected int getDefaultStatusBarTintColor(){
        return R.color.color_status_bar_translucence;
    }

    /* 沉浸式状态栏设置 */
    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(getDefaultStatusBarTintColor());
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}
