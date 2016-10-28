package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 拒绝退款
 */
public interface RejectRefundService {

    @FormUrlEncoded
    @POST("refund_reject.html")
    Observable<ResponseBody> reject(@FieldMap Map<String, String> map);

}
