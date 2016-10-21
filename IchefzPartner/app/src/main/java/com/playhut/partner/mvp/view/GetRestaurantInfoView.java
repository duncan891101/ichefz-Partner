package com.playhut.partner.mvp.view;

import com.playhut.partner.entity.RestaurantEntity;

/**
 * 获取厨房信息View定义
 */
public abstract class GetRestaurantInfoView extends BaseView{
    public abstract void loadSuccess(RestaurantEntity entity);
}
