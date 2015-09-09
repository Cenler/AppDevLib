package com.icenler.lib.utils.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by iCenler - 2015/9/10.
 * Description：适配器缓存帮助类
 */
public class ViewHolderHelper {

    protected SparseArray<View> mViews;
    protected Context mContext;
    protected View mConvertView;
    protected int mPosition;

    protected ViewHolderHelper(Context context, ViewGroup parent, int layoutId, int position) {
        this.mContext = context;
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        this.mConvertView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        this.mConvertView.setTag(this);
    }


    public static ViewHolderHelper get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        return convertView != null ? (ViewHolderHelper) convertView.getTag() : new ViewHolderHelper(context, parent, layoutId, position);
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);

        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }

        return (T) view;
    }

    /**
     * @param value 设置文本
     * @return
     */
    public ViewHolderHelper setText(int viewId, String value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    /**
     * @param color 背景色值
     * @return
     */
    public ViewHolderHelper setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * @param backgroundRes 背景资源
     * @return
     */
    public ViewHolderHelper setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    /**
     * @param textColor 十六进制色值
     * @return
     */
    public ViewHolderHelper setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    /**
     * @param textColorRes 颜色资源ID
     * @return
     */
    public ViewHolderHelper setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    /**
     * @param imageUrl 加载指定连接图片
     * @return
     */
    public ViewHolderHelper setImageUrl(int viewId, String imageUrl) {
        ImageView view = getView(viewId);
        // TODO 待完善添加
        // new BitmapUtils(mContext).display(view, imageUrl);
        return this;
    }

    /**
     * @param imageResId 图片资源ID
     * @return
     */
    public ViewHolderHelper setImageResource(int viewId, int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    /**
     * @param drawable 设置图片
     * @return
     */
    public ViewHolderHelper setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    /**
     * @param bitmap 设置图片
     * @return
     */
    public ViewHolderHelper setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    /**
     * @param value 透明度
     * @return
     */
    public ViewHolderHelper setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    /**
     * @param visible 是否显示
     * @return
     */
    public ViewHolderHelper setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 设置连接形式：Linkify - Spannable
     */
    public ViewHolderHelper linkify(int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    /**
     * @param listener 点击监听事件
     * @return
     */
    public ViewHolderHelper setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * @param listener 触摸监听事件
     * @return
     */
    public ViewHolderHelper setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    /**
     * @param listener 长按监听事件
     * @return
     */
    public ViewHolderHelper setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    /**
     * @param tag 目标对象
     * @return
     */
    public ViewHolderHelper setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    /**
     * @param key 指定Key
     * @param tag 目标对象
     * @return
     */
    public ViewHolderHelper setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    /**
     * @param checked 选中状态
     * @return
     */
    public ViewHolderHelper setChecked(int viewId, boolean checked) {
        Checkable view = (Checkable) getView(viewId);
        view.setChecked(checked);
        return this;
    }

    /**
     * @param adapter 设置适配器
     * @return
     */
    public ViewHolderHelper setAdapter(int viewId, Adapter adapter) {
        AdapterView view = getView(viewId);
        view.setAdapter(adapter);
        return this;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public int getPosition() {
        return mPosition;
    }

}
