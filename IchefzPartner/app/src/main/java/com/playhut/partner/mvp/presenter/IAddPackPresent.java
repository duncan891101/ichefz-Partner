package com.playhut.partner.mvp.presenter;

import java.io.File;

/**
 * add pack Present接口定义
 */
public interface IAddPackPresent {

    void add(File file, String title, String brief, String desc, String person2, String person4,
             String maxQuantity, String howMade);

}
