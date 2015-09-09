package com.icenler.lib.utils.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by iCenler - 2015/9/10.
 * Description：通用 BaseAdapter 适配器 - 可进行快速适配，简化代码量
 */
public abstract class QuickBaseAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected final int mLayoutId;

    public QuickBaseAdapter(Context context, List<T> datas, int layoutId) {
        this.mContext = context;
        this.mDatas = datas;
        this.mLayoutId = layoutId;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderHelper holder = getViewHolder(position, convertView, parent);
        convert(holder, getItem(position));

        return holder.getConvertView();
    }

    private ViewHolderHelper getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolderHelper.get(mContext, convertView, parent, mLayoutId, position);
    }

    /**
     * @param holder 缓存回收对象
     * @param item   getItem(int position) 实例
     */
    public abstract void convert(ViewHolderHelper holder, T item);

    public void add(T item) {
        mDatas.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<T> item) {
        mDatas.addAll(item);
        notifyDataSetChanged();
    }

    public void set(T oldItem, T newItem) {
        set(mDatas.indexOf(oldItem), newItem);
    }

    public void set(int index, T item) {
        mDatas.set(index, item);
        notifyDataSetChanged();
    }

    public void remove(T item) {
        mDatas.remove(item);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        mDatas.remove(index);
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> item) {
        mDatas.clear();
        mDatas.addAll(item);
        notifyDataSetChanged();
    }

    public boolean contains(T item) {
        return mDatas.contains(item);
    }

    /** 清空数据 */
    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

}
