package com.playhut.partner.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 *
 */
public class ImageLoderOptionUtils {

    /**
     * ImageLoader设置参数
     *
     * @param failImg       加载失败时显示的图片，不需要则填0
     * @param imgRound      圆角半径，不需要填0
     * @param loadingImage  加载中时显示的图片，不需要则填0
     * @return
     */
    public static DisplayImageOptions setOptions(int failImg, int imgRound, int loadingImage) {
        DisplayImageOptions options = null;
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder().
                cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true).displayer(new RoundedBitmapDisplayer(imgRound));
        if (failImg > 0) {
            builder = builder.showImageOnFail(failImg).showImageForEmptyUri(failImg);
        }
        if (loadingImage > 0) {
            builder = builder.showImageOnLoading(loadingImage);
        }
        options = builder.build();
        return options;
    }

}
