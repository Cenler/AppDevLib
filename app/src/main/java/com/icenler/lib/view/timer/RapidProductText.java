package com.icenler.lib.view.timer;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.AttributeSet;

public class RapidProductText extends ScalableIconText {

    private ProductTickTimer timer;
    private long lastReadTime;
    private long totalLeavingTime;

    private boolean isShowMill = true; //是否显示毫秒

    public RapidProductText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RapidProductText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * must call before onDisplay()
     *
     * @param time original count-down-time
     */
    public void init(long time) {
        totalLeavingTime = time;
        lastReadTime = SystemClock.elapsedRealtime();
    }

    /**
     * call in onDisplay()
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
            showNone();
        }
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        totalLeavingTime = 0L;
        showNone();
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

    private static final String TICK_FORMAT = "剩余 %d 天";

    private class ProductTickTimer extends CountDownTimer {

        int day, hour, min, sec, csec;
        String header = "";
        long leaving;

        public ProductTickTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            long centi_second = millisInFuture / 100L;
            if (centi_second > 0L) {
                day = (int) (centi_second / 864000L);
                hour = (int) (centi_second % 864000L / 36000L);
                min = (int) (centi_second % 36000L / 600L);
                sec = (int) (centi_second % 600L / 10L);
                csec = (int) (centi_second % 10L);
                header = getDateTimer(day, hour, min, sec);
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            leaving = millisUntilFinished;
            int d = day, h = hour, m = min, s = sec, c = csec;

            if (c-- == 0) {
                c = 9;
                header = getDateTimer(d, h, m, s);
                if (s-- == 0) {
                    s = 59;
                    if (m-- == 0) {
                        m = 59;
                        if (h-- == 0) {
                            h = 23;
                            if (d-- == 0) {
                                showNone();
                                return;
                            }
                        }
                    }
                }
            }

            //setText( header + c);
            setText("还剩" + header);
            day = d;
            hour = h;
            min = m;
            sec = s;
            csec = c;
        }

        @Override
        public void onFinish() {
            showNone();
        }

    }

    private String getDayRemain(int d, int h, int m, int s, int c) {
        if (d > 0)
            return String.format(TICK_FORMAT, d + 1);
        else
            return "最后 1 天";
    }

    private String getDateTimer(int d, int h, int m, int s) {
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

    void showNone() {
        setText(getDateTimer(0, 0, 0, 0));
    }
}
