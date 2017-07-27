package com.lzw.imageprocessor;

import android.view.View;

import java.io.File;

/**
 * 图片管理类，提供对外接口。
 * 静态代理模式，开发者只需要关心ImageLoaderProxy + LoaderOptions
 */
public class ImageLoaderHelper implements ILoaderProxy {
    private static ILoaderProxy sLoader;
    private static volatile ImageLoaderHelper sInstance;

    private ImageLoaderHelper() {
        sLoader = new PicassoLoaderProcessor();
    }

    public static ImageLoaderHelper getsInstance(){
        if(sInstance == null){
           synchronized (ImageLoaderHelper.class){
               if(sInstance == null){
                   sInstance = new ImageLoaderHelper();
               }
           }
        }
        return sInstance;
    }

    //可以随时替换图片加载框架
    public static void setImageLoader(ILoaderProxy loader) {
        if (loader != null) {
            sLoader = loader;
        }
    }

    @Override
    public ILoaderProxy loadImage(View view, String path) {
        sLoader.loadImage(view, path);
        return sLoader;
    }

    @Override
    public ILoaderProxy loadImage(View view, int drawable) {
        sLoader.loadImage(view, drawable);
        return sLoader;
    }

    @Override
    public ILoaderProxy loadImage(View view, File file) {
        sLoader.loadImage(view, file);
        return sLoader;
    }

    @Override
    public ILoaderProxy saveImage(String url, File destFile, ICallback callback) {
        return sLoader.saveImage(url, destFile, callback);
    }

    @Override
    public ILoaderProxy setLoaderOptions(LoaderOptions options) {
        return sLoader.setLoaderOptions(options);
    }

    @Override
    public ILoaderProxy clearMemoryCache() {
        return sLoader.clearMemoryCache();
    }

    @Override
    public ILoaderProxy clearDiskCache() {
        return sLoader.clearDiskCache();
    }

}
