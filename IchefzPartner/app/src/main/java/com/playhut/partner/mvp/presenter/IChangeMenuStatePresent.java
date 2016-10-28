package com.playhut.partner.mvp.presenter;

/**
 * 修改菜的状态Present接口定义
 */
public interface IChangeMenuStatePresent {

    /**
     * mtype: "list" or "set"
     * state: 0-关闭 1-打开 7-未审核 8-审核中 9-审核失败
     */
    void change(String menuId, String mtype, String state);

}
