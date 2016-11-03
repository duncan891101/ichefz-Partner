package com.playhut.partner.mvp.presenter;

/**
 * 获取菜的材料做法等 Present接口定义
 */
public interface IGetPackTipPresent {

    /**
     * type: 1-tip 2-ingredient 3-instruction
     */
    void get(String menuId, int type);

}
