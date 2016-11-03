package com.playhut.partner.mvp.presenter;

import java.io.File;
import java.util.Map;

/**
 * edit tip ingredient instruction Present接口定义
 */
public interface IEditTipPresent {

    void edit(String menuId, int type, String ids, Map<String, File> map, String titles, String descs);

}
