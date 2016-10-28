package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * add set
 */
public interface AddSetService {

    @FormUrlEncoded
    @POST("set_add.html")
    Observable<ResponseBody> add(@FieldMap Map<String, String> map);

}
