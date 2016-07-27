package com.icenler.lib.feature.mvp.demo1;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Cenler on 2015/11/27.
 * Description: MVP for Activity Presenter
 */
public abstract class BaseActivityPresenter<T extends IDelegate> extends Activity {

    /**
     * @return 代理模型
     */
    protected abstract Class<T> getDelegateClass();

    /**
     * 视图类
     */
    protected T delegateView;

    public BaseActivityPresenter() {
        try {
            delegateView = getDelegateClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        delegateView.create(getLayoutInflater(), null, savedInstanceState);
        setContentView(delegateView.getRootView());
        delegateView.initWidth();
        bindEventListener();
    }

    protected void bindEventListener() {
        // TODO 事件绑定
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        delegateView = null;
    }

}
