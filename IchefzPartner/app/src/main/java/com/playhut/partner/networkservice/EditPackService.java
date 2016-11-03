package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * edit pack
 */
public interface EditPackService {

    @FormUrlEncoded
    @POST("pack_edit.html")
    Observable<ResponseBody> edit(@FieldMap Map<String, String> map);

}
