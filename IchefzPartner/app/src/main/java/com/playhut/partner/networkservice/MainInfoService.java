package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 首页信息
 */
public interface MainInfoService {

    @FormUrlEncoded
    @POST("home.html")
    Observable<ResponseBody> getInfo(@FieldMap Map<String, String> map);

}
