package com.playhut.partner.mvp.presenter;

/**
 * 删除菜单Present接口定义
 */
public interface IDeleteMenuPresent {

    /**
     * mtype : 取值"set"或者"list"
     */
    void delete(String menuId, String mtype);

}
