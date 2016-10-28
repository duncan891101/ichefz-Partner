package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 确认退款
 */
public interface ConfirmRefundService {

    @FormUrlEncoded
    @POST("refund_adopt.html")
    Observable<ResponseBody> refund(@FieldMap Map<String, String> map);

}
