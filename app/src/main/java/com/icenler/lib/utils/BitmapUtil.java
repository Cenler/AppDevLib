package com.icenler.lib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by iCenler - 2015/9/9.
 * Description：图品相关处理工具类
 * 1、Bitmap、Drawble、Bytes互转
 * 2、Bitmap 存储
 * 3、高斯模糊图片
 * 4、阴影图片
 * 5、缩放图片
 */
public class BitmapUtil {

    private BitmapUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * @return 位图转字节码
     */
    public static byte[] bitmap2Bytes(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * @return 字节码转位图
     */
    public static Bitmap bytes2Bitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    /**
     * @return Drawable 转 Bitmap
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable == null) return null;

        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * @return Bitmap 转 Drawable
     */
    @SuppressWarnings("deprecation")
    public static Drawable bitmap2Drawable(Bitmap bm) {
        if (bm == null) return null;

        BitmapDrawable bd = new BitmapDrawable(bm);
        bd.setTargetDensity(bm.getDensity());

        return new BitmapDrawable(bm);
    }

    /**
     * 保存图片到指定位置
     */
    public static void saveBitmap(Context context, Bitmap bmp, String fileName) {
        try {
            FileOutputStream fileOut = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveBitmap(Context context, Bitmap bitmap, File file) {
        saveBitmap(context, bitmap, file.getAbsolutePath());
    }

    public static void saveBitmap(Context context, Bitmap bitmap, String filePath, String fileName) {
        saveBitmap(context, bitmap, new File(filePath, fileName).getAbsolutePath());
    }

    /**
     * @param bitmap
     * @param context
     * @param radius
     * @return 设置图品高斯模糊效果
     */
    public static Bitmap blurBitmap(Bitmap bitmap, Context context, float radius) {
        if (Build.VERSION.SDK_INT < 17) {
            return bitmap;
        }

        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(context);

        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps

        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur
        blurScript.setRadius(radius);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        //recycle the original bitmap
        bitmap.recycle();

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;
    }

    /**
     * 创建带有影子的图片
     *
     * @param originalImage 原图比例
     * @param scale         缩放比例
     */
    public static Bitmap createReflectedImage(Bitmap originalImage, float reflectRatio, float scale) {
        int width = (int) (originalImage.getWidth() * scale);
        int height = (int) (originalImage.getHeight() * scale);

        final Rect srcRect = new Rect(0, 0, originalImage.getWidth(), originalImage.getHeight());
        final Rect dstRect = new Rect(0, 0, width, height);

        final int reflectionGap = 1;

        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (int) (height + height * reflectRatio), Bitmap.Config.ARGB_8888);
        Canvas canvasRef = new Canvas(bitmapWithReflection);

        canvasRef.drawBitmap(originalImage, srcRect, dstRect, null);

        Matrix matrix = new Matrix();
        matrix.setTranslate(0, height + height + reflectionGap);
        matrix.preScale(scale, -scale);

        canvasRef.drawBitmap(originalImage, matrix, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, height, 0,
                bitmapWithReflection.getHeight() + reflectionGap,
                0x80ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvasRef.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

        originalImage.recycle();

        return bitmapWithReflection;
    }

    /**
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return 指定位图大小进行缩放
     */
    public static Bitmap zoomImage(Bitmap bitmap, int newWidth, int newHeight) {
        // 获取这个图片的宽和高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();

        // 计算缩放率，新尺寸除原始尺寸
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        return bmp;
    }

    /**
     * 得到缩放后的图片
     */
    public static Bitmap getScaleBitmap(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return dstbmp;
    }

}
