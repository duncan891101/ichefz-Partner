package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 确认订单
 */
public interface ConfirmOrderService {

    @FormUrlEncoded
    @POST("confirm_order.html")
    Observable<ResponseBody> confirm(@FieldMap Map<String, String> map);

}
