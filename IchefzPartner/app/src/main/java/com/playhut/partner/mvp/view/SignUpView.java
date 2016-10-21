package com.playhut.partner.mvp.view;

import com.playhut.partner.entity.Account;

/**
 * 注册View定义
 */
public abstract class SignUpView extends BaseView{
    public abstract void loadSuccess(Account account);
}
