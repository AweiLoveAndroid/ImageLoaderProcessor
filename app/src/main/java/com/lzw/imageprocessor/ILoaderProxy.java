package com.lzw.imageprocessor;

import android.view.View;

import java.io.File;

/**
 * 通用接口
 * 提取各个框架通用的View,path/file文件路径,通过LoaderOptions解决大量不同参数传入的问题
 */
public interface ILoaderProxy {

    abstract ILoaderProxy loadImage(View view, String path);
    abstract ILoaderProxy loadImage(View view, int drawable);
    abstract ILoaderProxy loadImage(View view, File file);

    /**
     * 保存图片到本地相册
     * @param url
     * @param destFile
     * @param callback
     */
    ILoaderProxy saveImage(String url, File destFile, ICallback callback);

    /**
     * 设置图片加载参数
     * @return
     */
    ILoaderProxy setLoaderOptions(LoaderOptions options);

    /**
     * 清理内存缓存
     */
    ILoaderProxy clearMemoryCache();

    /**
     * 清理磁盘缓存
     */
    ILoaderProxy clearDiskCache();
}
