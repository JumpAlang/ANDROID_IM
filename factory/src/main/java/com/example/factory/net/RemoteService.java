package com.example.factory.net;

import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.api.account.LoginModel;
import com.example.factory.model.api.account.RegisterModel;
import com.example.factory.model.api.group.GroupCreateModel;
import com.example.factory.model.api.group.GroupMemberAddModel;
import com.example.factory.model.api.message.MsgCreateModel;
import com.example.factory.model.api.user.UserUpdateModel;
import com.example.factory.model.card.GroupCard;
import com.example.factory.model.card.GroupMemberCard;
import com.example.factory.model.card.MessageCard;
import com.example.factory.model.card.UserCard;

import java.util.List;
import java.util.Observer;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * 网络请求的所有的接口
 *
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public interface RemoteService {

    /**
     * 注册接口
     */
    @POST("account/register")
    Observable<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    /**
     * 登录接口
     */
    @POST("account/login")
    Observable<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);

    /**
     * 绑定设备Id
     */
    @POST("account/bind/{pushId}")
    Observable<RspModel<AccountRspModel>> accountBind(@Path(encoded = true, value = "pushId") String pushId);

    // 用户更新的接口
    @PUT("user")
    Observable<RspModel<UserCard>> userUpdate(@Body UserUpdateModel model);

    // 用户搜索的接口
    @GET("user/search/{name}")
    Observable<RspModel<List<UserCard>>> userSearch(@Path("name") String name);

    // 用户关注接口
    @PUT("user/follow/{userId}")
    Observable<RspModel<UserCard>> userFollow(@Path("userId") String userId);

    // 用户关注接口
    @PUT("user/remove/{userId}")
    Observable<RspModel<UserCard>> userDelete(@Path("userId") String userId);

    // 获取联系人列表
    @GET("user/contact")
    Observable<RspModel<List<UserCard>>> userContacts();

    //获取某人信息
    @GET("user/{userId}")
    Call<RspModel<UserCard>> userFind(@Path("userId") String userId);

    // 发送消息的接口
    @POST("msg")
    Observable<RspModel<MessageCard>> msgPush(@Body MsgCreateModel model);

    // 创建群
    @POST("group")
    Call<RspModel<GroupCard>> groupCreate(@Body GroupCreateModel model);

    // 拉取群信息
    @GET("group/{groupId}")
    Call<RspModel<GroupCard>> groupFind(@Path("groupId") String groupId);

    // 群搜索的接口
    @GET("group/search/{name}")
    Call<RspModel<List<GroupCard>>> groupSearch(@Path(value = "name", encoded = true) String name);

    // 我的群列表
    @GET("group/list/{date}")
    Call<RspModel<List<GroupCard>>> groups(@Path(value = "date", encoded = true) String date);

    // 我的群的成员列表
    @GET("group/{groupId}/member")
    Call<RspModel<List<GroupMemberCard>>> groupMembers(@Path("groupId") String groupId);

    // 给群添加成员
    @POST("group/{groupId}/member")
    Call<RspModel<List<GroupMemberCard>>> groupMemberAdd(@Path("groupId") String groupId, @Body GroupMemberAddModel model);

    //拉取本人的课程信息
    @GET("subject")
    Observable<RspModel<String>> pullTimeData();

}
