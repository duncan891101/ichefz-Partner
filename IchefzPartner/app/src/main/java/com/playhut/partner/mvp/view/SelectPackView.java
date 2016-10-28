package com.playhut.partner.mvp.view;

import com.playhut.partner.entity.SelectPackEntity;

/**
 * Select from pack View定义
 */
public abstract class SelectPackView extends BaseView{
    public abstract void loadSuccess(SelectPackEntity entity);
}
