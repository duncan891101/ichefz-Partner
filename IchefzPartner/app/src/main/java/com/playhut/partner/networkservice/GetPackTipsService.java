package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * get tips & techniques  ingredient instruction
 */
public interface GetPackTipsService {

    @FormUrlEncoded
    @POST("pack_get_imgs.html")
    Observable<ResponseBody> get(@FieldMap Map<String, String> map);

}
