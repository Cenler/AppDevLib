package com.icenler.lib.view.guideview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.AnimRes;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

public class GuideEx implements View.OnClickListener {

    static final int DEF_OVERLAY_COLOR = 0xB2000000;

    private boolean mOutsideTouchable;

    private boolean mAutoDismiss;

    private int mEnterAnimationId;

    private int mExitAnimationId;

    private int mOverlayColor;

    private int mHookId;

    private List<Mask> mMaskList;

    private List<Hint> mHintList;

    private GuideView mMaskView;

    private OnSingleTapUpListener mOnSingleTapUpListener;

    private OnVisibilityChangedListener mOnVisibilityChangedListener;

    public static Builder builder() {
        return new Builder();
    }

    private GuideEx(Builder builder) {
        this.mEnterAnimationId = builder.enterAnim;
        this.mExitAnimationId = builder.exitAnim;
        this.mOverlayColor = builder.overlayColor;
        this.mOutsideTouchable = builder.outsideTouchable;
        this.mAutoDismiss = builder.autoDismiss;
        this.mMaskList = builder.maskEntities;
        this.mHintList = builder.hintEntities;
    }

    public void setCallback(OnVisibilityChangedListener listener) {
        mOnVisibilityChangedListener = listener;
    }

    public void setOnSingleTapUp(OnSingleTapUpListener listener) {
        mOnSingleTapUpListener = listener;
    }

    public void setHookId(int hookId) {
        mHookId = hookId;
    }

    /**
     * 创建好一个 GuideEx 实例后，使用该实例调用本函数遮罩才会显示
     */
    public void show(Activity activity) {
        if (mMaskView == null) {
            mMaskView = onCreateView(activity);
        }

        ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
        if (mMaskView.getParent() == null) {
            content.addView(mMaskView);
            if (mEnterAnimationId != -1) {
                Animation anim = AnimationUtils.loadAnimation(activity, mEnterAnimationId);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (mOnVisibilityChangedListener != null) {
                            mOnVisibilityChangedListener.onShow();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                mMaskView.startAnimation(anim);
            } else {
                if (mOnVisibilityChangedListener != null) {
                    mOnVisibilityChangedListener.onShow();
                }
            }
        }
    }

    /**
     * 隐藏该遮罩并回收资源相关
     */
    public void dismiss() {
        if (mMaskView == null) {
            return;
        }
        final ViewGroup vp = (ViewGroup) mMaskView.getParent();
        if (vp == null) {
            return;
        }
        if (mExitAnimationId != -1) {
            Context context = mMaskView.getContext();
            Animation anim = AnimationUtils.loadAnimation(context, mExitAnimationId);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    vp.removeView(mMaskView);
                    if (mOnVisibilityChangedListener != null) {
                        mOnVisibilityChangedListener.onDismiss();
                    }
                    onDestroy();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mMaskView.startAnimation(anim);
        } else {
            vp.removeView(mMaskView);
            if (mOnVisibilityChangedListener != null) {
                mOnVisibilityChangedListener.onDismiss();
            }
            onDestroy();
        }
    }

    private GuideView onCreateView(Activity activity) {
        ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
        // ViewGroup content = (ViewGroup) activity.getWindow().getDecorView();
        GuideView maskView = new GuideView(activity);
        maskView.setId(mHookId);// 钩子ID, 用于返回按钮屏蔽标记
        maskView.setOverlayColor(mOverlayColor);
        maskView.setMaskList(mMaskList);
        maskView.setHintList(mHintList);

        if (mOutsideTouchable) {
            maskView.setClickable(false);
        } else {
            maskView.setClickable(true);
            maskView.setOnClickListener(this);
        }

        return maskView;
    }

    private void onDestroy() {
        mOnVisibilityChangedListener = null;
        mMaskView.removeAllViews();
        mMaskView = null;
    }

    @Override
    public void onClick(View v) {
        if (mOnSingleTapUpListener != null) {
            mOnSingleTapUpListener.onSingleTapUp(mMaskView.getCurrentRawX(), mMaskView.getCurrentRawY());
        }

        if (mAutoDismiss) {
            dismiss();
        }
    }

    public interface OnVisibilityChangedListener {
        void onShow();

        void onDismiss();
    }

    public interface OnSingleTapUpListener {
        void onSingleTapUp(float rawX, float rawY);
    }

    public final static class Builder {

        private boolean outsideTouchable;

        private boolean autoDismiss;

        private int enterAnim = -1;

        private int exitAnim = -1;

        private int overlayColor = DEF_OVERLAY_COLOR;

        private final List<Mask> maskEntities = new ArrayList<>();

        private final List<Hint> hintEntities = new ArrayList<>();

        private Builder() {
        }

        public Builder setEnterAnimationId(@AnimRes int resId) {
            enterAnim = resId;
            return this;
        }

        public Builder setExitAnimationId(@AnimRes int resId) {
            exitAnim = resId;
            return this;
        }

        public Builder setOverlayColor(@ColorInt int color) {
            overlayColor = color;
            return this;
        }

        public Builder addMask(Mask mask) {
            maskEntities.add(mask);
            return this;
        }

        public Builder addHint(Hint hint) {
            hintEntities.add(hint);
            return this;
        }

        public Builder isOutsideTouchable(boolean enable) {
            outsideTouchable = enable;
            return this;
        }

        public Builder isAutoDismiss(boolean b) {
            autoDismiss = b;
            return this;
        }

        public GuideEx build() {
            return new GuideEx(this);
        }

    }
}