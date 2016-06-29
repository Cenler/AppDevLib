NestedScrollingChild                                 -> NestedScrollingParent


void setNestedScrollingEnabled(boolean enabled)

boolean isNestedScrollingEnabled()

boolean hasNestedScrollingParent()                      int getNestedScrollAxes()


boolean startNestedScroll(int axes)                     boolean onStartNestedScroll(
                                                                View child,
                                                                View target,
                                                                int nestedScrollAxes)
                                                        void onNestedScrollAccepted(
                                                                View child,
                                                                View target,
                                                                int nestedScrollAxes)


void stopNestedScroll()                                 void onStopNestedScroll(View target)


boolean dispatchNestedPreScroll(                        void onNestedPreScroll(
            int dx, int dy,                                     View target,
            int[] consumed,                                     int dx, int dy,
            int[] offsetInWindow)                               int[] consumed)


boolean dispatchNestedScroll(                           void onNestedScroll(
            int dxConsumed, int dyConsumed,                     View target,
            int dxUnconsumed, int dyUnconsumed,                 int dxConsumed, int dyConsumed,
            int[] offsetInWindow)                               int dxUnconsumed, int dyUnconsumed)


boolean dispatchNestedFling(                            boolean onNestedFling);
            float velocityX, float velocityY,                   View target,
            boolean consumed)                                   float velocityX, float velocityY,
                                                                boolean consumed)

boolean dispatchNestedPreFling(                         boolean onNestedPreFling(
            float velocityX, float velocityY);                  View target,
                                                                float velocityX, float velocityY)