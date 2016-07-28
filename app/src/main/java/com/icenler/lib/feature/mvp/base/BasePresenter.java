package com.icenler.lib.feature.mvp.base;

import android.support.annotation.NonNull;

import com.icenler.lib.utils.Preconditions;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BasePresenter<V extends BaseView> implements IPresenter<V> {

    private V mViewHandle;
    private CompositeSubscription mSubscriptions;

    @Override
    public void attachView(@NonNull V viewHandle) {
        mViewHandle = Preconditions.checkNotNull(viewHandle);
    }

    @Override
    public void detachView() {
        mViewHandle = null;
        if (mSubscriptions != null) {
            mSubscriptions.unsubscribe();
            mSubscriptions = null;
        }
    }

    public boolean isAttached() {
        return mViewHandle != null;
    }

    public V getViewHandle() {
        return mViewHandle;
    }

    public void addSubscription(Subscription s) {
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }
        mSubscriptions.add(s);
    }

}
