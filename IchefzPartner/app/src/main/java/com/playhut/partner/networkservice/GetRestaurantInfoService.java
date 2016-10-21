package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 获取厨房信息等
 */
public interface GetRestaurantInfoService {

    @FormUrlEncoded
    @POST("kitchen_get_info.html")
    Observable<ResponseBody> get(@FieldMap Map<String, String> map);

}
