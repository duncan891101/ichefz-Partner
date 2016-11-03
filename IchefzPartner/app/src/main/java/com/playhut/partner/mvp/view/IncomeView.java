package com.playhut.partner.mvp.view;

import com.playhut.partner.entity.IncomeEntity;

/**
 * 厨师收入View定义
 */
public abstract class IncomeView extends BaseView{
    public abstract void loadSuccess(IncomeEntity entity);
}
