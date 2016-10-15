package com.playhut.partner.entity;

/**
 * content是一个jsonObject、String等的解析类
 */
public class BaseResponseNoList<T> extends BaseResponse {

    private T content;

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

}
