package com.playhut.partner.utils;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.playhut.partner.base.PartnerApplication;
import com.playhut.partner.entity.Account;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Account相关操作类
 */
public class AccountUtils {

    /**
     * 获取Account对象，用于Application中mAccount初始化
     *
     * @return
     */
    public static synchronized Account getAccount() {
        SharedPreferences sp = SPUtils.getAccountSharedPreferences(PartnerApplication.mApp.getApplicationContext());
        Map<String, ?> map = sp.getAll();
        String key = "";
        if (map != null) {
            Set<String> set = map.keySet();
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                key = iterator.next();
                if (key.endsWith("chef_id")) {
                    break;
                }
            }
        }
        if (!TextUtils.isEmpty(key)) {
            int index = key.lastIndexOf(".chef_id");
            if (index != -1) {
                Account account = null;
                try {
                    account = (Account) SPUtils.getObjectWithAccount(Account.class, key.substring(0, index));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return account;
            }
        }
        return null;
    }

    /**
     * 登录后保存账户信息
     *
     * @param account
     */
    public static synchronized void saveAccount(Account account) {
        if (account == null)
            return;
        try {
            SPUtils.saveObjectWithAccount(account, account.getChef_id());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注销后，删除账号信息
     */
    public static synchronized void deleteAccount() {
        if (PartnerApplication.mAccount == null)
            return;
        SharedPreferences sp = SPUtils.getAccountSharedPreferences(PartnerApplication.mApp.getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        Map<String, ?> map = sp.getAll();
        String customerId = PartnerApplication.mAccount.getChef_id();
        if (map != null && !TextUtils.isEmpty(customerId)) {
            Set<String> set = map.keySet();
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                if ((!TextUtils.isEmpty(key)) && key.startsWith(customerId)) {
                    editor.remove(key);
                }
            }
            editor.commit();
        }
    }

}
