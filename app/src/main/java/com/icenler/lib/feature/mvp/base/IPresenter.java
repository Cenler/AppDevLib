package com.icenler.lib.feature.mvp.base;

import android.support.annotation.NonNull;

public interface IPresenter<V extends BaseView> {

    void attachView(@NonNull V viewHandle);

    void detachView();
}
