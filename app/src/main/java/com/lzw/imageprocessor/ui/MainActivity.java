package com.lzw.imageprocessor.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.lzw.imageprocessor.ImageLoaderHelper;
import com.lzw.imageprocessor.LoaderOptions;
import com.lzw.imageprocessor.R;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.iv);

        //真正的加载图片的操作
        ImageLoaderHelper.getsInstance()
                .loadImage(imageView,R.mipmap.ic_launcher)
                .setLoaderOptions(new LoaderOptions.Builder()
                        .angle(2)
                        .centerCrop()
                        .centerInside()
                        .config(Bitmap.Config.RGB_565)
                        .error(R.drawable.ic_holder)
                        .placeHolder(R.drawable.ic_holder)
                        .reSize(200,200)
                        .build())
                .clearDiskCache()
                .clearMemoryCache();
    }
}