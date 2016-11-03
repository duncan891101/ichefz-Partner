package com.playhut.partner.mvp.model;

import android.text.TextUtils;

import com.playhut.partner.network.ApiManager;
import com.playhut.partner.network.FieldMapManager;
import com.playhut.partner.networkservice.AddPackService;
import com.playhut.partner.networkservice.AddPackTipsService;
import com.playhut.partner.networkservice.EditPackWithImgService;
import com.playhut.partner.networkservice.EditTipService;
import com.playhut.partner.networkservice.UploadAvatarService;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * 上传文件Model
 */
public class UploadFileModel {

    /**
     * 上传头像
     */
    public Observable<ResponseBody> uploadAvatar(File file) {
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(null);

        Map<String, RequestBody> paramsMap = new TreeMap<>();
        Set<String> set = fieldMap.keySet();
        for (String key : set) {
            String value = fieldMap.get(key);
            paramsMap.put(key, RequestBody.create(MediaType.parse("text/plain"), value));
        }
        paramsMap.put("avatar\"; filename=\"" + file.getName() + "\"",
                RequestBody.create(MediaType.parse("multipart/form-data"), file));

        UploadAvatarService uploadAvatarService = ApiManager.create(UploadAvatarService.class);
        return uploadAvatarService.upload(paramsMap);
    }

    /**
     * add pack
     */
    public Observable<ResponseBody> addPack(File file, String title, String brief, String desc, String person2, String person4,
                                            String maxQuantity, String howMade) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("title", title);
        privateMap.put("brief_introduction", brief);
        privateMap.put("desc", desc);
        if (!TextUtils.isEmpty(person2)) {
            privateMap.put("person2", person2);
        } else {
            privateMap.put("person2", String.valueOf(-1));
        }
        if (!TextUtils.isEmpty(person4)) {
            privateMap.put("person4", person4);
        } else {
            privateMap.put("person4", String.valueOf(-1));
        }
        privateMap.put("max_quantity", maxQuantity);
        if (!TextUtils.isEmpty(howMade)) {
            privateMap.put("how_it_make", howMade);
        }
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);

        Map<String, RequestBody> paramsMap = new TreeMap<>();
        Set<String> set = fieldMap.keySet();
        for (String key : set) {
            String value = fieldMap.get(key);
            paramsMap.put(key, RequestBody.create(MediaType.parse("text/plain"), value));
        }
        paramsMap.put("pack_img\"; filename=\"" + file.getName() + "\"",
                RequestBody.create(MediaType.parse("multipart/form-data"), file));

        AddPackService addPackService = ApiManager.create(AddPackService.class);
        return addPackService.add(paramsMap);
    }

    /**
     * add pack tips  ingredient instruction
     */
    public Observable<ResponseBody> addPackTips(String menuId, String type, List<File> fileList, String titles, String descs) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("menu_id", menuId);
        privateMap.put("type", type);
        privateMap.put("titles", titles);
        privateMap.put("descs", descs);
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);

        Map<String, RequestBody> paramsMap = new TreeMap<>();
        Set<String> set = fieldMap.keySet();
        for (String key : set) {
            String value = fieldMap.get(key);
            paramsMap.put(key, RequestBody.create(MediaType.parse("text/plain"), value));
        }
        if (fileList != null && fileList.size() > 0) {
            for (int i = 1; i <= fileList.size(); i++) {
                File file = fileList.get(i - 1);
                paramsMap.put("image" + String.valueOf(i) + "\"; filename=\"" + file.getName() + "\"",
                        RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }
        }
        AddPackTipsService addPackTipsService = ApiManager.create(AddPackTipsService.class);
        return addPackTipsService.add(paramsMap);
    }

    /**
     * edit pack
     */
    public Observable<ResponseBody> editPack(File file, String menuId, String title, String brief, String desc, String person2, String person4,
                                             String maxQuantity, String howMade) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("menu_id", menuId);
        privateMap.put("title", title);
        privateMap.put("brief_introduction", brief);
        privateMap.put("desc", desc);
        if (!TextUtils.isEmpty(person2)) {
            privateMap.put("person2", person2);
        } else {
            privateMap.put("person2", String.valueOf(-1));
        }
        if (!TextUtils.isEmpty(person4)) {
            privateMap.put("person4", person4);
        } else {
            privateMap.put("person4", String.valueOf(-1));
        }
        privateMap.put("max_quantity", maxQuantity);
        if (!TextUtils.isEmpty(howMade)) {
            privateMap.put("how_it_make", howMade);
        }
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);

        Map<String, RequestBody> paramsMap = new TreeMap<>();
        Set<String> set = fieldMap.keySet();
        for (String key : set) {
            String value = fieldMap.get(key);
            paramsMap.put(key, RequestBody.create(MediaType.parse("text/plain"), value));
        }
        paramsMap.put("menu_img\"; filename=\"" + file.getName() + "\"",
                RequestBody.create(MediaType.parse("multipart/form-data"), file));

        EditPackWithImgService editPackWithImgService = ApiManager.create(EditPackWithImgService.class);
        return editPackWithImgService.edit(paramsMap);
    }

    /**
     * edit pack tips  ingredient instruction
     */
    public Observable<ResponseBody> editTip(String menuId, int type, String ids, Map<String, File> map, String titles, String descs) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("menu_id", menuId);
        privateMap.put("type", String.valueOf(type));
        privateMap.put("ids", ids);
        privateMap.put("titles", titles);
        privateMap.put("descs", descs);
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);

        Map<String, RequestBody> paramsMap = new TreeMap<>();
        Set<String> set = fieldMap.keySet();
        for (String key : set) {
            String value = fieldMap.get(key);
            paramsMap.put(key, RequestBody.create(MediaType.parse("text/plain"), value));
        }
        if (map != null && map.size() > 0) {
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                File file = map.get(key);
                paramsMap.put(key + "\"; filename=\"" + file.getName() + "\"",
                        RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }
        }
        EditTipService editTipService = ApiManager.create(EditTipService.class);
        return editTipService.edit(paramsMap);
    }

}
