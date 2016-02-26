package com.jack.jianyu;/**
 * author:S.jack
 * data:2016-01-13 17:47
 */

import android.app.Application;
import android.content.Context;

import com.jack.jianyu.constant.AppConstant;
import com.jack.jianyu.utils.ImageLoaderHelper;
import com.nostra13.universalimageloader.core.ImageLoader;


public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initData();
    }

    public static Context getContext() {
        return context;
    }

    private void initData() {
        ImageLoader.getInstance().init(ImageLoaderHelper.getInstance(this).getImageLoaderConfiguration(AppConstant.Paths.IMAGE_LOADER_CACHE_PATH));
    }
}
