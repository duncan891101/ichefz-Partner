package com.playhut.partner.mvp.view;

import com.playhut.partner.entity.FinanceListEntity;

/**
 * 厨师收入订单View定义
 */
public abstract class IncomeOrderView extends BaseView{
    public abstract void loadSuccess(FinanceListEntity entity);
}
