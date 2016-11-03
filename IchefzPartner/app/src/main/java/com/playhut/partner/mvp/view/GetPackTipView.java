package com.playhut.partner.mvp.view;

import com.playhut.partner.entity.PackTipEntity;

/**
 * 获取菜的做法材料等View定义
 */
public abstract class GetPackTipView extends BaseView{
    public abstract void loadSuccess(PackTipEntity entity);
}
