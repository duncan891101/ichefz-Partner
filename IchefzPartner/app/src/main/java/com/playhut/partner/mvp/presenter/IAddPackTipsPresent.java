package com.playhut.partner.mvp.presenter;

import java.io.File;
import java.util.List;

/**
 * add pack tips Present接口定义
 */
public interface IAddPackTipsPresent {

    /**
     * type: 1-tips  2-ingredient  3-instruction
     */
    void add(String menuId, String type, List<File> fileList, String titles, String descs);

}
