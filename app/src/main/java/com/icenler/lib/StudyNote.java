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