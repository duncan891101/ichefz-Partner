package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 忘记密码
 */
public interface ForgotPwdService {

    @FormUrlEncoded
    @POST("find_password.html")
    Observable<ResponseBody> forgotPwd(@FieldMap Map<String, String> map);

}
