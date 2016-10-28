package com.playhut.partner.mvp.view;

import com.playhut.partner.entity.NewOrderEntity;

/**
 * 新订单View定义
 */
public abstract class NewOrderView extends BaseView{
    public abstract void loadSuccess(NewOrderEntity entity);
}
