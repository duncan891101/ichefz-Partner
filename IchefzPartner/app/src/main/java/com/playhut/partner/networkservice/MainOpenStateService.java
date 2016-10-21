package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 首页营业状态
 */
public interface MainOpenStateService {

    @FormUrlEncoded
    @POST("is_open.html")
    Observable<ResponseBody> setOpenState(@FieldMap Map<String, String> map);

}
