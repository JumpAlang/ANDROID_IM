package com.example.factory;

import androidx.annotation.StringRes;
import com.example.common.common.app.Application;
import com.example.common.factory.data.DataSource;
import com.example.factory.model.api.RspModel;
import com.example.factory.persistence.Account;
import com.example.factory.utils.DBFlowExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class Factory {
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
     * 收到账户退出的消息需要进行账户退出重新登录
     */
    private void logout() {

    }


    /**
     * 处理推送来的消息
     *
             * @param message 消息
     */
    public static void dispatchPush(String message) {
        // TODO
    }

}
