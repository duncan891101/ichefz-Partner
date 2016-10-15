package com.playhut.partner.entity;

import java.util.List;

/**
 * IchefzSubscriber返回值包装类
 *
 * @param <K>
 */
public class SubscriberResponse<K> {
    private List<K> resultList;
    private K result;
    private STATE state;

    public enum STATE {
        RESULT, RESULT_LIST
    }

    public List<K> getResultList() {
        return resultList;
    }

    public void setResultList(List<K> resultList) {
        this.resultList = resultList;
    }

    public K getResult() {
        return result;
    }

    public void setResult(K result) {
        this.result = result;
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }
}
