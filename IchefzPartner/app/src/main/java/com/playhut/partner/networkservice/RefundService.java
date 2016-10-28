package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 拒绝订单
 */
public interface RefundService {

    @FormUrlEncoded
    @POST("refund_order.html")
    Observable<ResponseBody> refund(@FieldMap Map<String, String> map);

}
