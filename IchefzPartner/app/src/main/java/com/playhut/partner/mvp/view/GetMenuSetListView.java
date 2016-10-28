package com.playhut.partner.mvp.view;

import com.playhut.partner.entity.MyMenuSetEntity;

/**
 * My menu set list View定义
 */
public abstract class GetMenuSetListView extends BaseView{
    public abstract void loadSuccess(MyMenuSetEntity entity);
}
