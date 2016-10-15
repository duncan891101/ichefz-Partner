package com.playhut.partner.entity;

import android.text.TextUtils;

/**
 * 用户账户
 */
public class Account {
    private String chef_id; // 用户ID
    private String api_token; // token

    public String getChef_id() {
        return chef_id;
    }

    public void setChef_id(String chef_id) {
        this.chef_id = chef_id;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    /**
     * 判断账号是否有效
     *
     * @return
     */
    public boolean isAccountValid() {
        return !TextUtils.isEmpty(chef_id) && !TextUtils.isEmpty(api_token);
    }

}
