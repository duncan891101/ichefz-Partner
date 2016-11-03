package com.playhut.partner.debug;

/**
 *
 */
public class MyLog {

    public static final String TAG = "zhangbh";

    /**
     * 正式上线需要改为false
     */
    public static final boolean isDebug = false;

    public static void i(String msg) {
        if (isDebug)
            android.util.Log.i(TAG, msg);
    }

}
