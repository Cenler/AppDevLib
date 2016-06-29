>>> 说明：
    此包下的布局为Google官方百分比布局类型的扩展，可指定相对宽高百分比设置布局大小，从根本上解决了屏幕适配问题

>>> 使用：（具体参考：https://github.com/hongyangAndroid/android-percent-support-extend）
    - 支持属性：
        app:layout_widthPercent
        app:layout_heightPercent

        app:layout_marginPercent
        app:layout_marginLeftPercent
        app:layout_marginTopPercent
        app:layout_marginRightPercent
        app:layout_marginBottomPercent

        app:layout_marginStartPercent
        app:layout_marginEndPercent

        app:layout_maxWidthPercent
        app:layout_maxHeightPercent
        app:layout_minWidthPercent
        app:layout_minWidthPercent

        app:layout_paddingPercent
        app:layout_paddingBottomPercent="8%w"
        app:layout_paddingLeftPercent="2%w"
        app:layout_paddingRightPercent="4%w"
        app:layout_paddingTopPercent="6%w"

        app:layout_textSizePercent

    - sample：
        <TextView
            android:layout_width="0dp"
            android:layout_height="0dp"
            android:layout_gravity="left|top"
            android:background="#44ff0000"
            android:text="width:30%,height:20%"
            app:layout_heightPercent="20%"
            android:gravity="center"
            app:layout_widthPercent="30%"/>

            app:layout_heightPercent="50%w"
            app:layout_marginPercent="15%w"
            app:layout_marginBottomPercent="20%h"