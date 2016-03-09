/**************************************************************************************************
 * >>> Shader 之图形渲染
 * - BitmapShader:      图像渲染
 * - LinearGradient:    线性渐变
 * - RadialGradient:    环形渐变
 * - SweepGradient:     扇形渐变
 * - ComposeShader:     混合渲染，适用于组合操作
 * 使用： mPaint.setShader(XXXShader)
 * <p/>
 * Example：配合 Matrix 实现扇形动态渐变
 * <p/>
 * Paint paint = new Paint();
 * paint.setAntiAlias(true);
 * paint.setStrokeWidth(8);
 * paint.setStyle(Paint.Style.STROKE);
 * <p/>
 * int[] f = {Color.parseColor("#00A8D7A7"), Color.parseColor("#ffA8D7A7")};
 * float[] p = {.0f, 1.0f};
 * <p/>
 * SweepGradient sweepGradient = new SweepGradient(rectF.centerX(), rectF.centerX(), f, p);
 * Matrix matrix = new Matrix();
 * sweepGradient.getLocalMatrix(matrix);
 * matrix.postRotate(startAngle, rectF.centerX(), rectF.centerY());
 * sweepGradient.setLocalMatrix(matrix);
 * paint.setShader(sweepGradient);
 * <p/>
 * canvas.drawArc(rectF,0, 360, true, paint);
 * <p/>
 * >>> ViewConfiguration 之控件常用配置
 * - 使用：ViewConfiguration.get(context)
 * <p/>
 * >>> VelocityTracker 之手势速度检测
 * - 使用：VelocityTracker.obtain().addMovement(event)
 * <p/>
 * >>> Scroller 之模拟滑动
 * - 使用：配合 computeScroll() + scrollTo(x, y) 实现控件滑动效果
 * Scroller.startScroll(startX, startY, dx, dy, duration)
 * <p/>
 * >>> Matrix 之矩阵变换
 * - 使用：配合 Canvas 使用
 * Canvas.drawBitmap(bitmap, matrix, null)
 * {MSCALE_X, MSKEW_X,    MTRANS_X,
 * MSKEW_Y,  MSCALE_Y,   MTRANS_Y,
 * MPERSP_0, MPERSP_1,   MPERSP_2}
 * MSCALE： 用于处理缩放变换
 * MSKEW：  用于处理错切变换
 * MTRANS： 用于处理平移变换
 * MPERSP： 用于处理透视变换
 * <p/>
 * >>> ColorMatrix 之颜色变换
 * - 使用：配合 Paint 使用实现灰度
 * Example：
 * <p/>
 * ColorMatrix matrix = new ColorMatrix();
 * matrix.setSaturation(0);
 * Paint paint = new Paint();
 * paint.setColorFilter(new ColorMatrixColorFilter(matrix));
 * View.setLayerType(View.LAYER_TYPE_HARDWARE, paint);
 * <p/>
 * <p/>
 * >>> MeasureSpec 之控件测量
 * * MeasureSpec 由大小和模式组成
 * - AT_MOST：      对应 wrap_content
 * - EXACTLY：      对应 match_parent
 * - UNSPECIFIED：  具体数值
 * <p/>
 * Example:
 * <p/>
 * protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
 * // 声明一个临时变量来存储计算出的测量值
 * int resultWidth = 0;
 * <p/>
 * // 获取宽度测量规格中的mode
 * int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
 * <p/>
 * // 获取宽度测量规格中的size
 * int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
 * <p/>
 * if (modeWidth == MeasureSpec.EXACTLY) {
 * resultWidth = sizeWidth;
 * } else {
 * resultWidth = mBitmap.getWidth();
 * if (modeWidth == MeasureSpec.AT_MOST)
 * resultWidth = Math.min(resultWidth, sizeWidth);
 * }
 * <p/>
 * int resultHeight = 0;
 * int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
 * int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
 * <p/>
 * if (modeHeight == MeasureSpec.EXACTLY) {
 * resultHeight = sizeHeight;
 * } else {
 * resultHeight = mBitmap.getHeight();
 * if (modeHeight == MeasureSpec.AT_MOST) {
 * resultHeight = Math.min(resultHeight, sizeHeight);
 * }
 * }
 * <p/>
 * // 设置测量尺寸
 * setMeasuredDimension(resultWidth, resultHeight);
 * }
 * <p/>
 * SimpleDraweeView 属性使用：
 * com.facebook.drawee.view.SimpleDraweeView
 * android:id="@+id/my_image_view"
 * android:layout_width="20dp"
 * android:layout_height="20dp"
 * fresco:fadeDuration="300"
 * <p/>
 * //设置图片的缩放类型
 * fresco:actualImageScaleType="focusCrop"
 * <p/>
 * //设置图片加载成功前显示的图片也可以是背景色
 * fresco:placeholderImage="@color/wait_color"
 * fresco:placeholderImageScaleType="fitCenter"
 * <p/>
 * //图片加载失败的时候显示的图片
 * fresco:failureImage="@drawable/error"
 * fresco:failureImageScaleType="centerInside"
 * <p/>
 * //可设置加载失败点击重新加载，这是重新加载失败显示的图片
 * fresco:retryImage="@drawable/retrying"
 * fresco:retryImageScaleType="centerCrop"
 * <p/>
 * //显示进度条
 * fresco:progressBarImage="@drawable/progress_bar"
 * fresco:progressBarImageScaleType="centerInside"
 * fresco:progressBarAutoRotateInterval="1000"
 * <p/>
 * //设置背景图的可以使颜色和图片
 * fresco:backgroundImage="@color/blue"
 * <p/>
 * //设置图片加载完成覆盖的图片
 * fresco:overlayImage="@drawable/watermark"
 * fresco:pressedStateOverlayImage="@color/red"
 * <p/>
 * //将图片设置为圆形
 * fresco:roundAsCircle="false"
 * <p/>
 * //设置图片4个角的圆角半径
 * fresco:roundedCornerRadius="1dp"
 * fresco:roundTopLeft="true"
 * fresco:roundTopRight="false"
 * fresco:roundBottomLeft="false"
 * fresco:roundBottomRight="true"
 * fresco:roundWithOverlayColor="@color/corner_color"
 * fresco:roundingBorderWidth="2dp"
 * fresco:roundingBorderColor="@color/border_color"
 * <p/>
 * >>> ColorMatrixColorFilter\LightingColorFilter\PorterDuffColorFilter 之颜色过滤器
 * - ColorMatrixColorFilter：   色彩矩阵颜色过滤器
 * - LightingColorFilter：
 *
 * @色彩倍增: 可用于颜色过滤
 * @色彩添加: 光照染色
 * - PorterDuffColorFilter：
 * @混合色值:
 * @混合模式: 使用：配合 Paint 实现滤镜效果
 * <p/>
 * >>> Xfermode AvoidXfermode\PixelXorXfermode\PorterDuffXfermode 之图像混合模式
 * - AvoidXfermode:
 * @颜色通道:
 * @容差值:
 * @混合模式: # AvoidXfermode.Mode.TARGET:颜色通道渲染
 * # AvoidXfermode.Mode.AVOID:非颜色通道渲染
 * - PorterDuffXfermode
 * 用法：
 * @混合模式: 参考 mipmap-hdpi/img_proter_duff_xfermode.jpg
 * # ProterDuff.Mode.CLEAR     : 清除
 * # ProterDuff.Mode.DARKEN    ：变暗，深色（Android）覆盖
 * # ProterDuff.Mode.LIGHTEN   ：变亮，浅色（Android）覆盖
 * # ProterDuff.Mode.DST       ：只绘制目标
 * # ProterDuff.Mode.DST_ATOP  ：不相交绘制源图，相交绘制目标
 * # ProterDuff.Mode.DST_IN    ：相交处绘制目标
 * # ProterDuff.Mode.DST_OUT   ：不相交处绘制目标
 * # ProterDuff.Mode.DST_OVER  ：源图上绘制目标
 * # ProterDuff.Mode.SRC       ：只绘制源图
 * # ProterDuff.Mode.SRC_ATOP  ：不相交绘制目标，相交绘制源图
 * # ProterDuff.Mode.SRC_IN    ：相交处绘制源图
 * # ProterDuff.Mode.SRC_OUT   ：不相交处绘制源图
 * # ProterDuff.Mode.SRC_OVER  ：目标图上绘制源图
 * # ProterDuff.Mode.MULTIPLY  ：正片叠底
 * # ProterDuff.Mode.XOR       ：相交打孔
 * # ProterDuff.Mode.ADD       ：饱和相加
 * # ProterDuff.Mode.SCREEN    ：滤色，柔化作用
 * # ProterDuff.Mode.OVERLAY   ：叠加中和
 * - PixelXorXfermode: deprecated
 * <p/>
 * >>> 贝塞尔曲线插值器生成器
 * Path path = new Path();
 * path.lineTo();// 一阶
 * path.quadTo();// 二阶阶
 * path.cubicTo();// 三阶
 * PathInterpolatorCompat.create(new Path().quadTo());
 * <p/>
 * >>> compile(name:'camerascan-1.0', ext:'aar') aar包引入
 * <p/>
 * /**
 * >>> Shader 之图形渲染
 * - BitmapShader:      图像渲染
 * - LinearGradient:    线性渐变
 * - RadialGradient:    环形渐变
 * - SweepGradient:     扇形渐变
 * - ComposeShader:     混合渲染，适用于组合操作
 * 使用： mPaint.setShader(XXXShader)
 * <p/>
 * Example：配合 Matrix 实现扇形动态渐变
 * <p/>
 * Paint paint = new Paint();
 * paint.setAntiAlias(true);
 * paint.setStrokeWidth(8);
 * paint.setStyle(Paint.Style.STROKE);
 * <p/>
 * int[] f = {Color.parseColor("#00A8D7A7"), Color.parseColor("#ffA8D7A7")};
 * float[] p = {.0f, 1.0f};
 * <p/>
 * SweepGradient sweepGradient = new SweepGradient(rectF.centerX(), rectF.centerX(), f, p);
 * Matrix matrix = new Matrix();
 * sweepGradient.getLocalMatrix(matrix);
 * matrix.postRotate(startAngle, rectF.centerX(), rectF.centerY());
 * sweepGradient.setLocalMatrix(matrix);
 * paint.setShader(sweepGradient);
 * <p/>
 * canvas.drawArc(rectF,0, 360, true, paint);
 * <p/>
 * >>> ViewConfiguration 之控件常用配置
 * - 使用：ViewConfiguration.get(context)
 * <p/>
 * >>> VelocityTracker 之手势速度检测
 * - 使用：VelocityTracker.obtain().addMovement(event)
 * <p/>
 * >>> Scroller 之模拟滑动
 * - 使用：配合 computeScroll() + scrollTo(x, y) 实现控件滑动效果
 * Scroller.startScroll(startX, startY, dx, dy, duration)
 * <p/>
 * >>> Matrix 之矩阵变换
 * - 使用：配合 Canvas 使用
 * Canvas.drawBitmap(bitmap, matrix, null)
 * {MSCALE_X, MSKEW_X,    MTRANS_X,
 * MSKEW_Y,  MSCALE_Y,   MTRANS_Y,
 * MPERSP_0, MPERSP_1,   MPERSP_2}
 * MSCALE： 用于处理缩放变换
 * MSKEW：  用于处理错切变换
 * MTRANS： 用于处理平移变换
 * MPERSP： 用于处理透视变换
 * <p/>
 * >>> ColorMatrix 之颜色变换
 * - 使用：配合 Paint 使用实现灰度
 * Example：
 * <p/>
 * ColorMatrix matrix = new ColorMatrix();
 * matrix.setSaturation(0);
 * Paint paint = new Paint();
 * paint.setColorFilter(new ColorMatrixColorFilter(matrix));
 * View.setLayerType(View.LAYER_TYPE_HARDWARE, paint);
 * <p/>
 * <p/>
 * >>> MeasureSpec 之控件测量
 * * MeasureSpec 由大小和模式组成
 * - AT_MOST：      对应 wrap_content
 * - EXACTLY：      对应 match_parent
 * - UNSPECIFIED：  具体数值
 * <p/>
 * Example:
 * <p/>
 * protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
 * // 声明一个临时变量来存储计算出的测量值
 * int resultWidth = 0;
 * <p/>
 * // 获取宽度测量规格中的mode
 * int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
 * <p/>
 * // 获取宽度测量规格中的size
 * int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
 * <p/>
 * if (modeWidth == MeasureSpec.EXACTLY) {
 * resultWidth = sizeWidth;
 * } else {
 * resultWidth = mBitmap.getWidth();
 * if (modeWidth == MeasureSpec.AT_MOST)
 * resultWidth = Math.min(resultWidth, sizeWidth);
 * }
 * <p/>
 * int resultHeight = 0;
 * int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
 * int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
 * <p/>
 * if (modeHeight == MeasureSpec.EXACTLY) {
 * resultHeight = sizeHeight;
 * } else {
 * resultHeight = mBitmap.getHeight();
 * if (modeHeight == MeasureSpec.AT_MOST) {
 * resultHeight = Math.min(resultHeight, sizeHeight);
 * }
 * }
 * <p/>
 * // 设置测量尺寸
 * setMeasuredDimension(resultWidth, resultHeight);
 * }
 * <p/>
 * SimpleDraweeView 属性使用：
 * com.facebook.drawee.view.SimpleDraweeView
 * android:id="@+id/my_image_view"
 * android:layout_width="20dp"
 * android:layout_height="20dp"
 * fresco:fadeDuration="300"
 * <p/>
 * //设置图片的缩放类型
 * fresco:actualImageScaleType="focusCrop"
 * <p/>
 * //设置图片加载成功前显示的图片也可以是背景色
 * fresco:placeholderImage="@color/wait_color"
 * fresco:placeholderImageScaleType="fitCenter"
 * <p/>
 * //图片加载失败的时候显示的图片
 * fresco:failureImage="@drawable/error"
 * fresco:failureImageScaleType="centerInside"
 * <p/>
 * //可设置加载失败点击重新加载，这是重新加载失败显示的图片
 * fresco:retryImage="@drawable/retrying"
 * fresco:retryImageScaleType="centerCrop"
 * <p/>
 * //显示进度条
 * fresco:progressBarImage="@drawable/progress_bar"
 * fresco:progressBarImageScaleType="centerInside"
 * fresco:progressBarAutoRotateInterval="1000"
 * <p/>
 * //设置背景图的可以使颜色和图片
 * fresco:backgroundImage="@color/blue"
 * <p/>
 * //设置图片加载完成覆盖的图片
 * fresco:overlayImage="@drawable/watermark"
 * fresco:pressedStateOverlayImage="@color/red"
 * <p/>
 * //将图片设置为圆形
 * fresco:roundAsCircle="false"
 * <p/>
 * //设置图片4个角的圆角半径
 * fresco:roundedCornerRadius="1dp"
 * fresco:roundTopLeft="true"
 * fresco:roundTopRight="false"
 * fresco:roundBottomLeft="false"
 * fresco:roundBottomRight="true"
 * fresco:roundWithOverlayColor="@color/corner_color"
 * fresco:roundingBorderWidth="2dp"
 * fresco:roundingBorderColor="@color/border_color"
 * <p/>
 * >>> ColorMatrixColorFilter\LightingColorFilter\PorterDuffColorFilter 之颜色过滤器
 * - ColorMatrixColorFilter：   色彩矩阵颜色过滤器
 * - LightingColorFilter：
 * @色彩倍增: 可用于颜色过滤
 * @色彩添加: 光照染色
 * - PorterDuffColorFilter：
 * @混合色值:
 * @混合模式: 使用：配合 Paint 实现滤镜效果
 * <p/>
 * >>> Xfermode AvoidXfermode\PixelXorXfermode\PorterDuffXfermode 之图像混合模式
 * - AvoidXfermode:
 * @颜色通道:
 * @容差值:
 * @混合模式: # AvoidXfermode.Mode.TARGET:颜色通道渲染
 * # AvoidXfermode.Mode.AVOID:非颜色通道渲染
 * - PorterDuffXfermode
 * 用法：
 * @混合模式: 参考 mipmap-hdpi/img_proter_duff_xfermode.jpg
 * # ProterDuff.Mode.CLEAR     : 清除
 * # ProterDuff.Mode.DARKEN    ：变暗，深色（Android）覆盖
 * # ProterDuff.Mode.LIGHTEN   ：变亮，浅色（Android）覆盖
 * # ProterDuff.Mode.DST       ：只绘制目标
 * # ProterDuff.Mode.DST_ATOP  ：不相交绘制源图，相交绘制目标
 * # ProterDuff.Mode.DST_IN    ：相交处绘制目标
 * # ProterDuff.Mode.DST_OUT   ：不相交处绘制目标
 * # ProterDuff.Mode.DST_OVER  ：源图上绘制目标
 * # ProterDuff.Mode.SRC       ：只绘制源图
 * # ProterDuff.Mode.SRC_ATOP  ：不相交绘制目标，相交绘制源图
 * # ProterDuff.Mode.SRC_IN    ：相交处绘制源图
 * # ProterDuff.Mode.SRC_OUT   ：不相交处绘制源图
 * # ProterDuff.Mode.SRC_OVER  ：目标图上绘制源图
 * # ProterDuff.Mode.MULTIPLY  ：正片叠底
 * # ProterDuff.Mode.XOR       ：相交打孔
 * # ProterDuff.Mode.ADD       ：饱和相加
 * # ProterDuff.Mode.SCREEN    ：滤色，柔化作用
 * # ProterDuff.Mode.OVERLAY   ：叠加中和
 * - PixelXorXfermode: deprecated
 * <p/>
 * >>> 贝塞尔曲线插值器生成器
 * Path path = new Path();
 * path.lineTo();// 一阶
 * path.quadTo();// 二阶阶
 * path.cubicTo();// 三阶
 * PathInterpolatorCompat.create(new Path().quadTo());
 * <p/>
 * >>> compile(name:'camerascan-1.0', ext:'aar') aar包引入


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

/**
 * >>> 贝塞尔曲线插值器生成器
 *     Path path = new Path();
 *          path.lineTo();// 一阶
 *          path.quadTo();// 二阶阶
 *          path.cubicTo();// 三阶
 *          PathInterpolatorCompat.create(new Path().quadTo());
 *
 * >>> compile(name:'camerascan-1.0', ext:'aar') aar包引入
 *
 * */
/**
 * >>> TextView 下划线 TextView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
 * >>> EditText 输入过滤
 *     使用：EditText.setKeyListener() EditText.setFilter(new InputFilter[]{new InputFilter.LengthFilter(11)) 长度限定
 *
 * //只接受整数输入
 * KeyListener l = new DigitsKeyListener(fasle,false);
 * //接受有符号整数输入
 * KeyListener l = new DigitsKeyListener(true,false);
 * //接受小数，整数输入
 * KeyListener l = new DigitsKeyListener(false,true);
 * //接受有符号整数/小数输入
 * KeyListener l = new DigitsKeyListener(true,true);
 * */
/**************************************************************************************************/

//        "http://i.imgur.com/rFLNqWI.jpg",
//        "http://i.imgur.com/C9pBVt7.jpg",
//        "http://i.imgur.com/rT5vXE1.jpg",
//        "http://i.imgur.com/aIy5R2k.jpg",
//        "http://i.imgur.com/MoJs9pT.jpg",
//        "http://i.imgur.com/S963yEM.jpg",
//        "http://i.imgur.com/rLR2cyc.jpg",
//        "http://i.imgur.com/SEPdUIx.jpg",
//        "http://i.imgur.com/aC9OjaM.jpg",
//        "http://i.imgur.com/76Jfv9b.jpg",
//        "http://i.imgur.com/fUX7EIB.jpg",
//        "http://i.imgur.com/syELajx.jpg",
//        "http://i.imgur.com/COzBnru.jpg",
//        "http://i.imgur.com/Z3QjilA.jpg",

//        SVG Drawable
//        M (x y) 移动到x,y
//        L (x y) 直线连到x,y，还有简化命令H(x) 水平连接、V(y)垂直连接
//        Z，没有参数，连接起点和终点
//        C(x1 y1 x2 y2 x y)，控制点x1,y1 x2,y2，终点x,y
//        Q(x1 y1 x y)，控制点x1,y1，终点x,y
//        A(rx ry x-axis-rotation large-arc-flag sweep-flag x y)
//        rx ry 椭圆半径
//        x-axis-rotation x轴旋转角度
//        large-arc-flag 为0时表示取小弧度，1时取大弧度
//        sweep-flag 0取逆时针方向，1取顺时针方向
