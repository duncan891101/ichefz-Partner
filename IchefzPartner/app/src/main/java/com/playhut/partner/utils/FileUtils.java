package com.playhut.partner.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 文件操作的工具类
 */
public class FileUtils {

    public static final String ICHEFZ_ROOT_FILE = "/partner";

    public static final String IMG_CACHE_FILE = "/img_cache"; // 图片缓存文件夹

    public static final String IMG_AVATAR_FILE = "/img_avatar";

    public static final String APK_FILE = "/apk";

    /**
     * 判断SD卡是否存在
     *
     * @return
     */
    private static boolean isSDCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡根目录
     *
     * @return
     */
    private static File getSDCardPath() {
        return Environment.getExternalStorageDirectory();
    }

    /**
     * 图片缓存存储目录
     *
     * @param context
     * @return
     */
    public static File getImageCachePath(Context context) {
        if (isSDCardExist()) {
            File rootFile = getSDCardPath();
            String cachePathStr = rootFile.getAbsolutePath() + ICHEFZ_ROOT_FILE + IMG_CACHE_FILE;
            File cachePathFile = new File(cachePathStr);
            if (!cachePathFile.exists()) {
                cachePathFile.mkdirs();
            }
            return cachePathFile;
        } else {
            return context.getFilesDir();
        }
    }

    /**
     * 头像缓存存储目录
     *
     * @param context
     */
    public static File getAvatarCachePath(Context context) {
        if (isSDCardExist()) {
            File rootFile = getSDCardPath();
            String cachePathStr = rootFile.getAbsolutePath() + ICHEFZ_ROOT_FILE + IMG_AVATAR_FILE;
            File cachePathFile = new File(cachePathStr);
            if (!cachePathFile.exists()) {
                cachePathFile.mkdirs();
            }
            return cachePathFile;
        } else {
            return context.getFilesDir();
        }
    }

    /**
     * 下载的APK文件存放的目录
     */
    public static File getDownLoadApkPath(Context context) {
        if (isSDCardExist()) {
            File rootFile = getSDCardPath();
            String cachePathStr = rootFile.getAbsolutePath() + ICHEFZ_ROOT_FILE + APK_FILE;
            File cachePathFile = new File(cachePathStr);
            if (!cachePathFile.exists()) {
                cachePathFile.mkdirs();
            }
            return cachePathFile;
        } else {
            return context.getFilesDir();
        }
    }

}
