package com.playhut.partner.base;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.playhut.partner.constants.GlobalConstants;
import com.playhut.partner.entity.Account;
import com.playhut.partner.utils.AccountUtils;
import com.playhut.partner.utils.FileUtils;
import com.playhut.partner.utils.PartnerUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Application
 */
public class PartnerApplication extends Application {

    public static PartnerApplication mApp = null;

    public static Account mAccount = null;

    private Executor mMultiThreadExecutor;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        mAccount = AccountUtils.getAccount();
        initGlobalConstants();
        initImageLoader(getApplicationContext());
    }

    /**
     * 初始化一些全局的变量
     */
    private void initGlobalConstants() {
        GlobalConstants.DEVICE_ID = PartnerUtils.getDeviceId(this);
        GlobalConstants.SCREEN_WIDTH = PartnerUtils.getScreenWidth(this);
        GlobalConstants.SCREEN_HEIGHT = PartnerUtils.getScreenHeight(this);
        GlobalConstants.APP_VERSION = PartnerUtils.getAppVersion(this);
        GlobalConstants.STATUS_BAR_HEIGHT = PartnerUtils.getStatusBarHeight(this);
    }

    /**
     * 初始化ImageLoader
     */
    private void initImageLoader(Context context) {
        ImageLoaderConfiguration config =
                new ImageLoaderConfiguration.Builder(context)
                        .threadPoolSize(4) // 线程池内加载的数量
                        .threadPriority(Thread.NORM_PRIORITY - 1)
                        .denyCacheImageMultipleSizesInMemory()
                        .discCacheFileNameGenerator(new Md5FileNameGenerator()) // 文件名以MD5加密保存
                        .tasksProcessingOrder(QueueProcessingType.FIFO)
                        .discCache(new UnlimitedDiskCache(FileUtils.getImageCachePath(context))) // 自定义缓存路径
                        .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                        .imageDownloader(new BaseImageDownloader(context))
                        .writeDebugLogs()
                        .build();// 开始构建
        ImageLoader.getInstance().init(config);
    }

    /**
     * 多线程线程池
     *
     * @return
     */
    public synchronized Executor getMultiThreadExecutor() {
        if (mMultiThreadExecutor == null) {
            mMultiThreadExecutor = Executors.newCachedThreadPool();
        }
        return mMultiThreadExecutor;
    }

}
