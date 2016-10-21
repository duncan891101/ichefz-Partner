package com.playhut.partner.utils;

import android.widget.Toast;

import com.playhut.partner.base.PartnerApplication;

/**
 *
 */
public class ToastUtils {

    public static void show(String text) {
        Toast.makeText(PartnerApplication.mApp.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

}
