package com.icenler.lib;

import android.os.Bundle;

import com.icenler.lib.base.BaseApplication;
import com.icenler.lib.utils.manager.ToastManager;
import com.icenler.lib.view.swiplayout.SwipeBackActivity;
import com.icenler.lib.view.swiplayout.SwipeBackLayout;

import butterknife.ButterKnife;

public class MainActivity extends SwipeBackActivity {

    private long firshTimeOfClickBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
    }

    /**
     * TODO 工作安排：
     * 4、 处理带处理项
     * 5、 pinned-section-listview
     */
    private void init() {
        SwipeBackLayout mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_ALL);
        // mSwipeBackLayout.setEdgeSize();
        mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {

            }

            @Override
            public void onEdgeTouch(int edgeFlag) {

            }

            @Override
            public void onScrollOverThreshold() {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firshTimeOfClickBackPressed > 2000) {
            firshTimeOfClickBackPressed = System.currentTimeMillis();
            ToastManager.show(this, getString(R.string.prompt_exit_app));
        } else {
            BaseApplication.getInstance().exitApp();
            super.onBackPressed();
        }
    }

}
