package com.playhut.partner.mvp.presenter;

import java.io.File;

/**
 * edit packPresent接口定义
 */
public interface IEditPackWithImgPresent {

    void edit(File file, String menuId, String title, String brief, String desc, String person2, String person4,
              String maxQuantity, String howMade);

}
