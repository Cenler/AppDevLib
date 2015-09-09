package com.icenler.lib.view.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Cenler - 2015/3/26
 * Description：点击返回的 ImageButton
 */
public class BackImgButton extends ImageButton {

    public BackImgButton(Context context) {
        super(context);
        initView();
    }

    public BackImgButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BackImgButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            }
        });
    }

}
