package com.icenler.lib.view.timer;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by iCenler - 2015/9/11.
 * Description：计时器
 */
public class TimerTextView extends TextView {

    private static final String TICK_FORMAT = "剩余 %d 天";

    private ProductTickTimer timer;
    private long lastReadTime;
    private long totalLeavingTime;

    private boolean isShowDays = true; //是否显示天数
    private boolean isShowMill = true; //是否显示毫秒

    public TimerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 初始化倒计时时间，并开始计时
     */
    public void init(long time) {
        totalLeavingTime = time;
        lastReadTime = SystemClock.elapsedRealtime();
    }

    /**
     * 开始显示
     */
    public void start() {
        if (timer != null) timer.cancel();

        long now = SystemClock.elapsedRealtime();
        long offset = now - lastReadTime;
        if (offset > 0) {
            totalLeavingTime -= offset;
        }
        if (totalLeavingTime > 0L) {
            timer = new ProductTickTimer(totalLeavingTime, 100L);
            timer.start();
        } else {
            finish();
        }
    }

    /**
     * 取消显示
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        totalLeavingTime = 0L;
        finish();
    }

    /**
     * call in onBack() / onLeave()
     */
    public void cancel() {
        if (timer != null) {
            totalLeavingTime = timer.leaving;
            timer.cancel();
            timer = null;
        }
        lastReadTime = SystemClock.elapsedRealtime();
    }

    /**
     * 以1/10秒为单位倒数计时
     */
    private class ProductTickTimer extends CountDownTimer {
        private int day, hour, min, sec, csec;
        private long leaving;
        private String remainingTime = "";

        public ProductTickTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            long second = millisInFuture / 100L;
            if (second > 0L) {
                day = (int) (second / 864000L);
                hour = (int) (second % 864000L / 36000L);
                min = (int) (second % 36000L / 600L);
                sec = (int) (second % 600L / 10L);
                csec = (int) (second % 10L);
                remainingTime = getFormatTimer(day, hour, min, sec);
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // 没0.1s回调一次
            leaving = millisUntilFinished;
            int d = day, h = hour, m = min, s = sec, c = csec;

            if (c-- == 0) {
                c = 9;
                remainingTime = getFormatTimer(d, h, m, s);
                if (s-- == 0) {
                    s = 59;
                    if (m-- == 0) {
                        m = 59;
                        if (h-- == 0) {
                            h = 23;
                            if (d-- == 0) {
                                finish();
                                return;
                            }
                        }
                    }
                }
            }

            setText(remainingTime);
            day = d;
            hour = h;
            min = m;
            sec = s;
            csec = c;
        }

        @Override
        public void onFinish() {
            finish();
        }

    }

    private String getFormatDays(int d, int h, int m, int s, int c) {
        if (d > 0)
            return String.format(TICK_FORMAT, d + 1);
        else
            return "最后 1 天";
    }

    private String getFormatTimer(int d, int h, int m, int s) {
        if (d > 0) {
            if (isShowMill)
                return String.format("%d天%d时%d分%d秒", d, h, m, s);
            else
                return String.format("%d天%d时%d分", d, h, m);
        } else {
            if (isShowMill)
                return String.format("%d时%d分%d秒", h, m, s);
            else
                return String.format("%d时%d分", h, m);

        }
    }

    /**
     * 倒计时结束
     */
    private void finish() {
        this.setText(getFormatTimer(0, 0, 0, 0));
    }

}
