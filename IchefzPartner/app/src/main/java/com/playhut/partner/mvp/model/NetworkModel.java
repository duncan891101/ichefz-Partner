package com.playhut.partner.mvp.model;

import android.text.TextUtils;

import com.playhut.partner.network.ApiManager;
import com.playhut.partner.network.FieldMapManager;
import com.playhut.partner.networkservice.AddSetService;
import com.playhut.partner.networkservice.ChangeMenuStateService;
import com.playhut.partner.networkservice.ConfirmOrderService;
import com.playhut.partner.networkservice.ConfirmRefundService;
import com.playhut.partner.networkservice.DeleteMenuService;
import com.playhut.partner.networkservice.DeleteMsgService;
import com.playhut.partner.networkservice.EditBankInfoService;
import com.playhut.partner.networkservice.EditKitchenAddressService;
import com.playhut.partner.networkservice.EditPersonInfoService;
import com.playhut.partner.networkservice.EditRestaurantInfoService;
import com.playhut.partner.networkservice.FeedbackCenterService;
import com.playhut.partner.networkservice.ForgotPwdService;
import com.playhut.partner.networkservice.GetMenuPackListService;
import com.playhut.partner.networkservice.GetMenuSetListService;
import com.playhut.partner.networkservice.GetRestaurantInfoService;
import com.playhut.partner.networkservice.HistoryOrderService;
import com.playhut.partner.networkservice.LoginService;
import com.playhut.partner.networkservice.LogoutService;
import com.playhut.partner.networkservice.MainInfoService;
import com.playhut.partner.networkservice.MainOpenStateService;
import com.playhut.partner.networkservice.MessageDetailService;
import com.playhut.partner.networkservice.MessageListService;
import com.playhut.partner.networkservice.ModifyPwdService;
import com.playhut.partner.networkservice.NewOrderService;
import com.playhut.partner.networkservice.RefundService;
import com.playhut.partner.networkservice.RejectRefundService;
import com.playhut.partner.networkservice.ReplyService;
import com.playhut.partner.networkservice.SelectPackService;
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

    /**
     * 修改银行卡信息
     */
    public Observable<ResponseBody> editBankInfo(String name, String routingNum, String accountNum, String tax, String ssn) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("name", name);
        privateMap.put("routing_number", routingNum);
        privateMap.put("account_number", accountNum);
        privateMap.put("tax", tax);
        privateMap.put("ssn", ssn);
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        EditBankInfoService editBankInfoService = ApiManager.create(EditBankInfoService.class);
        return editBankInfoService.edit(fieldMap);
    }

    /**
     * 获取所有可以组装成set的pack
     */
    public Observable<ResponseBody> selectPack() {
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(null);
        SelectPackService selectPackService = ApiManager.create(SelectPackService.class);
        return selectPackService.getList(fieldMap);
    }

    /**
     * Add Set
     */
    public Observable<ResponseBody> addSet(String packIds, String setName, String desc, String person2, String person4, String maxQuantity) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("pack_ids", packIds);
        privateMap.put("title", setName);
        privateMap.put("desc", desc);
        privateMap.put("person2", person2);
        privateMap.put("person4", person4);
        privateMap.put("max_quantity", maxQuantity);
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        AddSetService addSetService = ApiManager.create(AddSetService.class);
        return addSetService.add(fieldMap);
    }

    /**
     * New order
     */
    public Observable<ResponseBody> getNewOrderList(String page, String pageSize) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("page", page);
        privateMap.put("page_size", pageSize);
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        NewOrderService newOrderService = ApiManager.create(NewOrderService.class);
        return newOrderService.getList(fieldMap);
    }

    /**
     * 确认订单
     */
    public Observable<ResponseBody> confirmOrder(String orderId) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("order_id", orderId);
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        ConfirmOrderService confirmOrderService = ApiManager.create(ConfirmOrderService.class);
        return confirmOrderService.confirm(fieldMap);
    }

    /**
     * 拒绝订单
     */
    public Observable<ResponseBody> refundOrder(String orderId, String reason) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("order_id", orderId);
        privateMap.put("refund_reason", reason);
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        RefundService refundService = ApiManager.create(RefundService.class);
        return refundService.refund(fieldMap);
    }

    /**
     * 其他订单
     */
    public Observable<ResponseBody> getHistoryOrderList(String state, int page, int pageSize) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("state", state);
        privateMap.put("page", String.valueOf(page));
        privateMap.put("page_size", String.valueOf(pageSize));
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        HistoryOrderService historyOrderService = ApiManager.create(HistoryOrderService.class);
        return historyOrderService.getList(fieldMap);
    }

    /**
     * 确认退款
     */
    public Observable<ResponseBody> confirmRefund(String orderId) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("order_id", orderId);
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        ConfirmRefundService confirmRefundService = ApiManager.create(ConfirmRefundService.class);
        return confirmRefundService.refund(fieldMap);
    }

    /**
     * 拒绝退款
     */
    public Observable<ResponseBody> rejectRefund(String orderId) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("order_id", orderId);
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        RejectRefundService rejectRefundService = ApiManager.create(RejectRefundService.class);
        return rejectRefundService.reject(fieldMap);
    }

    /**
     * 用户消息列表
     */
    public Observable<ResponseBody> getMessageList(int page, int pageSize) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("page", String.valueOf(page));
        privateMap.put("page_size", String.valueOf(pageSize));
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        MessageListService messageListService = ApiManager.create(MessageListService.class);
        return messageListService.getList(fieldMap);
    }

    /**
     * 消息明细
     */
    public Observable<ResponseBody> getMessageDetail(String senderId, int page, int pageSize) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("sender_id", senderId);
        privateMap.put("page", String.valueOf(page));
        privateMap.put("page_size", String.valueOf(pageSize));
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        MessageDetailService messageDetailService = ApiManager.create(MessageDetailService.class);
        return messageDetailService.getList(fieldMap);
    }

    /**
     * 回复消息
     */
    public Observable<ResponseBody> replyMessage(String senderId, String content, String parentId) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("sender_id", senderId);
        privateMap.put("content", content);
        privateMap.put("parent_id", parentId);
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap, true);
        ReplyService replyService = ApiManager.create(ReplyService.class);
        return replyService.reply(fieldMap);
    }

    /**
     * 删除消息
     */
    public Observable<ResponseBody> deleteMsg(String messageId) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("message_id", messageId);
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        DeleteMsgService deleteMsgService = ApiManager.create(DeleteMsgService.class);
        return deleteMsgService.delete(fieldMap);
    }

    /**
     * My menu set list
     */
    public Observable<ResponseBody> getMenuSetList(int page, int pageSize) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("page", String.valueOf(page));
        privateMap.put("page_size", String.valueOf(pageSize));
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        GetMenuSetListService getMenuSetListService = ApiManager.create(GetMenuSetListService.class);
        return getMenuSetListService.getList(fieldMap);
    }

    /**
     * My menu pack list
     */
    public Observable<ResponseBody> getMenuPackList(int page, int pageSize) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("page", String.valueOf(page));
        privateMap.put("page_size", String.valueOf(pageSize));
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        GetMenuPackListService getMenuPackListService = ApiManager.create(GetMenuPackListService.class);
        return getMenuPackListService.getList(fieldMap);
    }

    /**
     * 删除菜单
     */
    public Observable<ResponseBody> deleteMenu(String menuId, String mtype) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("menu_id", menuId);
        privateMap.put("mtype", mtype);
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        DeleteMenuService deleteMenuService = ApiManager.create(DeleteMenuService.class);
        return deleteMenuService.delete(fieldMap);
    }

    /**
     * 修改菜的状态
     */
    public Observable<ResponseBody> changeMenuState(String menuId, String mtype, String state) {
        Map<String, String> privateMap = new TreeMap<>();
        privateMap.put("menu_id", menuId);
        privateMap.put("mtype", mtype);
        privateMap.put("state", state);
        Map<String, String> fieldMap = FieldMapManager.getFieldMap(privateMap);
        ChangeMenuStateService changeMenuStateService = ApiManager.create(ChangeMenuStateService.class);
        return changeMenuStateService.change(fieldMap);
    }

}
