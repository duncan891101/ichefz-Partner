package com.playhut.partner.mvp.model;

import android.text.TextUtils;

import com.playhut.partner.network.ApiManager;
import com.playhut.partner.network.FieldMapManager;
import com.playhut.partner.networkservice.EditKitchenAddressService;
import com.playhut.partner.networkservice.EditPersonInfoService;
import com.playhut.partner.networkservice.EditRestaurantInfoService;
import com.playhut.partner.networkservice.FeedbackCenterService;
import com.playhut.partner.networkservice.ForgotPwdService;
import com.playhut.partner.networkservice.GetRestaurantInfoService;
import com.playhut.partner.networkservice.LoginService;
import com.playhut.partner.networkservice.LogoutService;
import com.playhut.partner.networkservice.MainInfoService;
import com.playhut.partner.networkservice.MainOpenStateService;
import com.playhut.partner.networkservice.ModifyPwdService;
import com.playhut.partner.networkservice.SignUpService;

import java.util.Map;
import java.util.TreeMap;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * 网络层
 */
public class NetworkModel {

    /**
     * 注册
     */
    public Observable<ResponseBody> signUp(String email, String pwd, String channelId,
                                           String firstName, String lastName, String phoneNumber) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("email", email);
        privateMap.put("password", pwd);
        privateMap.put("channelId", channelId);
        privateMap.put("device_type", "android");
        privateMap.put("first_name", firstName);
        privateMap.put("last_name", lastName);
        privateMap.put("phone_number", phoneNumber);
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        SignUpService signUpService = ApiManager.create(SignUpService.class);
        return signUpService.signUp(fieldMap);
    }

    /**
     * 登录
     */
    public Observable<ResponseBody> login(String email, String pwd, String channelId) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("email", email);
        privateMap.put("password", pwd);
        privateMap.put("channelId", channelId);
        privateMap.put("device_type", "android");
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        LoginService loginService = ApiManager.create(LoginService.class);
        return loginService.login(fieldMap);
    }

    /**
     * 忘记密码
     */
    public Observable<ResponseBody> forgotPwd(String email) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("email", email);
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        ForgotPwdService forgotPwdService = ApiManager.create(ForgotPwdService.class);
        return forgotPwdService.forgotPwd(fieldMap);
    }

    /**
     * 首页信息
     */
    public Observable<ResponseBody> getMainInfo() {
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(null);
        MainInfoService mainInfoService = ApiManager.create(MainInfoService.class);
        return mainInfoService.getInfo(fieldMap);
    }

    /**
     * 首页营业状态
     */
    public Observable<ResponseBody> setOpenState(int state) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("open_state", String.valueOf(state));
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        MainOpenStateService mainOpenStateService = ApiManager.create(MainOpenStateService.class);
        return mainOpenStateService.setOpenState(fieldMap);
    }

    /**
     * 退出登录
     */
    public Observable<ResponseBody> logout(String channelId) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("channelId", channelId);
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        LogoutService logoutService = ApiManager.create(LogoutService.class);
        return logoutService.logout(fieldMap);
    }

    /**
     * 意见反馈中心
     */
    public Observable<ResponseBody> feedbackCenter(String content) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("content", content);
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        FeedbackCenterService feedbackCenterService = ApiManager.create(FeedbackCenterService.class);
        return feedbackCenterService.send(fieldMap);
    }

    /**
     * 获取厨房信息
     */
    public Observable<ResponseBody> getRestaurantInfo() {
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(null);
        GetRestaurantInfoService getRestaurantInfoService = ApiManager.create(GetRestaurantInfoService.class);
        return getRestaurantInfoService.get(fieldMap);
    }

    /**
     * 修改厨房信息
     */
    public Observable<ResponseBody> editRestaurantInfo(String content, int changeType) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("chef_info", content);
        privateMap.put("change_type", String.valueOf(changeType));
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        EditRestaurantInfoService editRestaurantInfoService = ApiManager.create(EditRestaurantInfoService.class);
        return editRestaurantInfoService.edit(fieldMap);
    }

    /**
     * 修改个人信息
     */
    public Observable<ResponseBody> editPersonInfo(String content, int changeType) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("chef_info", content);
        privateMap.put("change_type", String.valueOf(changeType));
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        EditPersonInfoService editPersonInfoService = ApiManager.create(EditPersonInfoService.class);
        return editPersonInfoService.edit(fieldMap);
    }

    /**
     * 修改密码
     */
    public Observable<ResponseBody> modifyPwd(String oldPwd, String newPwd) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("old_pwd", oldPwd);
        privateMap.put("new_pwd", newPwd);
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        ModifyPwdService modifyPwdService = ApiManager.create(ModifyPwdService.class);
        return modifyPwdService.modify(fieldMap);
    }

    /**
     * 修改厨房地址
     */
    public Observable<ResponseBody> editKitchenAddress(String country, String street, String apt, String city, String state, String zipCode) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("country", country);
        privateMap.put("street_address", street);
        if (!TextUtils.isEmpty(apt)){
            privateMap.put("apt", apt);
        }
        privateMap.put("city", city);
        privateMap.put("state", state);
        privateMap.put("zip_code", zipCode);
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        EditKitchenAddressService editKitchenAddressService = ApiManager.create(EditKitchenAddressService.class);
        return editKitchenAddressService.edit(fieldMap);
    }

}
