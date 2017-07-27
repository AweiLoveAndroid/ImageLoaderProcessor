package com.lzw.imageprocessor.ui;

import android.app.Application;

import com.lzw.imageprocessor.ImageLoaderHelper;
import com.lzw.imageprocessor.LoaderOptions;
import com.lzw.imageprocessor.PicassoLoaderProcessor;

/**
 * 作者：Created by lzw
 * 时间：Created on 2017/7/22 0022 23:03
 * 邮箱：lzw20099002@126.com
 */

public class MyApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        //这里只需要一行代码切换图片加载框架，6不6！！！

        //初始化Picasso方式加载图片
        ImageLoaderHelper.setImageLoader(new PicassoLoaderProcessor(new LoaderOptions()));

        //初始化Glide方式网络请求代理
        //ImageLoaderHelper.setImageLoader(new GlideLoaderProcessor());
    }
}
