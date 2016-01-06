/**************************************************************************************************/
/**
 * >>> Shader 之图形渲染
 *     - BitmapShader:      图像渲染
 *     - LinearGradient:    线性渐变
 *     - RadialGradient:    环形渐变
 *     - SweepGradient:     扇形渐变
 *     - ComposeShader:     混合渲染，适用于组合操作
 *     使用： mPaint.setShader(XXXShader)
 *
 *     Example：配合 Matrix 实现扇形动态渐变
 *
         Paint paint = new Paint();
         paint.setAntiAlias(true);
         paint.setStrokeWidth(8);
         paint.setStyle(Paint.Style.STROKE);

         int[] f = {Color.parseColor("#00A8D7A7"), Color.parseColor("#ffA8D7A7")};
         float[] p = {.0f, 1.0f};

         SweepGradient sweepGradient = new SweepGradient(rectF.centerX(), rectF.centerX(), f, p);
         Matrix matrix = new Matrix();
         sweepGradient.getLocalMatrix(matrix);
         matrix.postRotate(startAngle, rectF.centerX(), rectF.centerY());
         sweepGradient.setLocalMatrix(matrix);
         paint.setShader(sweepGradient);

         canvas.drawArc(rectF,0, 360, true, paint);
 * */


/**
 * >>> ViewConfiguration 之控件常用配置
 *      - 使用：ViewConfiguration.get(context)
 *
 * >>> VelocityTracker 之手势速度检测
 *      - 使用：VelocityTracker.obtain().addMovement(event)
 *
 * >>> Scroller 之模拟滑动
 *      - 使用：配合 computeScroll() + scrollTo(x, y) 实现控件滑动效果
 *          Scroller.startScroll(startX, startY, dx, dy, duration)
 *
 * >>> Matrix 之矩阵变换
 *      - 使用：配合 Canvas 使用
 *          Canvas.drawBitmap(bitmap, matrix, null)
 *      {MSCALE_X, MSKEW_X,    MTRANS_X,
 *       MSKEW_Y,  MSCALE_Y,   MTRANS_Y,
 *       MPERSP_0, MPERSP_1,   MPERSP_2}
 *          MSCALE： 用于处理缩放变换
 *          MSKEW：  用于处理错切变换
 *          MTRANS： 用于处理平移变换
 *          MPERSP： 用于处理透视变换
 *
 * >>> ColorMatrix 之颜色变换
 *      - 使用：配合 Paint 使用实现灰度
 *      Example：
 *
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            Paint paint = new Paint();
            paint.setColorFilter(new ColorMatrixColorFilter(matrix));
            View.setLayerType(View.LAYER_TYPE_HARDWARE, paint);
 *
 * */


/**
 * >>> MeasureSpec 之控件测量
 *      * MeasureSpec 由大小和模式组成
 *      - AT_MOST：      对应 wrap_content
 *      - EXACTLY：      对应 match_parent
 *      - UNSPECIFIED：  具体数值
 *
 *    Example:

          protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
              // 声明一个临时变量来存储计算出的测量值
              int resultWidth = 0;

              // 获取宽度测量规格中的mode
              int modeWidth = MeasureSpec.getMode(widthMeasureSpec);

              // 获取宽度测量规格中的size
              int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);

              if (modeWidth == MeasureSpec.EXACTLY) {
                  resultWidth = sizeWidth;
              } else {
                  resultWidth = mBitmap.getWidth();
                  if (modeWidth == MeasureSpec.AT_MOST)
                      resultWidth = Math.min(resultWidth, sizeWidth);
              }

              int resultHeight = 0;
              int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
              int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

              if (modeHeight == MeasureSpec.EXACTLY) {
                  resultHeight = sizeHeight;
              } else {
                  resultHeight = mBitmap.getHeight();
                      if (modeHeight == MeasureSpec.AT_MOST) {
                          resultHeight = Math.min(resultHeight, sizeHeight);
                  }
              }

              // 设置测量尺寸
              setMeasuredDimension(resultWidth, resultHeight);
          }
 * */

/**
 * SimpleDraweeView 属性使用：
     com.facebook.drawee.view.SimpleDraweeView
     android:id="@+id/my_image_view"
     android:layout_width="20dp"
     android:layout_height="20dp"
     fresco:fadeDuration="300"

     //设置图片的缩放类型
     fresco:actualImageScaleType="focusCrop"

     //设置图片加载成功前显示的图片也可以是背景色
     fresco:placeholderImage="@color/wait_color"
     fresco:placeholderImageScaleType="fitCenter"

     //图片加载失败的时候显示的图片
     fresco:failureImage="@drawable/error"
     fresco:failureImageScaleType="centerInside"

     //可设置加载失败点击重新加载，这是重新加载失败显示的图片
     fresco:retryImage="@drawable/retrying"
     fresco:retryImageScaleType="centerCrop"

     //显示进度条
     fresco:progressBarImage="@drawable/progress_bar"
     fresco:progressBarImageScaleType="centerInside"
     fresco:progressBarAutoRotateInterval="1000"

     //设置背景图的可以使颜色和图片
     fresco:backgroundImage="@color/blue"

     //设置图片加载完成覆盖的图片
     fresco:overlayImage="@drawable/watermark"
     fresco:pressedStateOverlayImage="@color/red"

     //将图片设置为圆形
     fresco:roundAsCircle="false"

     //设置图片4个角的圆角半径
     fresco:roundedCornerRadius="1dp"
     fresco:roundTopLeft="true"
     fresco:roundTopRight="false"
     fresco:roundBottomLeft="false"
     fresco:roundBottomRight="true"
     fresco:roundWithOverlayColor="@color/corner_color"
     fresco:roundingBorderWidth="2dp"
     fresco:roundingBorderColor="@color/border_color"
 * */

/**
 * >>> ColorMatrixColorFilter\LightingColorFilter\PorterDuffColorFilter 之颜色过滤器
 *      - ColorMatrixColorFilter：   色彩矩阵颜色过滤器
 *      - LightingColorFilter：
 *          @色彩倍增: 可用于颜色过滤
 *          @色彩添加: 光照染色
 *      - PorterDuffColorFilter：
 *          @混合色值:
 *          @混合模式:
 *      使用：配合 Paint 实现滤镜效果
 *
 * >>> Xfermode AvoidXfermode\PixelXorXfermode\PorterDuffXfermode 之图像混合模式
 *      - AvoidXfermode:
 *          @颜色通道:
 *          @容差值:
 *          @混合模式:
 *              # AvoidXfermode.Mode.TARGET:颜色通道渲染
 *              # AvoidXfermode.Mode.AVOID:非颜色通道渲染
 *      - PorterDuffXfermode
 *          用法：
 *          @混合模式: 参考 mipmap-hdpi/img_proter_duff_xfermode.jpg
 *              # ProterDuff.Mode.CLEAR     : 清除
 *              # ProterDuff.Mode.DARKEN    ：变暗，深色（Android）覆盖
 *              # ProterDuff.Mode.LIGHTEN   ：变亮，浅色（Android）覆盖
 *              # ProterDuff.Mode.DST       ：只绘制目标
 *              # ProterDuff.Mode.DST_ATOP  ：不相交绘制源图，相交绘制目标
 *              # ProterDuff.Mode.DST_IN    ：相交处绘制目标
 *              # ProterDuff.Mode.DST_OUT   ：不相交处绘制目标
 *              # ProterDuff.Mode.DST_OVER  ：源图上绘制目标
 *              # ProterDuff.Mode.SRC       ：只绘制源图
 *              # ProterDuff.Mode.SRC_ATOP  ：不相交绘制目标，相交绘制源图
 *              # ProterDuff.Mode.SRC_IN    ：相交处绘制源图
 *              # ProterDuff.Mode.SRC_OUT   ：不相交处绘制源图
 *              # ProterDuff.Mode.SRC_OVER  ：目标图上绘制源图
 *              # ProterDuff.Mode.MULTIPLY  ：正片叠底
 *              # ProterDuff.Mode.XOR       ：相交打孔
 *              # ProterDuff.Mode.ADD       ：饱和相加
 *              # ProterDuff.Mode.SCREEN    ：滤色，柔化作用
 *              # ProterDuff.Mode.OVERLAY   ：叠加中和
 *      - PixelXorXfermode: deprecated
 * */
/**************************************************************************************************/