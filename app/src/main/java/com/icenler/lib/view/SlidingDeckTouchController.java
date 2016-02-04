/*
 * Copyright Txus Ballesteros 2016 (@txusballesteros)
 *
 * This file is part of some open source application.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * Contact: Txus Ballesteros <txus.ballesteros@gmail.com>
 */
package com.icenler.lib.view;

import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public class SlidingDeckTouchController {
    private static final int NO_VIEW = -1;
    private static int SNAP_VELOCITY = 5000;
    private static final int VELOCITY_UNITS = 1000;
    private static final int INITIAL_POSITION = 0;
    private static final int INITIAL_OFFSET = 0;
    private static final int MINIMUM_OFFSET_TO_TRIGGER_MOVEMENT_IN_PX = 20;
    private int accumulatedOffsetX = INITIAL_OFFSET;
    private int accumulatedOffsetY = INITIAL_OFFSET;
    private int initialPositionX = INITIAL_POSITION;
    private int initialPositionY = INITIAL_POSITION;
    private final SlidingDeck ownerView;
    private MotionType motionType = MotionType.UNKNOWN;
    private VelocityTracker velocityTracker;
    private int currentItemView = NO_VIEW;

    enum MotionType {
        UNKNOWN,
        HORIZONTAL,
        VERTICAL
    }

    SlidingDeckTouchController(SlidingDeck ownerView) {
        this.ownerView = ownerView;

        ViewConfiguration config = ViewConfiguration.get(ownerView.getContext());
        SNAP_VELOCITY = (int) (config.getScaledMinimumFlingVelocity() * 1.5);
    }

    boolean onTouchEvent(MotionEvent event) {
        boolean result = false;
        int motionAction = event.getAction();
        switch (motionAction) {
            case MotionEvent.ACTION_DOWN:
                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain();
                } else {
                    velocityTracker.clear();
                }
                velocityTracker.addMovement(event);
                initialPositionX = (int)event.getX();
                initialPositionY = (int)event.getY();
                currentItemView = ownerView.findViewIndexByPosition(initialPositionX, initialPositionY);
                result = true;
                break;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(VELOCITY_UNITS);

                int currentPositionX = (int)event.getX();
                int currentPositionY = (int)event.getY();
                int currentHorizontalOffset = currentPositionX - initialPositionX;
                int currentVerticalOffset = currentPositionY - initialPositionY;
                if (checkMinimumMovementTrigger(currentHorizontalOffset, currentVerticalOffset)) {
                    accumulatedOffsetX += currentHorizontalOffset;
                    accumulatedOffsetY += currentVerticalOffset;
                    if (motionType == MotionType.UNKNOWN) {
                        motionType = getMotionType();
                    }
                    applyOffsets(accumulatedOffsetX, accumulatedOffsetY);
                    initialPositionX = (int)event.getX();
                    initialPositionY = (int)event.getY();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                float xVelocity = velocityTracker.getXVelocity();
                float yVelocity = velocityTracker.getYVelocity();
                if (xVelocity >= SNAP_VELOCITY) {
                    ownerView.performHorizontalSwipe();
                } else if (yVelocity >= SNAP_VELOCITY) {
                    if ((yVelocity > 0 && !ownerView.isExpanded()) || (yVelocity < 0 && ownerView.isExpanded())) {
                        ownerView.performVerticalSwipe();
                    }
                }

                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
                initialPositionX = INITIAL_POSITION;
                initialPositionY = INITIAL_POSITION;
                accumulatedOffsetX = INITIAL_OFFSET;
                accumulatedOffsetY = INITIAL_OFFSET;
                motionType = MotionType.UNKNOWN;
                ownerView.performReleaseTouch();
                break;
        }
        return result;
    }

    private void applyOffsets(int horizontalOffset, int verticalOffset) {
        if (motionType == MotionType.HORIZONTAL) {
            applyHorizontalMotion(horizontalOffset);
        } else if (motionType == MotionType.VERTICAL) {
            applyVerticalMotion(verticalOffset);
        }
    }

    private MotionType getMotionType() {
        MotionType result = MotionType.HORIZONTAL;
        if (Math.abs(accumulatedOffsetY) > Math.abs(accumulatedOffsetX)) {
            result = MotionType.VERTICAL;
        }
        return result;
    }

    private boolean checkMinimumMovementTrigger(int currentHorizontalOffset, int currentVerticalOffset) {
        return Math.abs(currentHorizontalOffset) >= MINIMUM_OFFSET_TO_TRIGGER_MOVEMENT_IN_PX ||
               Math.abs(currentVerticalOffset) >= MINIMUM_OFFSET_TO_TRIGGER_MOVEMENT_IN_PX ||
               Math.abs(accumulatedOffsetX) >= MINIMUM_OFFSET_TO_TRIGGER_MOVEMENT_IN_PX ||
               Math.abs(accumulatedOffsetY) >= MINIMUM_OFFSET_TO_TRIGGER_MOVEMENT_IN_PX;
    }

    private void applyHorizontalMotion(int offset) {
        if (currentItemView != NO_VIEW) {
            ownerView.setOffsetLeftRight(currentItemView, offset);
        }
    }

    private void applyVerticalMotion(int offset) {
        ownerView.setOffsetTopBottom(offset);
    }
}
