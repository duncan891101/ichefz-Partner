package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 修改密码
 */
public interface ModifyPwdService {

    @FormUrlEncoded
    @POST("password_edit.html")
    Observable<ResponseBody> modify(@FieldMap Map<String, String> map);

}
