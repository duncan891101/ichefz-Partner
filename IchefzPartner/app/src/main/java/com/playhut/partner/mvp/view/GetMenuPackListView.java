package com.playhut.partner.mvp.view;

import com.playhut.partner.entity.MyMenuPackEntity;

/**
 * My menu pack list View定义
 */
public abstract class GetMenuPackListView extends BaseView{
    public abstract void loadSuccess(MyMenuPackEntity entity);
}
