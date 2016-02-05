package com.icenler.lib.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icenler.lib.R;
import com.icenler.lib.utils.ScreenUtil;

/**
 * 通用标题导航特性：
 * 1、 可根据 value_common_titlebar.xml 中的自定义属性设置其
 *      - 标题、左右侧文本
 *      - 左右侧图标、图标对齐方式及图标大小
 * 2、 可对 setCommonTitleBar 初始化，并实现 ICommonTitleBar 接口进行事件回调处理
 */
public class CommonTitleBar extends RelativeLayout implements View.OnClickListener {

    private static final int DEFAULT_LEFT_ICON_SIZE = 16;// 默认图标大小（单位：dp）
    private static final int DEFAULT_RIGHT_ICON_SIZE = 16;
    private static final int ICON_ALGIN_LEFT = 0;// 默认图标对齐方式
    private static final int ICON_ALGIN_RIGHT = 1;

    protected Context mContext;
    protected TextView mLeftSide_TV, mRightSide_TV, mTitle_TV;
    protected ICommonTitleBar mICommonTitleBar;
    /**
     *  CommonTitleBar 接口类
     */
    public interface ICommonTitleBar {
        void onTileClick(View view);
        /**
         * 左侧按钮点击触发
         */
        void onLeftClick(View view);

        /**
         * 右侧按钮点击触发
         */
        void onRightClick(View view);
    }

    public void setCommonTitleBar(ICommonTitleBar listener) {
        this.mICommonTitleBar = listener;
    }

    /**
     *  默认图标显示大小、对齐方式
     */
    protected int mLeftIconAlign = ICON_ALGIN_LEFT;
    protected int mRightIconAlign = ICON_ALGIN_LEFT;
    protected int mLeftIconSize = ScreenUtil.dp2px(DEFAULT_LEFT_ICON_SIZE);
    protected int mRightIconSize = ScreenUtil.dp2px(DEFAULT_RIGHT_ICON_SIZE);

    public CommonTitleBar(Context context) {
        super(context);
        init(null, 0);
    }

    public CommonTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CommonTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        mContext = getContext();
        super.inflate(mContext, R.layout.common_titlebar_layout, this);

        mLeftSide_TV = (TextView) findViewById(R.id.titlebar_left_tv);
        mRightSide_TV = (TextView) findViewById(R.id.titlebar_right_tv);
        mTitle_TV = (TextView) findViewById(R.id.titlebar_title_tv);

        mTitle_TV.setOnClickListener(this);
        mLeftSide_TV.setOnClickListener(this);
        mRightSide_TV.setOnClickListener(this);

        getObtainStyleAttributes(attrs, defStyle);
    }

    /**
     * 获取定义的属性值
     * @param attrs
     */
    private void getObtainStyleAttributes(AttributeSet attrs, int defStyle) {
        TypedArray attributes = mContext.obtainStyledAttributes(attrs, R.styleable.CommonTitleBar, defStyle, 0);

        /*
        * getDimension: 基于 DisplayMetrics 进行转换（单位：float）
        * getDimensionPixelSize: 四舍五入（单位：px）
        * getDimensionPixelOffset: 强转（单位：px）
        * */
        String mTitle = attributes.getString(R.styleable.CommonTitleBar_titleLabel);
        String mLeftText = attributes.getString(R.styleable.CommonTitleBar_leftLabel);
        String mRightText = attributes.getString(R.styleable.CommonTitleBar_rightLabel);
        Drawable mLeftIcon = attributes.getDrawable(R.styleable.CommonTitleBar_leftIcon);
        Drawable mRightIcon = attributes.getDrawable(R.styleable.CommonTitleBar_rightIcon);
        mLeftIconSize = attributes.getDimensionPixelSize(R.styleable.CommonTitleBar_leftIconSize, mLeftIconSize);
        mRightIconSize = attributes.getDimensionPixelSize(R.styleable.CommonTitleBar_rightIconSize, mRightIconSize);
        mLeftIconAlign = attributes.getInt(R.styleable.CommonTitleBar_leftIconAlign, mRightIconAlign);
        mRightIconAlign = attributes.getInt(R.styleable.CommonTitleBar_rightIconAlign, mRightIconAlign);

        // 回馈之前StyledAttributes检索,为以后重用。
        attributes.recycle();

        setTitle(mTitle);
        setLeft(mLeftText, mLeftIcon, mLeftIconAlign);
        setRight(mRightText, mRightIcon, mRightIconAlign);
    }

    /**
     *  设置 title
     */
    public void setTitle(String text) {
        mTitle_TV.setText(text);
    }

    public void setTitle(int textResId) {
        checkAndSetText(mTitle_TV, textResId);
    }

    /**
     * 设置左侧按钮文本、Icon和Icon对齐方式
     */
    public void setLeft(String text, Drawable icon, int align) {
        mLeftSide_TV.setText(text);
        checkAlign(mLeftSide_TV, resizeDrawable(icon, mLeftIconSize, mLeftIconSize), align);
    }

    public void setLeft(int textResId, int iconResId, int align) {
        checkAndSetText(mLeftSide_TV, textResId);
        checkAlign(mLeftSide_TV, resizeDrawable(iconResId, mLeftIconSize, mLeftIconSize), align);
    }

    public void setLeftVisibility(int visibility) { mLeftSide_TV.setVisibility(visibility); }

    /**
     * 设置右侧按钮文本、Icon和Icon对齐方式
     */
    public void setRight(String text, Drawable icon, int align) {
        mRightSide_TV.setText(text);
        checkAlign(mRightSide_TV, resizeDrawable(icon, mRightIconSize, mRightIconSize), align);
    }

    public void setRight(int textResId, int iconResId, int align) {
        checkAndSetText(mRightSide_TV, textResId);
        checkAlign(mRightSide_TV, resizeDrawable(iconResId, mRightIconSize, mRightIconSize), align);
    }

    public void setRightVisibility(int visibility) { mRightSide_TV.setVisibility(visibility); }

    protected void checkAndSetText(TextView tv, int textResId) {
        tv.setText(textResId <= 0 ? null : textResId);
    }

    protected void checkAlign(TextView tv, Drawable icon, int algin) {
        switch (algin) {
            case ICON_ALGIN_RIGHT:
                tv.setCompoundDrawables(null, null, icon, null);
                break;
            case ICON_ALGIN_LEFT:
            default:
                tv.setCompoundDrawables(icon, null, null, null);
                break;
        }
    }

    protected Drawable resizeDrawable(int iconResId, int maxWidth, int maxHeight) {
        return iconResId <= 0 ? null : resizeDrawable(getResources().getDrawable(iconResId), maxWidth, maxHeight);
    }

    protected  Drawable resizeDrawable(Drawable drawable, int maxWidth, int maxHeight) {
        if (null != drawable) {
            float dWidth = drawable.getMinimumWidth();
            float dHeight = drawable.getMinimumHeight();
            float widthScale = maxWidth / dWidth;
            float heightScale = maxHeight / dHeight;
            float scale = Math.min(widthScale, heightScale);

            int targetWidtht = (int) (scale * dWidth + 0.5f);
            int tartgetHeight = (int) (scale * dHeight + 0.5f);
            drawable.setBounds(0, 0, targetWidtht, tartgetHeight);
        }

        return drawable;
    }

    @Override
    public void onClick(View v) {
        if (null != mICommonTitleBar) {
            switch (v.getId()) {
                case R.id.titlebar_left_tv:
                    mICommonTitleBar.onLeftClick(v);
                    break;
                case R.id.titlebar_right_tv:
                    mICommonTitleBar.onRightClick(v);
                    break;
                case R.id.titlebar_title_tv:
                    mICommonTitleBar.onTileClick(v);
                    break;

                default: break;
            }
        }
    }

}
