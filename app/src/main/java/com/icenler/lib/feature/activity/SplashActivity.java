package com.icenler.lib.feature.activity;

import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.icenler.lib.R;
import com.icenler.lib.feature.base.BaseActivity;

import butterknife.BindView;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.iv_splash_picture)
    ImageView mSplashPictureIV;

    @Override
    protected int doGetLayoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void doInit() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        startAnim();
    }

    private void startAnim() {
        ViewCompat.animate(mSplashPictureIV)
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setDuration(2500)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        super.onAnimationEnd(view);
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivityAfterFinish(new Intent(SplashActivity.this, MainActivity.class));
                            }
                        }, 500);
                    }
                })
                .start();
//        Animator animation = AnimatorInflater.loadAnimator(this, R.animator.anim_welcome_splash);
//        animation.setTarget(mSplashPictureIV);
//        animation.setDuration(2500);
//        animation.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                mSplashPictureIV.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        startActivityAfterFinish(new Intent(SplashActivity.this, MainActivity.class));
//
//                    }
//                }, 500);
//            }
//        });
//        animation.start();
    }
}
