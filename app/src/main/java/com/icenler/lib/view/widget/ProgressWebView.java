package com.icenler.lib.view.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AbsoluteLayout;
import android.widget.ProgressBar;

import com.icenler.lib.R;
import com.icenler.lib.utils.ScreenUtil;

/**
 * Created by iCenler - 2015/4/11
 * Description：带加载进度条的 WebView
 */
public class ProgressWebView extends WebView {

    private static final int DEFAULT_PROGRESS_HEIGHT = 3;
    private static final int DEFAULT_PROGRESS_MAX = 150;

    protected ProgressBar mProgressBar;
    protected AbsoluteLayout.LayoutParams lp;
    protected int mProgressBarMax = DEFAULT_PROGRESS_MAX;
    protected int mProgressHeight = ScreenUtil.dp2px(DEFAULT_PROGRESS_HEIGHT);

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressBar.setProgress(mProgressBar.getProgress() + 1);
            if (mProgressBar.getProgress() == mProgressBarMax) {
                mHandler.removeCallbacksAndMessages(null);
                mProgressBar.setVisibility(View.GONE);
            } else {
                mHandler.sendEmptyMessage(0);
            }
            super.handleMessage(msg);
        }
    };

    public ProgressWebView(Context context) {
        super(context);
        init();
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        lp = (AbsoluteLayout.LayoutParams) mProgressBar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        mProgressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    private void init() {
        mProgressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        mProgressBar.setMax(mProgressBarMax);
        mProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.shape_progress_webview));
        mProgressBar.setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.MATCH_PARENT, mProgressHeight, 0, 0));
        this.addView(mProgressBar);
        this.setWebChromeClient(new MyWebChromeClient());
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mProgressBar.setProgress(newProgress);
            if (newProgress == 100)
                mHandler.sendEmptyMessage(0);
            super.onProgressChanged(view, newProgress);
        }
    }

}
