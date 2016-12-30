package com.icenler.lib.view.guideview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;

import com.icenler.lib.utils.ScreenUtil;

import java.util.List;

/**
 * 设计思路:
 * 1. 分两层 (遮罩物层Mask\装饰物层Hint)
 * <p>
 * 2. 遮罩层 (目标区域\形状\半径\圆角\边框\Padding\ID)
 * <p>
 * 3. 装饰物层(目标遮罩层ID默认屏幕, 相对问题, 对齐方式, 相对位置X\Y, 偏移位置X\Y, )
 */
class GuideView extends AbsoluteLayout {

    private Paint mEraserPaint;
    private Paint mBorderPaint;
    private Bitmap mEraserBitmap;
    private Canvas mEraserCanvas;

    private int overlayColor;

    private List<Mask> maskEntities;

    private List<Hint> hintEntities;

    private float mCurrRawX, mCurrRawY;

    /**
     * 遮罩背景颜色
     */
    public void setOverlayColor(int color) {
        overlayColor = color;
    }

    /**
     * 遮罩物
     */
    public void setMaskList(List<Mask> maskEntities) {
        this.maskEntities = maskEntities;
        for (Mask mask : maskEntities) {
            Rect targetRect = mask.targetRect;

            targetRect.left -= mask.paddingLeft;
            targetRect.top -= mask.paddingTop;
            targetRect.right += mask.paddingRight;
            targetRect.bottom += mask.paddingBottom;

            targetRect.offset(mask.offsetX, mask.offsetY);
        }
    }

    /**
     * 装饰物
     */
    public void setHintList(List<Hint> hintEntities) {
        this.hintEntities = hintEntities;
        if (hintEntities != null) {
            removeAllViews();
            for (Hint hint : hintEntities) {
                addView(hint.hintView, new LayoutParams(hint.width, hint.height, 0, 0));
            }
        }
    }

    public GuideView(Context context) {
        this(context, null);
    }

    public GuideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWillNotDraw(false);

        initView();
    }

    private void initView() {
        int screenWidth = ScreenUtil.getDisplayWidth();
        int screenHeight = ScreenUtil.getDisplayHeight();

        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStyle(Paint.Style.STROKE);

        mEraserPaint = new Paint();
        mEraserPaint.setColor(Color.WHITE);
        mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mEraserPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        mEraserBitmap = Bitmap.createBitmap(screenWidth, screenHeight
                , Bitmap.Config.ARGB_8888);
        mEraserCanvas = new Canvas(mEraserBitmap);
    }

    private void orientationLayout(Hint hint) {
        LayoutParams lp =
                (LayoutParams) hint.hintView.getLayoutParams();
        lp.x = hint.absX;
        lp.y = hint.absY;
    }

    private void alignHintChild(Hint hint, Rect targetRect) {
        View child = hint.hintView;
        int measuredWidth = child.getMeasuredWidth();
        int measuredHeight = child.getMeasuredHeight();

        int centerX = targetRect.centerX();
        int centerY = targetRect.centerY();

        int left = targetRect.left;
        int top = targetRect.top;
        int right = targetRect.right;
        int bottom = targetRect.bottom;

        int width = targetRect.width();
        int height = targetRect.height();

        switch (hint.anchor) {
            case ANCHOR_LEFT:
                if (hint.gravity == Hint.Gravity.GRAVITY_CENTER) {
                    hint.absX = left - measuredWidth + hint.offsetX;
                    hint.absY = top + ((height - measuredHeight) / 2) + hint.offsetY;
                } else if (hint.gravity == Hint.Gravity.GRAVITY_END) {
                    hint.absX = left - measuredWidth + hint.offsetX;
                    hint.absY = bottom - measuredHeight + hint.offsetY;
                } else {
                    hint.absX = left - measuredWidth + hint.offsetX;
                    hint.absY = top + hint.offsetY;
                }
                break;
            case ANCHOR_TOP:
                if (hint.gravity == Hint.Gravity.GRAVITY_CENTER) {
                    hint.absX = left + ((width - measuredWidth) / 2) + hint.offsetX;
                    hint.absY = top - measuredHeight + hint.offsetY;
                } else if (hint.gravity == Hint.Gravity.GRAVITY_END) {
                    hint.absX = right - measuredWidth + hint.offsetX;
                    hint.absY = top - measuredHeight + hint.offsetY;
                } else {
                    hint.absX = left + hint.offsetX;
                    hint.absY = top - measuredHeight + hint.offsetY;
                }
                break;
            case ANCHOR_RIGHT:
                if (hint.gravity == Hint.Gravity.GRAVITY_CENTER) {
                    hint.absX = right + hint.offsetX;
                    hint.absY = top + ((measuredHeight - height) / 2) + hint.offsetY;
                } else if (hint.gravity == Hint.Gravity.GRAVITY_END) {
                    hint.absX = right + hint.offsetX;
                    hint.absY = bottom - measuredHeight + hint.offsetY;
                } else {
                    hint.absX = right + hint.offsetX;
                    hint.absY = top + hint.offsetY;
                }
                break;
            case ANCHOR_BOTTOM:
                if (hint.gravity == Hint.Gravity.GRAVITY_CENTER) {
                    hint.absX = left + (width - measuredWidth) / 2 + hint.offsetX;
                    hint.absY = bottom + hint.offsetY;
                } else if (hint.gravity == Hint.Gravity.GRAVITY_END) {
                    hint.absX = right - measuredWidth + hint.offsetX;
                    hint.absY = bottom + hint.offsetY;
                } else {
                    hint.absX = left + hint.offsetX;
                    hint.absY = bottom + hint.offsetY;
                }
                break;
            case ANCHOR_OVER:
            default:
                hint.absX = centerX - measuredWidth / 2 + hint.offsetX;
                hint.absY = centerY - measuredHeight / 2 + hint.offsetY;
                break;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (hintEntities != null && !hintEntities.isEmpty()) {
            for (Hint hint : hintEntities) {
                // 找到目标锚点, 进行相对定位
                if (hint.maskAnchorId != View.NO_ID) {
                    // 获取锚点ID
                    for (Mask mask : maskEntities) {
                        if (hint.maskAnchorId != mask.maskId)
                            continue;
                        alignHintChild(hint, mask.targetRect);
                        break;// 退出查找
                    }
                } else {
                    // 绝对定位
                    if (hint.absX == 0 && hint.absY == 0) {
                        Rect rect = new Rect();
                        getGlobalVisibleRect(rect);
                        alignHintChild(hint, rect);
                    } else {
                        hint.absX += hint.offsetX;
                        hint.absY += hint.offsetY;
                    }
                }

                orientationLayout(hint);
            }
        }

        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            clearFocus();
            mEraserCanvas.setBitmap(null);
            mEraserBitmap = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mEraserBitmap.eraseColor(Color.TRANSPARENT);
        mEraserCanvas.drawColor(overlayColor);

        for (Mask mask : maskEntities) {
            mBorderPaint.setColor(mask.borderColor);
            mBorderPaint.setStrokeWidth(mask.borderSize);

            Rect targetRect = mask.targetRect;
            if (mask.shapeStyle == Mask.Shape.RECTANGLE) {
                mEraserCanvas.drawRoundRect(new RectF(targetRect)
                        , mask.cornerSize
                        , mask.cornerSize
                        , mEraserPaint);
                mEraserCanvas.drawRoundRect(new RectF(targetRect)
                        , mask.cornerSize
                        , mask.cornerSize
                        , mBorderPaint);
            } else if (mask.shapeStyle == Mask.Shape.CIRCLE) {
                float centerX = targetRect.centerX();
                float centerY = targetRect.centerY();
                int radiusSize = mask.radiusSize != 0 ? mask.radiusSize : Math.min(targetRect.width(), targetRect.height());

                mEraserCanvas.drawCircle(centerX, centerY
                        , radiusSize, mEraserPaint);
                mEraserCanvas.drawCircle(centerX, centerY
                        , radiusSize, mBorderPaint);
            }
        }

        canvas.drawBitmap(mEraserBitmap, 0, 0, null);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mCurrRawX = ev.getRawX();
        mCurrRawY = ev.getRawY();
        return super.dispatchTouchEvent(ev);
    }

    public float getCurrentRawX() {
        return mCurrRawX;
    }

    public float getCurrentRawY() {
        return mCurrRawY;
    }

}
