package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 登录
 */
public interface LoginService {

    @FormUrlEncoded
    @POST("login.html")
    Observable<ResponseBody> login(@FieldMap Map<String, String> map);

}
