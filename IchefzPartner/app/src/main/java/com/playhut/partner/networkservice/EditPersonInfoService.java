package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 修改个人信息
 */
public interface EditPersonInfoService {

    @FormUrlEncoded
    @POST("profile_edit.html")
    Observable<ResponseBody> edit(@FieldMap Map<String, String> map);

}
