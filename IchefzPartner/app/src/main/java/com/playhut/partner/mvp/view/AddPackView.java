package com.playhut.partner.mvp.view;

import com.playhut.partner.entity.AddPackEntity;

/**
 * Add pack view定义
 */
public abstract class AddPackView extends BaseView{
    public abstract void loadSuccess(AddPackEntity entity);
}
