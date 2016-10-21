package com.playhut.partner.mvp.view;

import com.playhut.partner.entity.Account;

/**
 * 登录View定义
 */
public abstract class LoginView extends BaseView{
    public abstract void loadSuccess(Account account);
}
