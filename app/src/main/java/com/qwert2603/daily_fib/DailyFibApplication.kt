package com.qwert2603.daily_fib

import android.app.Application
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration

class DailyFibApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val displayImageOptions = DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.ic_image_black_24dp)
                .build()
        val config = ImageLoaderConfiguration.Builder(this)
                .memoryCacheSize(12 * 1024 * 1024)
                .diskCache(UnlimitedDiskCache(cacheDir))
                .defaultDisplayImageOptions(displayImageOptions)
                .build()
        ImageLoader.getInstance().init(config)
    }
}