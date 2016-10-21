package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 注册
 */
public interface SignUpService {

    @FormUrlEncoded
    @POST("register.html")
    Observable<ResponseBody> signUp(@FieldMap Map<String, String> map);

}
