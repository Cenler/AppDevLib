package com.icenler.lib.view.timer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.icenler.lib.R;

public class ScalableIconText extends TextView {

    private int leftHeight = -1;
    private int leftWidth = -1;
    private int rightHeight = -1;
    private int rightWidth = -1;
    private int topHeight = -1;
    private int topWidth = -1;
    private int bottomHeight = -1;
    private int bottomWidth = -1;

    public ScalableIconText(Context context) {
        super(context);
    }

    public ScalableIconText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ScalableIconText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScalableIconText);
        if (a != null) {
            int count = a.getIndexCount();
            int index = 0;
            for (int i = 0; i < count; i++) {
                index = a.getIndex(i);
                switch (index) {
                    case R.styleable.ScalableIconText_bottom_height:
                        bottomHeight = a.getDimensionPixelSize(index, -1);
                        break;
                    case R.styleable.ScalableIconText_bottom_width:
                        bottomWidth = a.getDimensionPixelSize(index, -1);
                        break;
                    case R.styleable.ScalableIconText_left_height:
                        leftHeight = a.getDimensionPixelSize(index, -1);
                        break;
                    case R.styleable.ScalableIconText_left_width:
                        leftWidth = a.getDimensionPixelSize(index, -1);
                        break;
                    case R.styleable.ScalableIconText_right_height:
                        rightHeight = a.getDimensionPixelSize(index, -1);
                        break;
                    case R.styleable.ScalableIconText_right_width:
                        rightWidth = a.getDimensionPixelSize(index, -1);
                        break;
                    case R.styleable.ScalableIconText_top_height:
                        topHeight = a.getDimensionPixelSize(index, -1);
                        break;
                    case R.styleable.ScalableIconText_top_width:
                        topWidth = a.getDimensionPixelSize(index, -1);
                        break;
                }
            }

            Drawable[] drawables = getCompoundDrawables();
            int dir = 0;
            for (Drawable drawable : drawables) {
                setImageSize(drawable, dir++);
            }
            setCompoundDrawables(drawables[0], drawables[1], drawables[2],
                    drawables[3]);

            a.recycle();
        }

    }

    private void setImageSize(Drawable d, int dir) {
        if (d == null) {
            return;
        }

        int height = -1;
        int width = -1;

        switch (dir) {
            case 0:
                height = leftHeight;
                width = leftWidth;
                break;
            case 1:
                height = topHeight;
                width = topWidth;
                break;
            case 2:
                height = rightHeight;
                width = rightWidth;
                break;
            case 3:
                height = bottomHeight;
                width = bottomWidth;
                break;
        }

        if (width != -1 && height != -1) {
            d.setBounds(0, 0, width, height);
        }
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {

        if (left != null) left.setBounds(0, 0, leftWidth, leftHeight);
        if (right != null) right.setBounds(0, 0, rightWidth, rightHeight);
        if (top != null) top.setBounds(0, 0, topWidth, topHeight);
        if (bottom != null) bottom.setBounds(0, 0, bottomWidth, bottomHeight);

        super.setCompoundDrawables(left, top, right, bottom);
    }
}
