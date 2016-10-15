package com.playhut.partner.entity;

import java.util.List;

/**
 * content是一个jsonArray的解析类
 */
public class BaseResponseIsList<T> extends BaseResponse {
    private List<T> content;

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
