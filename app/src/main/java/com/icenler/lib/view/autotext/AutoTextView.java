package com.icenler.lib.view.autotext;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * A {@link TextView} that re-sizes its text to be no larger than the width of the view.
 *
 * @attr ref R.styleable.AutoTextView_sizeToFit
 * @attr ref R.styleable.AutoTextView_minTextSize
 * @attr ref R.styleable.AutoTextView_precision
 */
public class AutoTextView extends TextView implements AutoTextHelper.OnTextSizeChangeListener {

    private AutoTextHelper autoTextHelper;

    public AutoTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public AutoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public AutoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        autoTextHelper = AutoTextHelper.create(this, attrs, defStyle)
                .addOnTextSizeChangeListener(this);
    }

    // Getters and Setters

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        if (autoTextHelper != null) {
            autoTextHelper.setTextSize(unit, size);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLines(int lines) {
        super.setLines(lines);
        if (autoTextHelper != null) {
            autoTextHelper.setMaxLines(lines);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMaxLines(int maxLines) {
        super.setMaxLines(maxLines);
        if (autoTextHelper != null) {
            autoTextHelper.setMaxLines(maxLines);
        }
    }

    /**
     * Returns the {@link AutoTextHelper} for this View.
     */
    public AutoTextHelper getAutoTextHelper() {
        return autoTextHelper;
    }

    /**
     * Returns whether or not the text will be automatically re-sized to fit its constraints.
     */
    public boolean isFitTextSize() {
        return autoTextHelper.isEnabled();
    }

    /**
     * Sets the property of this field (sizeToFit), to automatically resize the text to fit its
     * constraints.
     */
    public void setFitTextSize() {
        setFitTextSize(true);
    }

    /**
     * If true, the text will automatically be re-sized to fit its constraints; if false, it will
     * act like a normal TextView.
     *
     * @param fitTextSize
     */
    public void setFitTextSize(boolean fitTextSize) {
        autoTextHelper.setIsEnabled(fitTextSize);
    }

    /**
     * Returns the maximum size (in pixels) of the text in this View.
     */
    public float getMaxTextSize() {
        return autoTextHelper.getMaxTextSize();
    }

    /**
     * Set the maximum text size to the given value, interpreted as "scaled pixel" units. This size
     * is adjusted based on the current density and user font size preference.
     *
     * @param size The scaled pixel size.
     *
     * @attr ref android.R.styleable#TextView_textSize
     */
    public void setMaxTextSize(float size) {
        autoTextHelper.setMaxTextSize(size);
    }

    /**
     * Set the maximum text size to a given unit and value. See TypedValue for the possible
     * dimension units.
     *
     * @param unit The desired dimension unit.
     * @param size The desired size in the given units.
     *
     * @attr ref android.R.styleable#TextView_textSize
     */
    public void setMaxTextSize(int unit, float size) {
        autoTextHelper.setMaxTextSize(unit, size);
    }

    /**
     * Returns the minimum size (in pixels) of the text in this View.
     */
    public float getMinTextSize() {
        return autoTextHelper.getMinTextSize();
    }

    /**
     * Set the minimum text size to the given value, interpreted as "scaled pixel" units. This size
     * is adjusted based on the current density and user font size preference.
     *
     * @param minSize The scaled pixel size.
     *
     * @attr ref R.styleable.AutoTextView_minTextSize
     */
    public void setMinTextSize(int minSize) {
        autoTextHelper.setMinTextSize(TypedValue.COMPLEX_UNIT_SP, minSize);
    }

    /**
     * Set the minimum text size to a given unit and value. See TypedValue for the possible
     * dimension units.
     *
     * @param unit The desired dimension unit.
     * @param minSize The desired size in the given units.
     *
     * @attr ref R.styleable.AutoTextView_minTextSize
     */
    public void setMinTextSize(int unit, float minSize) {
        autoTextHelper.setMinTextSize(unit, minSize);
    }

    /**
     * Returns the amount of precision used to calculate the correct text size to fit within its
     * bounds.
     */
    public float getPrecision() {
        return autoTextHelper.getPrecision();
    }

    /**
     * Set the amount of precision used to calculate the correct text size to fit within its
     * bounds. Lower precision is more precise and takes more time.
     *
     * @param precision The amount of precision.
     */
    public void setPrecision(float precision) {
        autoTextHelper.setPrecision(precision);
    }

    @Override
    public void onTextSizeChange(float textSize, float oldTextSize) {
        // do nothing
    }
}
