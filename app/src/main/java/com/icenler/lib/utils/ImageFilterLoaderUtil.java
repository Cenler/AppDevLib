package com.icenler.lib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by iCenler - 2015/9/11：
 * Description：图品滤镜加载工具类
 * 1、 高斯模糊
 * 2、 ……
 */
public class ImageFilterLoaderUtil {

    /**
     * 高斯模糊显示图片 TODO 待完善测试
     *
     * @param context
     * @param imageView
     * @param url
     */
    public static void loadingImage(final Context context, final ImageView imageView, String url) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                imageView.setImageBitmap(ImageUtil.blurBitmap(context, loadedImage, 0));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    static class BlurAsyncTask extends AsyncTask<Bitmap, Void, Bitmap> {
        private Context mContext;
        private ImageView mImageView;
        private float mRadius = 10;

        public BlurAsyncTask(String imageUri, ImageView imageView, Context context, float radius) {
            this.mContext = context;
            this.mImageView = imageView;
            this.mRadius = radius;
        }

        @Override
        protected Bitmap doInBackground(Bitmap... loadedImage) {
            return ImageUtil.blurBitmap(mContext, loadedImage[0], mRadius);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mImageView.setImageBitmap(bitmap);
        }
    }

}
