package com.example.factory;

import android.util.Log;

import com.example.common.common.app.Application;
import com.example.factory.data.group.GroupCenter;
import com.example.factory.data.group.GroupDispatcher;
import com.example.factory.data.message.MessageCenter;
import com.example.factory.data.message.MessageDispatcher;
import com.example.factory.data.user.UserCenter;
import com.example.factory.data.user.UserDispatcher;
import com.example.factory.model.api.PushModel;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.card.GroupCard;
import com.example.factory.model.card.GroupMemberCard;
import com.example.factory.model.card.MessageCard;
import com.example.factory.model.card.UserCard;
import com.example.factory.persistence.Account;
import com.example.factory.utils.DBFlowExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public class Factory {
    public static final String TAG="Factory";

    // 单例模式ø
    private static final Factory instance;
    // 全局的线程池
    private final Executor executor;
    // 全局的Gson
    private final Gson gson;


    static {
        instance = new Factory();
    }

    private Factory() {
        // 新建一个4个线程的线程池
        executor = Executors.newFixedThreadPool(4);
        gson = new GsonBuilder()
                // 设置时间格式
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                // 设置一个过滤器，数据库级别的Model不进行Json转换
                .setExclusionStrategies(new DBFlowExclusionStrategy())
                .create();
    }

    /**
     * Factory 中的初始化
     */
    public static void setup() {
        // 初始化数据库
        FlowManager.init(new FlowConfig.Builder(app())
                .openDatabasesOnInit(true) // 数据库初始化的时候就开始打开
                .build());

        // 持久化的数据进行初始化
        Account.load(app());
    }

    /**
     * 返回全局的Application
     *
     * @return Application
     */
    public static Application app() {
        return Application.getInstance();
    }


    /**
     * 异步运行的方法
     *
     * @param runnable Runnable
     */
    public static void runOnAsync(Runnable runnable) {
        // 拿到单例，拿到线程池，然后异步执行
        instance.executor.execute(runnable);
    }

    /**
     * 返回一个全局的Gson，在这可以进行Gson的一些全局的初始化
     *
     * @return Gson
     */
    public static Gson getGson() {
        return instance.gson;
    }


    /**
     * 进行错误Code的解析，
     * 把网络返回的Code值进行统一的规划并返回为一个String资源
     *
     * @param model    RspModel
     */
    public static int decodeRspCode(RspModel model) {
        if (model == null)
            return 0;

        // 进行Code区分
        switch (model.getCode()) {
            case RspModel.SUCCEED:
                return 0;
            case RspModel.ERROR_SERVICE:
                return R.string.data_rsp_error_service;
            case RspModel.ERROR_NOT_FOUND_USER:
                return R.string.data_rsp_error_not_found_user;
            case RspModel.ERROR_NOT_FOUND_GROUP:
                return R.string.data_rsp_error_not_found_group;
            case RspModel.ERROR_NOT_FOUND_GROUP_MEMBER:
                return R.string.data_rsp_error_not_found_group_member;
            case RspModel.ERROR_CREATE_USER:
                return R.string.data_rsp_error_create_user;
            case RspModel.ERROR_CREATE_GROUP:
                return R.string.data_rsp_error_create_group;
            case RspModel.ERROR_CREATE_MESSAGE:
                return R.string.data_rsp_error_create_message;
            case RspModel.ERROR_PARAMETERS:
                return R.string.data_rsp_error_parameters;
            case RspModel.ERROR_PARAMETERS_EXIST_ACCOUNT:
                return R.string.data_rsp_error_parameters_exist_account;
            case RspModel.ERROR_PARAMETERS_EXIST_NAME:
                return R.string.data_rsp_error_parameters_exist_name;
            case RspModel.ERROR_ACCOUNT_TOKEN:
                instance.logout();
                return R.string.data_rsp_error_account_token;
            case RspModel.ERROR_ACCOUNT_LOGIN:
                return R.string.data_rsp_error_account_login;
            case RspModel.ERROR_ACCOUNT_REGISTER:
                return R.string.data_rsp_error_account_register;
            case RspModel.ERROR_ACCOUNT_NO_PERMISSION:
                return R.string.data_rsp_error_account_no_permission;
            case RspModel.ERROR_UNKNOWN:
            default:
                return R.string.data_rsp_error_unknown;
        }
    }
    /**
     * 处理推送来的消息
     *
     * @param str 消息
     */
    public static void dispatchPush(String str) {
        // 首先检查登录状态
        if (!Account.isLogin())
            return;

        PushModel model = PushModel.decode(str);
        if (model == null)
            return;

        // 对推送集合进行遍历
        for (PushModel.Entity entity : model.getEntities()) {
            Log.e(TAG, "dispatchPush-Entity:" + entity.toString());

            switch (entity.type) {
                case PushModel.ENTITY_TYPE_LOGOUT:
                    instance.logout();
                    // 退出情况下，直接返回，并且不可继续
                    return;

                case PushModel.ENTITY_TYPE_MESSAGE: {
                    // 普通消息
                    MessageCard card = getGson().fromJson(entity.content, MessageCard.class);
                    getMessageCenter().dispatch(card);
                    break;
                }

                case PushModel.ENTITY_TYPE_ADD_FRIEND: {
                    // 好友添加
                    UserCard card = getGson().fromJson(entity.content, UserCard.class);
                    getUserCenter().dispatch(card);
                    break;
                }

                case PushModel.ENTITY_TYPE_ADD_GROUP: {
                    // 添加群
                    GroupCard card = getGson().fromJson(entity.content, GroupCard.class);
                    getGroupCenter().dispatch(card);
                    break;
                }

                case PushModel.ENTITY_TYPE_ADD_GROUP_MEMBERS:
                case PushModel.ENTITY_TYPE_MODIFY_GROUP_MEMBERS: {
                    // 群成员变更, 回来的是一个群成员的列表
                    Type type = new TypeToken<List<GroupMemberCard>>() {
                    }.getType();
                    List<GroupMemberCard> card = getGson().fromJson(entity.content, type);
                    // 把数据集合丢到数据中心处理
                    getGroupCenter().dispatch(card.toArray(new GroupMemberCard[0]));
                    break;
                }
                case PushModel.ENTITY_TYPE_EXIT_GROUP_MEMBERS: {
                }
            }
        }
    }

    /**
     * 收到账户退出的消息需要进行账户退出重新登录
     */
    public static void logout() {
        Account.clear(Application.getInstance());
    }



    /**
     * 获取一个用户中心的实现类
     *
     * @return 用户中心的规范接口
     */
    public static UserCenter getUserCenter() {
        return UserDispatcher.instance();
    }
    public static GroupCenter getGroupCenter() {
        return GroupDispatcher.instance();
    }
    public static MessageCenter getMessageCenter() {
        return MessageDispatcher.instance();
    }

}
