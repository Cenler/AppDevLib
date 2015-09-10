package com.icenler.lib.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.icenler.lib.R;
import com.icenler.lib.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iCenler - 2015/9/10.
 * Description： 微信 TabView(使用需实现：OnItemIconTextSelectListener)
 */
public class TabView extends LinearLayout implements View.OnClickListener {

    private static final int DEFAULT_TEXTSIZE = 12;
    private static final int DEFAULT_TEXT_COLOR_NORMAL = 0xFF777777;
    private static final int DEFAULT_TEXT_COLOR_SELECT = 0xFF45C01A;
    private static final int DEFAULT_ITEM_PADDING = 10;

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private int mItemCount;
    private List<TabItem> mTabItems;

    private int mTextSize = ScreenUtil.sp2px(DEFAULT_TEXTSIZE);
    private int mItemPadding = ScreenUtil.dp2px(DEFAULT_ITEM_PADDING);
    private int mTextColorNormal = DEFAULT_TEXT_COLOR_NORMAL;
    private int mTextColorSelect = DEFAULT_TEXT_COLOR_SELECT;

    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener mOnPageChangeListener) {
        this.mOnPageChangeListener = mOnPageChangeListener;     // 滑动改变监听
    }

    private OnItemIconTextSelectListener mListener;

    public interface OnItemIconTextSelectListener {

        int[] onIconSelect(int position);

        String onTextSelect(int position);
    }

    public TabView(Context context) {
        this(context, null);
    }

    public TabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        getObtainAttributes(attrs);
    }

    private void getObtainAttributes(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.TabView);
        int N = array.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.TabView_tab_text_size:
                    mTextSize = array.getDimensionPixelOffset(attr, mTextSize);
                    break;
                case R.styleable.TabView_tab_text_normal_color:
                    mTextColorNormal = array.getColor(attr, mTextColorNormal);
                    break;
                case R.styleable.TabView_tab_text_select_color:
                    mTextColorSelect = array.getColor(attr, mTextColorSelect);
                    break;
                case R.styleable.TabView_tab_item_padding:
                    mItemPadding = array.getDimensionPixelOffset(attr, mItemPadding);
                    break;
            }
        }

        array.recycle();
    }

    public void setViewPager(final ViewPager mViewPager) {
        if (mViewPager == null) return;

        this.mViewPager = mViewPager;
        this.mPagerAdapter = mViewPager.getAdapter();
        if (this.mPagerAdapter == null)
            throw new RuntimeException("Not found PagerAdapter, Please setter Adapter!");

        this.mItemCount = mPagerAdapter.getCount();
        this.mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // TODO 下标越界待处理，创建Map集合，在创建 Pager 时进行添加操作（类似JazzyViewPager）
                View leftView = mViewPager.getChildAt(position);
                View rightView = mViewPager.getChildAt(position + 1);

                if (positionOffset > 0) {
                    leftView.setAlpha(1 - positionOffset);  // [1, 0]
                    rightView.setAlpha(positionOffset);     // [0, 1]
                    mTabItems.get(position).setTabAlpha(1 - positionOffset);
                    mTabItems.get(position + 1).setTabAlpha(positionOffset);
                } else {
                    mViewPager.getChildAt(position).setAlpha(1);
                    mTabItems.get(position).setTabAlpha(1 - positionOffset);
                }

                if (mOnPageChangeListener != null)
                    mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                if (mOnPageChangeListener != null)
                    mOnPageChangeListener.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mOnPageChangeListener != null)
                    mOnPageChangeListener.onPageScrollStateChanged(state);
            }
        });

        if (mPagerAdapter instanceof OnItemIconTextSelectListener) {
            mListener = (OnItemIconTextSelectListener) mPagerAdapter;
        } else {
            throw new RuntimeException("Please implements TabView.OnItemIconTextSelectListener to PagerAdapter!");
        }

        initTabItem();// 初始化 MenuBar
    }

    private void initTabItem() {
        mTabItems = new ArrayList<>();

        TabItem tabItem;
        LayoutParams params;
        for (int i = 0; i < mItemCount; i++) {
            tabItem = new TabItem(getContext());
            params = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            tabItem.setPadding(mItemPadding, mItemPadding, mItemPadding, mItemPadding);
            tabItem.setIconText(mListener.onIconSelect(i), mListener.onTextSelect(i));
            tabItem.setTextSize(mTextSize);
            tabItem.setTextColorNormal(mTextColorNormal);
            tabItem.setTextColorSelect(mTextColorSelect);
            tabItem.setLayoutParams(params);
            tabItem.setTag(i);
            tabItem.setOnClickListener(this);
            this.addView(tabItem);
            mTabItems.add(tabItem);
        }
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        if (mViewPager.getCurrentItem() == position) return;

        for (TabItem tabItem : mTabItems) {
            tabItem.setTabAlpha(0);
        }

        mTabItems.get(position).setTabAlpha(1);
        mViewPager.setCurrentItem(position, false);
    }

}
