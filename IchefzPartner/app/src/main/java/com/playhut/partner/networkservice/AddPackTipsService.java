package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * add tips & techniques  ingredient instruction
 */
public interface AddPackTipsService {

    @Multipart
    @POST("pack_add_imgs.html")
    Observable<ResponseBody> add(@PartMap Map<String, RequestBody> map);

}
