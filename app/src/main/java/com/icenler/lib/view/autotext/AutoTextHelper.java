package com.icenler.lib.view.autotext;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Editable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.SingleLineTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.icenler.lib.R;

import java.util.ArrayList;

/**
 * A helper class to enable automatically resizing {@link TextView}`s {@code textSize} to fit
 * within its bounds.
 *
 * @attr ref R.styleable.AutoTextView_sizeToFit
 * @attr ref R.styleable.AutoTextView_minTextSize
 * @attr ref R.styleable.AutoTextView_precision
 */
public class AutoTextHelper {

    private static final String TAG = "AutoFitTextHelper";
    private static final boolean SPEW = false;

    // Minimum size of the text in pixels
    private static final int DEFAULT_MIN_TEXT_SIZE = 8; //sp
    // How precise we want to be when reaching the target textWidth size
    private static final float DEFAULT_PRECISION = 0.5f;

    /**
     * Creates a new instance of {@code AutofitHelper} that wraps a {@link TextView} and enables
     * automatically sizing the text to fit.
     */
    public static AutoTextHelper create(TextView view) {
        return create(view, null, 0);
    }

    /**
     * Creates a new instance of {@code AutofitHelper} that wraps a {@link TextView} and enables
     * automatically sizing the text to fit.
     */
    public static AutoTextHelper create(TextView view, AttributeSet attrs) {
        return create(view, attrs, 0);
    }

    /**
     * Creates a new instance of {@code AutofitHelper} that wraps a {@link TextView} and enables
     * automatically sizing the text to fit.
     */
    public static AutoTextHelper create(TextView view, AttributeSet attrs, int defStyle) {
        AutoTextHelper helper = new AutoTextHelper(view);
        boolean fitTextSize = true;
        if (attrs != null) {
            Context context = view.getContext();
            int minTextSize = (int) helper.getMinTextSize();
            float precision = helper.getPrecision();

            TypedArray typedArray = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.AutoTextView,
                    defStyle,
                    0);
            fitTextSize = typedArray.getBoolean(R.styleable.AutoTextView_fitTextSize, fitTextSize);
            minTextSize = typedArray.getDimensionPixelSize(R.styleable.AutoTextView_minTextSize,
                    minTextSize);
            precision = typedArray.getFloat(R.styleable.AutoTextView_precision, precision);
            typedArray.recycle();

            helper.setMinTextSize(TypedValue.COMPLEX_UNIT_PX, minTextSize)
                    .setPrecision(precision);
        }
        helper.setIsEnabled(fitTextSize);

        return helper;
    }

    /**
     * Re-sizes the textSize of the TextView so that the text fits within the bounds of the View.
     */
    private static void fitTextSize(TextView view, TextPaint paint, float minTextSize, float maxTextSize,
                                    int maxLines, float precision) {
        if (maxLines <= 0 || maxLines == Integer.MAX_VALUE) {
            // Don't auto-size since there's no limit on lines.
            return;
        }

        int targetWidth = view.getWidth() - view.getPaddingLeft() - view.getPaddingRight();
        if (targetWidth <= 0) {
            return;
        }

        CharSequence text = view.getText();
        TransformationMethod method = view.getTransformationMethod();
        if (method != null) {
            text = method.getTransformation(text, view);
        }

        Context context = view.getContext();
        Resources r = Resources.getSystem();
        DisplayMetrics displayMetrics;

        float size = maxTextSize;
        float high = size;
        float low = 0;

        if (context != null) {
            r = context.getResources();
        }
        displayMetrics = r.getDisplayMetrics();

        paint.set(view.getPaint());
        paint.setTextSize(size);

        if ((maxLines == 1 && paint.measureText(text, 0, text.length()) > targetWidth)
                || getLineCount(text, paint, size, targetWidth, displayMetrics) > maxLines) {
            size = getfitTextSize(text, paint, targetWidth, maxLines, low, high, precision,
                    displayMetrics);
        }

        if (size < minTextSize) {
            size = minTextSize;
        }

        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    /**
     * Recursive binary search to find the best size for the text.
     */
    private static float getfitTextSize(CharSequence text, TextPaint paint,
                                        float targetWidth, int maxLines, float low, float high, float precision,
                                        DisplayMetrics displayMetrics) {
        float mid = (low + high) / 2.0f;
        int lineCount = 1;
        StaticLayout layout = null;

        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, mid,
                displayMetrics));

        if (maxLines != 1) {
            layout = new StaticLayout(text, paint, (int) targetWidth, Layout.Alignment.ALIGN_NORMAL,
                    1.0f, 0.0f, true);
            lineCount = layout.getLineCount();
        }

        if (SPEW) Log.d(TAG, "low=" + low + " high=" + high + " mid=" + mid +
                " target=" + targetWidth + " maxLines=" + maxLines + " lineCount=" + lineCount);

        if (lineCount > maxLines) {
            // For the case that `text` has more newline characters than `maxLines`.
            if ((high - low) < precision) {
                return low;
            }
            return getfitTextSize(text, paint, targetWidth, maxLines, low, mid, precision,
                    displayMetrics);
        } else if (lineCount < maxLines) {
            return getfitTextSize(text, paint, targetWidth, maxLines, mid, high, precision,
                    displayMetrics);
        } else {
            float maxLineWidth = 0;
            if (maxLines == 1) {
                maxLineWidth = paint.measureText(text, 0, text.length());
            } else {
                for (int i = 0; i < lineCount; i++) {
                    if (layout.getLineWidth(i) > maxLineWidth) {
                        maxLineWidth = layout.getLineWidth(i);
                    }
                }
            }

            if ((high - low) < precision) {
                return low;
            } else if (maxLineWidth > targetWidth) {
                return getfitTextSize(text, paint, targetWidth, maxLines, low, mid, precision,
                        displayMetrics);
            } else if (maxLineWidth < targetWidth) {
                return getfitTextSize(text, paint, targetWidth, maxLines, mid, high, precision,
                        displayMetrics);
            } else {
                return mid;
            }
        }
    }

    private static int getLineCount(CharSequence text, TextPaint paint, float size, float width,
                                    DisplayMetrics displayMetrics) {
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, size,
                displayMetrics));
        StaticLayout layout = new StaticLayout(text, paint, (int) width,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        return layout.getLineCount();
    }

    private static int getMaxLines(TextView view) {
        int maxLines = -1; // No limit (Integer.MAX_VALUE also means no limit)

        TransformationMethod method = view.getTransformationMethod();
        if (method != null && method instanceof SingleLineTransformationMethod) {
            maxLines = 1;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // setMaxLines() and getMaxLines() are only available on android-16+
            maxLines = view.getMaxLines();
        }

        return maxLines;
    }

    // Attributes
    private TextView textView;
    private TextPaint textPaint;
    /**
     * Original textSize of the TextView.
     */
    private float textSize;

    private int maxLines;
    private float minTextSize;
    private float maxTextSize;
    private float precision;

    private boolean isEnabled;
    private boolean isFitting;

    private ArrayList<OnTextSizeChangeListener> listeners;

    private TextWatcher textWatcher = new AutofitTextWatcher();

    private View.OnLayoutChangeListener onLayoutChangeListener =
            new OnFitLayoutChangeListener();

    private AutoTextHelper(TextView view) {
        final Context context = view.getContext();
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;

        textView = view;
        textPaint = new TextPaint();
        setRawTextSize(view.getTextSize());

        maxLines = getMaxLines(view);
        minTextSize = scaledDensity * DEFAULT_MIN_TEXT_SIZE;
        maxTextSize = textSize;
        precision = DEFAULT_PRECISION;
    }

    /**
     * Adds an {@link OnTextSizeChangeListener} to the list of those whose methods are called
     * whenever the {@link TextView}'s {@code textSize} changes.
     */
    public AutoTextHelper addOnTextSizeChangeListener(OnTextSizeChangeListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<OnTextSizeChangeListener>();
        }
        listeners.add(listener);
        return this;
    }

    /**
     * Removes the specified {@link OnTextSizeChangeListener} from the list of those whose methods
     * are called whenever the {@link TextView}'s {@code textSize} changes.
     */
    public AutoTextHelper removeOnTextSizeChangeListener(OnTextSizeChangeListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
        return this;
    }

    /**
     * Returns the amount of precision used to calculate the correct text size to fit within its
     * bounds.
     */
    public float getPrecision() {
        return precision;
    }

    /**
     * Set the amount of precision used to calculate the correct text size to fit within its
     * bounds. Lower precision is more precise and takes more time.
     *
     * @param precision The amount of precision.
     */
    public AutoTextHelper setPrecision(float precision) {
        if (this.precision != precision) {
            this.precision = precision;

            fitTextSize();
        }
        return this;
    }

    /**
     * Returns the minimum size (in pixels) of the text.
     */
    public float getMinTextSize() {
        return minTextSize;
    }

    /**
     * Set the minimum text size to the given value, interpreted as "scaled pixel" units. This size
     * is adjusted based on the current density and user font size preference.
     *
     * @param size The scaled pixel size.
     * @attr ref R.styleable.AutoTextView_minTextSize
     */
    public AutoTextHelper setMinTextSize(float size) {
        return setMinTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * Set the minimum text size to a given unit and value. See TypedValue for the possible
     * dimension units.
     *
     * @param unit The desired dimension unit.
     * @param size The desired size in the given units.
     * @attr ref R.styleable.AutoTextView_minTextSize
     */
    public AutoTextHelper setMinTextSize(int unit, float size) {
        Context context = textView.getContext();
        Resources r = Resources.getSystem();

        if (context != null) {
            r = context.getResources();
        }

        setRawMinTextSize(TypedValue.applyDimension(unit, size, r.getDisplayMetrics()));
        return this;
    }

    private void setRawMinTextSize(float size) {
        if (size != minTextSize) {
            minTextSize = size;

            fitTextSize();
        }
    }

    /**
     * Returns the maximum size (in pixels) of the text.
     */
    public float getMaxTextSize() {
        return maxTextSize;
    }

    /**
     * Set the maximum text size to the given value, interpreted as "scaled pixel" units. This size
     * is adjusted based on the current density and user font size preference.
     *
     * @param size The scaled pixel size.
     * @attr ref android.R.styleable#TextView_textSize
     */
    public AutoTextHelper setMaxTextSize(float size) {
        return setMaxTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * Set the maximum text size to a given unit and value. See TypedValue for the possible
     * dimension units.
     *
     * @param unit The desired dimension unit.
     * @param size The desired size in the given units.
     * @attr ref android.R.styleable#TextView_textSize
     */
    public AutoTextHelper setMaxTextSize(int unit, float size) {
        Context context = textView.getContext();
        Resources r = Resources.getSystem();

        if (context != null) {
            r = context.getResources();
        }

        setRawMaxTextSize(TypedValue.applyDimension(unit, size, r.getDisplayMetrics()));
        return this;
    }

    private void setRawMaxTextSize(float size) {
        if (size != maxTextSize) {
            maxTextSize = size;

            fitTextSize();
        }
    }

    /**
     * @see TextView#getMaxLines()
     */
    public int getMaxLines() {
        return maxLines;
    }

    /**
     * @see TextView#setMaxLines(int)
     */
    public AutoTextHelper setMaxLines(int lines) {
        if (maxLines != lines) {
            maxLines = lines;

            fitTextSize();
        }
        return this;
    }

    /**
     * Returns whether or not automatically resizing text is enabled.
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * Set the enabled state of automatically resizing text.
     */
    public AutoTextHelper setIsEnabled(boolean enabled) {
        if (isEnabled != enabled) {
            isEnabled = enabled;

            if (enabled) {
                textView.addTextChangedListener(textWatcher);
                textView.addOnLayoutChangeListener(onLayoutChangeListener);

                fitTextSize();
            } else {
                textView.removeTextChangedListener(textWatcher);
                textView.removeOnLayoutChangeListener(onLayoutChangeListener);

                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }
        }
        return this;
    }

    /**
     * Returns the original text size of the View.
     *
     * @see TextView#getTextSize()
     */
    public float getTextSize() {
        return textSize;
    }

    /**
     * Set the original text size of the View.
     *
     * @see TextView#setTextSize(float)
     */
    public void setTextSize(float size) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * Set the original text size of the View.
     *
     * @see TextView#setTextSize(int, float)
     */
    public void setTextSize(int unit, float size) {
        if (isFitting) {
            // We don't want to update the TextView's actual textSize while we're autofitting
            // since it'd get set to the autofitTextSize
            return;
        }
        Context context = textView.getContext();
        Resources r = Resources.getSystem();

        if (context != null) {
            r = context.getResources();
        }

        setRawTextSize(TypedValue.applyDimension(unit, size, r.getDisplayMetrics()));
    }

    private void setRawTextSize(float size) {
        if (textSize != size) {
            textSize = size;
        }
    }

    private void fitTextSize() {
        float oldTextSize = textView.getTextSize();
        float textSize;

        isFitting = true;
        fitTextSize(textView, textPaint, minTextSize, maxTextSize, maxLines, precision);
        isFitting = false;

        textSize = textView.getTextSize();
        if (textSize != oldTextSize) {
            sendTextSizeChange(textSize, oldTextSize);
        }
    }

    private void sendTextSizeChange(float textSize, float oldTextSize) {
        if (listeners == null) {
            return;
        }

        for (OnTextSizeChangeListener listener : listeners) {
            listener.onTextSizeChange(textSize, oldTextSize);
        }
    }

    private class AutofitTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            // do nothing
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            fitTextSize();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // do nothing
        }
    }

    private class OnFitLayoutChangeListener implements View.OnLayoutChangeListener {
        @Override
        public void onLayoutChange(View view, int left, int top, int right, int bottom,
                                   int oldLeft, int oldTop, int oldRight, int oldBottom) {
            fitTextSize();
        }
    }

    /**
     * When an object of a type is attached to an {@code AutofitHelper}, its methods will be called
     * when the {@code textSize} is changed.
     */
    public interface OnTextSizeChangeListener {
        /**
         * This method is called to notify you that the size of the text has changed to
         * {@code textSize} from {@code oldTextSize}.
         */
        public void onTextSizeChange(float textSize, float oldTextSize);
    }
}
