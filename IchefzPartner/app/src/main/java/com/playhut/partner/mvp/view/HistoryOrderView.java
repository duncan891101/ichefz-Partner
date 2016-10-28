package com.playhut.partner.mvp.view;

import com.playhut.partner.entity.NewOrderEntity;

/**
 * 其他订单View定义
 */
public abstract class HistoryOrderView extends BaseView{
    public abstract void loadSuccess(NewOrderEntity entity);
}
