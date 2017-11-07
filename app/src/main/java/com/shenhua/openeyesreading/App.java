package com.shenhua.openeyesreading;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Application
 * Created by Shenhua on 5/8/2016.
 */
public class App extends Application {

    private static Context mApplicationContext;

    private static DisplayImageOptions.Builder b;

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("app is start");
        mApplicationContext = this;
        initImageLoader(getApplicationContext());
    }

    public static void initImageLoader(Context context) {
        b = new DisplayImageOptions.Builder();
        b.cacheOnDisc(true)
                .cacheInMemory(false)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565);
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.discCacheFileNameGenerator(new Md5FileNameGenerator());
        config.memoryCacheSize(20 * 1024 * 1024); // 20 MiB
        config.discCacheSize(100 * 1024 * 1024); // 100 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.defaultDisplayImageOptions(b.build());
        config.writeDebugLogs();
        ImageLoader.getInstance().init(config.build());
    }

    public static DisplayImageOptions getDefaultOptionsBuilder() {
        b.showImageOnFail(R.drawable.img_default);
        b.showImageForEmptyUri(R.drawable.img_default);
        return b.build();
    }

    public static Context getContext() {
        return mApplicationContext;
    }
}
