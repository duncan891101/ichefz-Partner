package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * delete tip
 */
public interface DeleteTipService {

    @FormUrlEncoded
    @POST("pack_del_imgs.html")
    Observable<ResponseBody> delete(@FieldMap Map<String, String> map);

}
