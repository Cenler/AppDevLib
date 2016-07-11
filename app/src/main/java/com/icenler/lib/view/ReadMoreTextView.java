package com.icenler.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.icenler.lib.R;

/**
 * Created by iCenler - 2016/4/28.
 * Description：可展开收起的文本控件
 */
public class ReadMoreTextView extends TextView {

    static final int DEF_CLICKABLE_TEXT_COLOR = 0x619CC1;
    static final String DEF_EXPANDED_TEXT = "展开";
    static final String DEF_COLLAPSED_TEXT = "收起";
    static final String ELLIPSIZE = "... ";

    private boolean isShowCollapsedText = true;
    private boolean isUnfold = false;

    private int mTrimLength = -1;
    private int mTrimLines;
    private int mClickableTextColor = DEF_CLICKABLE_TEXT_COLOR;

    private String mExpandedText = DEF_EXPANDED_TEXT;
    private String mCollapsedText = DEF_COLLAPSED_TEXT;
    private String mText;

    private BufferType mBufferType;

    private ClickableSpan mClickableSpan;

    public ReadMoreTextView(Context context) {
        this(context, null);
    }

    public ReadMoreTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReadMoreTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mClickableSpan = new ReadMoreClickableSpan();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ReadMoreTextView);

        final int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int index = typedArray.getIndex(i);
            switch (index) {
                case R.styleable.ReadMoreTextView_rmtv_clickableTextColor:
                    mClickableTextColor = typedArray.getColor(index, mClickableTextColor);
                    break;

                case R.styleable.ReadMoreTextView_rmtv_showCollapsedText:
                    isShowCollapsedText = typedArray.getBoolean(index, isShowCollapsedText);
                    break;

                case R.styleable.ReadMoreTextView_rmtv_trimCollapsedText:
                    mCollapsedText = typedArray.getString(index);
                    break;

                case R.styleable.ReadMoreTextView_rmtv_trimExpandedText:
                    mExpandedText = typedArray.getString(index);
                    break;

                case R.styleable.ReadMoreTextView_rmtv_trimLength:
                    mTrimLength = typedArray.getInteger(index, mTrimLength);
                    break;

                case R.styleable.ReadMoreTextView_rmtv_trimLines:
                    mTrimLines = typedArray.getInteger(index, mTrimLines);
                    break;
            }
        }

        typedArray.recycle();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        mText = text.toString();
        mBufferType = type;
        setTrimText();
    }

    private void setTrimText() {
        super.setText(getProcessText(mText), mBufferType);
        super.setMovementMethod(LinkMovementMethod.getInstance());
        super.setHighlightColor(Color.TRANSPARENT);// 去除按下背景
    }

    private CharSequence getProcessText(CharSequence text) {
        if (!TextUtils.isEmpty(text) && (mTrimLength > 0 || mTrimLines > 0)) {
            addLayoutListener();

            if (isUnfold) {
                return updateExpandedText();
            } else {
                return updateCollapsedText();
            }
        }

        return text;
    }

    private CharSequence updateExpandedText() {
        if (isShowCollapsedText) {
            SpannableStringBuilder ssb = new SpannableStringBuilder(mText, 0, mText.length())
                    .append(mCollapsedText);
            ssb.setSpan(mClickableSpan
                    , ssb.length() - mCollapsedText.length()
                    , ssb.length()
                    , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ssb;
        }

        return mText;
    }

    private CharSequence updateCollapsedText() {
        int length = getTrimLength();
        if (length >= mText.length())
            return mText;

        SpannableStringBuilder ssb = new SpannableStringBuilder(mText, 0, length + 1)
                .append(ELLIPSIZE)
                .append(mExpandedText);
        ssb.setSpan(mClickableSpan
                , ssb.length() - mExpandedText.length()
                , ssb.length()
                , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    private void addLayoutListener() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);

                if (!isUnfold)
                    return;

                StaticLayout staticLayout = new StaticLayout(mText
                        , getPaint()
                        , getMeasuredWidth() - getCompoundPaddingLeft() - getCompoundPaddingRight()
                        , Layout.Alignment.ALIGN_NORMAL
                        , getLineSpacingMultiplier()
                        , getLineSpacingExtra()
                        , false);

                int lineCount = staticLayout.getLineCount();
                int lineCharsCount = staticLayout.getLineEnd(0);
                float lineMaxWidth = staticLayout.getLineMax(0);

                if (mTrimLines > 0 && mTrimLines < lineCount) {
                    // 截取一行的一半字符宽度进行收缩
                    float charsWidth = lineMaxWidth / lineCharsCount;
                    int charsCount = Math.round(lineMaxWidth * 0.5f / charsWidth);
                    mTrimLength = lineCharsCount * (mTrimLines - 1) + charsCount;
                }

                setTrimText();
            }
        });
    }

    private int getTrimLength() {
        return mTrimLength > 0 ? mTrimLength : mText.length() - 1;
    }

    public String getWholeText() {
        return mText;
    }

    class ReadMoreClickableSpan extends ClickableSpan {
        @Override
        public void onClick(View widget) {
            isUnfold = !isUnfold;
            setTrimText();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(mClickableTextColor);
        }
    }

}
