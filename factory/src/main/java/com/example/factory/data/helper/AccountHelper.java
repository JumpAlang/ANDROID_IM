package com.example.factory.data.helper;

import android.text.TextUtils;
import android.util.Log;


import com.example.common.factory.data.DataSource;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.api.account.LoginModel;
import com.example.factory.model.api.account.RegisterModel;
import com.example.factory.model.db.User;
import com.example.factory.net.Network;
import com.example.factory.net.RemoteService;
import com.example.factory.persistence.Account;


import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public class AccountHelper {
    public static final String TAG="AccountHelper";
    /**
     * 注册的接口，异步的调用
     *
     * @param model    传递一个注册的Model进来
     * @param observer 成功与失败的接口回送
     */
    public static void register(final RegisterModel model, Observer<RspModel<AccountRspModel>> observer) {
        // 调用Retrofit对我们的网络请求接口做代理
        RemoteService service = Network.remote();
        // 得到一个Call
        Observable<RspModel<AccountRspModel>> rspModelObservable = service.accountRegister(model);
        // 异步的请求
        rspModelObservable.subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<RspModel<AccountRspModel>>() {
                    @Override
                    public void accept(RspModel<AccountRspModel> rspModel) throws Exception {
                        {
                            if (rspModel.success()) {
                                // 拿到实体
                                AccountRspModel accountRspModel = rspModel.getResult();
                                // 获取我的信息
                                User user = accountRspModel.getUser();
                                user.save();
                                // 同步到XML持久化中
                                Account.login(accountRspModel);
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 登录的调用
     *
     * @param model    登录的Model
     * @param observer 成功与失败的接口回送
     */
    public static void login(final LoginModel model, Observer<RspModel<AccountRspModel>> observer) {
        // 调用Retrofit对我们的网络请求接口做代理
        RemoteService service = Network.remote();
        // 得到一个Call
        Observable<RspModel<AccountRspModel>> rspModelObservable = service.accountLogin(model);
        // 异步的请求
        rspModelObservable.subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<RspModel<AccountRspModel>>() {
                    @Override
                    public void accept(RspModel<AccountRspModel> rspModel) throws Exception {
                        {
                            if (rspModel.success()) {
                                // 拿到实体
                                AccountRspModel accountRspModel = rspModel.getResult();
                                // 获取我的信息
                                User user = accountRspModel.getUser();
                                user.save();
                                // 同步到XML持久化中
                                Account.login(accountRspModel);
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    /**
     * 对设备Id进行绑定的操作
     *
     * @param observer
     *
     */
    public static void bindPush(Observer<RspModel<AccountRspModel>> observer) {
        // 检查是否为空
        String pushId = Account.getPushId();
        if (TextUtils.isEmpty(pushId))
            return;

        // 调用Retrofit对我们的网络请求接口做代理
        RemoteService service = Network.remote();
        Observable<RspModel<AccountRspModel>> rspModelObservable = service.accountBind(pushId);
        rspModelObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static void bindPush() {
        // 检查是否为空
        String pushId = Account.getPushId();
        if (TextUtils.isEmpty(pushId))
            return;

        // 调用Retrofit对我们的网络请求接口做代理
        RemoteService service = Network.remote();
        Observable<RspModel<AccountRspModel>> rspModelObservable = service.accountBind(pushId);
        rspModelObservable.subscribeOn(Schedulers.newThread())
                .subscribe();
    }

}
