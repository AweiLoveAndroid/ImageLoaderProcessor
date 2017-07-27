package com.lzw.imageprocessor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.io.File;
import java.security.MessageDigest;

/**
 * Glide图片加载具体方式
 */
public class GlideLoaderProcessor implements ILoaderProxy{
    @Override
    public ILoaderProxy loadImage(View view, String path) {
        return null;
    }

    @Override
    public ILoaderProxy loadImage(View view, int drawable) {
        return null;
    }

    @Override
    public ILoaderProxy loadImage(View view, File file) {
        return null;
    }

    @Override
    public ILoaderProxy saveImage(String url, File destFile, ICallback callback) {
        return null;
    }

    @Override
    public ILoaderProxy setLoaderOptions(LoaderOptions options) {
        return null;
    }

    @Override
    public ILoaderProxy clearMemoryCache() {
        return null;
    }

    @Override
    public ILoaderProxy clearDiskCache() {
        return null;
    }



    /**
     * Glide显示为圆形图片
     */
    static class GlideCircleTransform extends BitmapTransformation {

        public GlideCircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null){
                return null;
            }

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }


        public String getId() {
            return getClass().getName();
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {

        }
    }

}
