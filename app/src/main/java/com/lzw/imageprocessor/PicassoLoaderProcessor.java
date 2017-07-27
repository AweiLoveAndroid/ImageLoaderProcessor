package com.lzw.imageprocessor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.lzw.imageprocessor.cache.LruCacheUtils;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.File;

/**
 * Picasso图片加载具体方式
 */
public class PicassoLoaderProcessor implements ILoaderProxy {
    private volatile static Picasso sPicassoSingleton;
    private final String PICASSO_CACHE = "picasso-cache";
    private static LruCache sLruCache = new LruCache(AppLike.gInstance);
    private static ILoaderProxy sLoaderProxy;
    private LoaderOptions options;

    public PicassoLoaderProcessor(LoaderOptions options) {
        this.options = options;
    }

    private static Picasso getPicasso() {
        if (sPicassoSingleton == null) {
            synchronized (PicassoLoaderProcessor.class) {
                if (sPicassoSingleton == null) {
                    sPicassoSingleton = new Picasso.Builder(AppLike.gInstance).memoryCache(sLruCache).build();
                }
            }
        }
        return sPicassoSingleton;
    }

    private static ILoaderProxy init(ILoaderProxy loaderProxy) {
        sLoaderProxy = loaderProxy;
        return sLoaderProxy;

    }

    private ILoaderProxy obtain() {
        return PicassoLoaderProcessor.init(sLoaderProxy);
    }

    @Override
    public ILoaderProxy loadImage(View view, String path) {
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            //判断缓存中是否已经缓存过该图片，有则直接拿Bitmap，没有则直接调用Glide加载并缓存Bitmap
            Bitmap bitmap = LruCacheUtils.getInstance().getBitmapFromMemCache(path);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                RequestCreator requestCreator = getPicasso().load(path);
                loadOptions(requestCreator).into(imageView);
            }
        }

        return obtain();
    }

    @Override
    public ILoaderProxy loadImage(View view, int drawable) {
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            RequestCreator requestCreator = getPicasso().load(drawable);
            loadOptions(requestCreator).into(imageView);

        }
        return obtain();
    }

    @Override
    public ILoaderProxy loadImage(View view, File file) {
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            //判断缓存中是否已经缓存过该图片，有则直接拿Bitmap，没有则直接调用Glide加载并缓存Bitmap
            Bitmap bitmap = LruCacheUtils.getInstance().getBitmapFromMemCache(file);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                RequestCreator requestCreator = getPicasso().load(file);
                loadOptions(requestCreator).into(imageView);
            }
        }
        return obtain();
    }


    @Override
    public ILoaderProxy saveImage(String url, final File destFile, final ICallback callback) {
        getPicasso().load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                FileUtil.saveBitmap(bitmap, destFile.getAbsolutePath());
                AppLike.gInstance.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(destFile)));
                ToastUtils.showWarnMsg("图片保存成功！");
                if (callback != null) {
                    callback.onSuccess("图片保存成功");
                }
                if (bitmap != null) {
                    bitmap.recycle();
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                ToastUtils.showWarnMsg("图片保存失败！");
                if (callback != null) {
                    callback.onFailed("图片加载失败");
                }
            }


            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
        return obtain();
    }

    @Override
    public ILoaderProxy setLoaderOptions(LoaderOptions options) {
        return obtain().setLoaderOptions(options);
    }

    @Override
    public ILoaderProxy clearMemoryCache() {
        sLruCache.clear();
        return obtain();
    }

    @Override
    public ILoaderProxy clearDiskCache() {
        File diskFile = new File(AppLike.gInstance.getCacheDir(), PICASSO_CACHE);
        if (diskFile.exists()) {
            FileUtil.deleteFile(diskFile);
        }
        return obtain();
    }

    private RequestCreator loadOptions(RequestCreator requestCreator) {
        if (options == null) {
            return requestCreator;
        }
        if (options.targetHeight > 0 && options.targetWidth > 0) {
            requestCreator.resize(options.targetWidth, options.targetHeight);
        }
        if (options.isCenterInside) {
            requestCreator.centerInside();
        } else if (options.isCenterCrop) {
            requestCreator.centerCrop();
        }
        if (options.config != null) {
            requestCreator.config(options.config);
        }
        if (options.errorResId != 0) {
            requestCreator.error(options.errorResId);
        }
        if (options.placeholderResId != 0) {
            requestCreator.placeholder(options.placeholderResId);
        }
        if (options.bitmapAngle != 0) {
            requestCreator.transform(new PicassoTransformation(options.bitmapAngle));
        }
        return requestCreator;
    }

    /**
     * 圆角矩形
     */
    class PicassoTransformation implements Transformation {
        private float bitmapAngle;

        protected PicassoTransformation(float corner) {
            this.bitmapAngle = corner;
        }

        @Override
        public Bitmap transform(Bitmap source) {
            float roundPx = bitmapAngle;//角度
            Bitmap output = Bitmap.createBitmap(source.getWidth(),
                    source.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, source.getWidth(), source.getHeight());
            final RectF rectF = new RectF(rect);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(source, rect, rect, paint);
            source.recycle();
            return output;
        }

        @Override
        public String key() {
            return "bitmapAngle()";
        }
    }

}
