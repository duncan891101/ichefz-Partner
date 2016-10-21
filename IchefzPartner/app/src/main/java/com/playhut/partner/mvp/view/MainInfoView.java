package com.playhut.partner.mvp.view;

import com.playhut.partner.entity.MainInfoEntity;

/**
 * 首页信息View定义
 */
public abstract class MainInfoView extends BaseView{
    public abstract void loadSuccess(MainInfoEntity entity);
}
