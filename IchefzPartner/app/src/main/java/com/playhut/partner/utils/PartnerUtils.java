package com.playhut.partner.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.playhut.partner.network.Md5;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基本工具类
 */
public class PartnerUtils {

    /**
     * 获取APP当前版本号
     *
     * @return
     */
    public static String getAppVersion(Context context) {
        String result = "";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            result = info.versionName;
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 获取手机设备号
     *
     * @return
     */
    public static String getDeviceId(Context context) {
        // 由于READ_PHONE_STATE权限在6.0上不能配置在配置文件上，必须动态询问用户才能有用，所以TelephonyManager getDeviceId在6.0上获取不到
        StringBuilder deviceId = new StringBuilder("and");
        String serialNumber = Build.SERIAL;
        if (!TextUtils.isEmpty(serialNumber)){
            deviceId.append(serialNumber);
        }
        deviceId.append("_");
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!TextUtils.isEmpty(androidId)){
            deviceId.append(androidId);
        }
        return Md5.md5Digest(deviceId.toString());
    }

    /**
     * dip转px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px转dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 判断是否已连接网络
     *
     * @param context
     * @return
     */
    public static boolean checkNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isConnected() && info.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 手机号码判断
     *
     * @param phone 手机号码
     * @return
     */
    public static boolean isMobileNumber(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        String reg = "^((13[0-9])|(14[0,9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(phone);
        boolean flag = matcher.matches();
        return flag;
    }

    /**
     * 判断该View是否在屏幕内
     *
     * @param context
     * @param view
     * @return
     */
    public static boolean isViewInScreen(Context context, View view) {
        Rect rect = new Rect(0, 0, getScreenWidth(context), getScreenHeight(context));
        return view.getLocalVisibleRect(rect);
    }

    /**
     * 判断是否Email
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 格式化价格
     *
     * @param price
     * @return
     */
    public static String formatPrice(float price) {
        BigDecimal bigDecimal = new BigDecimal(price);
        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        return String.format("%.2f", bigDecimal.floatValue());
    }

    /**
     * 显示软键盘
     *
     * @param context
     * @param view
     */
    public static void showKeyBoard(Context context, View view) {
        // 显示软键盘
        try {
            InputMethodManager imm = ((InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE));
            imm.showSoftInput(view, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 隐藏软键盘
     *
     * @param context
     */
    public static void hideKeyboard(Context context) {
        try {
            InputMethodManager imm = ((InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE));
            if (((Activity) context).getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
        }
        return sbar;
    }


    /**
     * 获取当前日期
     *
     * @return
     */
    public static String[] getCurrentDate() {
        String[] dates = new String[3];
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        dates[0] = String.valueOf(year);
        if ((month + "").length() == 1) {
            dates[1] = "0" + month;
        } else {
            dates[1] = String.valueOf(month);
        }
        if ((day + "").length() == 1) {
            dates[2] = "0" + day;
        } else {
            dates[2] = String.valueOf(day);
        }
        return dates;
    }


}
