package com.playhut.partner.networkservice;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * edit tip ingredient instruction
 */
public interface EditTipService {

    @Multipart
    @POST("pack_edit_imgs.html")
    Observable<ResponseBody> edit(@PartMap Map<String, RequestBody> map);

}
