package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * My menu set list
 */
public interface GetMenuSetListService {

    @FormUrlEncoded
    @POST("set_list.html")
    Observable<ResponseBody> getList(@FieldMap Map<String, String> map);

}
