package com.icenler.lib.view.guideview;

import android.view.View;
import android.view.WindowManager;

public class Hint {

    public enum Anchor {
        ANCHOR_LEFT, ANCHOR_RIGHT,
        ANCHOR_TOP, ANCHOR_BOTTOM, ANCHOR_OVER
    }

    public enum Gravity {
        GRAVITY_START, GRAVITY_END, GRAVITY_CENTER
    }

    View hintView;

    Anchor anchor;

    Gravity gravity;

    int absX;

    int absY;

    int offsetX;

    int offsetY;

    int width;

    int height;

    int maskAnchorId;

    public static Builder builder() {
        return new Builder();
    }

    private Hint(Builder builder) {
        this.hintView = builder.hintView;
        this.anchor = builder.anchor;
        this.gravity = builder.gravity;
        this.absX = builder.absX;
        this.absY = builder.absY;
        this.offsetX = builder.offsetX;
        this.offsetY = builder.offsetY;
        this.width = builder.width;
        this.height = builder.height;
        this.maskAnchorId = builder.maskAnchorId;
    }

    public static final class Builder {

        private View hintView;

        private Anchor anchor = Anchor.ANCHOR_OVER;

        private Gravity gravity = Gravity.GRAVITY_START;

        private int absX;

        private int absY;

        private int offsetX;

        private int offsetY;

        private int width = WindowManager.LayoutParams.WRAP_CONTENT;

        private int height = WindowManager.LayoutParams.WRAP_CONTENT;

        private int maskAnchorId = View.NO_ID;

        private Builder() {
        }

        public Builder setHintView(View hintView) {
            this.hintView = hintView;
            return this;
        }

        public Builder setAnchor(Anchor anchor) {
            this.anchor = anchor;
            return this;
        }

        public Builder setGravity(Gravity gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setAbsX(int x) {
            this.absX = x;
            return this;
        }

        public Builder setAbsY(int y) {
            this.absY = y;
            return this;
        }

        public Builder setOffsetX(int x) {
            this.offsetX = x;
            return this;
        }

        public Builder setOffsetY(int y) {
            this.offsetY = y;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setLayoutParams(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder setMaskAnchorId(int maskAnchorId) {
            this.maskAnchorId = maskAnchorId;
            return this;
        }

        public Hint build() {
            return new Hint(this);
        }
    }

}