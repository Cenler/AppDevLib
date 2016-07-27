package com.icenler.lib.feature.mvp.base;

import android.support.annotation.NonNull;

import com.icenler.lib.utils.Preconditions;

import rx.subscriptions.CompositeSubscription;

public class BasePresenter<V extends BaseView> implements IPresenter<V> {

    protected V mViewHandle;
    protected CompositeSubscription mSubscriptions;

    @Override
    public void attachView(@NonNull V viewHandle) {
        mViewHandle = Preconditions.checkNotNull(viewHandle);
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView() {
        mViewHandle = null;
        mSubscriptions.unsubscribe();
        mSubscriptions = null;
    }

    public boolean isAttached() {
        return mViewHandle != null;
    }

    public V getViewHandle() {
        return mViewHandle;
    }

}
