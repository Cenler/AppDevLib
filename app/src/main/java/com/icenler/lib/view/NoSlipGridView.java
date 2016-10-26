package com.icenler.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by iCenler - 2015/5/14.
 * Description：解决多个滑动组件嵌套
 */
public class NoSlipGridView extends GridView {

    public NoSlipGridView(Context context) {
        super(context);
    }

    public NoSlipGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoSlipGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 6, MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
