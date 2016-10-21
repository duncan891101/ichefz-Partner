package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 退出登录
 */
public interface LogoutService {

    @FormUrlEncoded
    @POST("logout.html")
    Observable<ResponseBody> logout(@FieldMap Map<String, String> map);

}
