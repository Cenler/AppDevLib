package com.icenler.lib.view.guideview;

import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.view.View;

public class Mask {

    public enum Shape {
        CIRCLE, RECTANGLE
    }

    Rect targetRect;

    Shape shapeStyle;

    int radiusSize;

    int cornerSize;

    int borderSize;

    int borderColor;

    int paddingLeft;

    int paddingRight;

    int paddingTop;

    int paddingBottom;

    int maskId;

    int offsetX;

    int offsetY;

    public static Mask.Builder builder() {
        return new Mask.Builder();
    }

    private Mask(Mask.Builder builder) {
        this.targetRect = builder.rect;
        this.shapeStyle = builder.shape;
        this.radiusSize = builder.radius;
        this.cornerSize = builder.corner;
        this.borderSize = builder.border;
        this.borderColor = builder.borderColor;
        this.paddingLeft = builder.left;
        this.paddingRight = builder.right;
        this.paddingTop = builder.top;
        this.paddingBottom = builder.bottom;
        this.maskId = builder.maskId;
        this.offsetX = builder.offsetX;
        this.offsetY = builder.offsetY;
    }

    public static final class Builder {

        private Rect rect;

        private Shape shape;

        private int radius;

        private int corner;

        private int border;

        private int borderColor;

        private int left;

        private int right;

        private int top;

        private int bottom;

        private int maskId = View.NO_ID;

        private int offsetX;

        private int offsetY;

        private Builder() {
        }

        public Mask.Builder setMaskRect(Rect targetRect) {
            this.rect = targetRect;
            return this;
        }

        public Mask.Builder setShapeStyle(Shape shapeStyle) {
            this.shape = shapeStyle;
            return this;
        }

        public Mask.Builder setRadius(int radius) {
            this.radius = radius;
            return this;
        }

        public Mask.Builder setCorner(int corner) {
            this.corner = corner;
            return this;
        }

        public Mask.Builder setBorder(int border) {
            this.border = border;
            return this;
        }

        public Mask.Builder setBorder(int border, @ColorInt int color) {
            this.border = border;
            this.borderColor = color;
            return this;
        }

        public Mask.Builder setBorderColor(@ColorInt int color) {
            this.borderColor = color;
            return this;
        }

        public Mask.Builder setPaddingLeft(int left) {
            this.left = left;
            return this;
        }

        public Mask.Builder setPaddingTop(int top) {
            this.top = top;
            return this;
        }

        public Mask.Builder setPaddingRight(int right) {
            this.right = right;
            return this;
        }

        public Mask.Builder setPaddingBottom(int bottom) {
            this.bottom = bottom;
            return this;
        }

        public Mask.Builder setPadding(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
            return this;
        }

        public Mask.Builder setMaskId(int maskId) {
            this.maskId = maskId;
            return this;
        }

        public Mask.Builder setOffsetX(int offsetX) {
            this.offsetX = offsetX;
            return this;
        }

        public Mask.Builder setOffsetY(int offsetY) {
            this.offsetY = offsetY;
            return this;
        }

        public Mask build() {
            return new Mask(this);
        }
    }

}