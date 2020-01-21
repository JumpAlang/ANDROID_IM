package com.example.factory.data.helper;


import com.example.common.factory.data.DataSource;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.api.user.UserUpdateModel;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;
import com.example.factory.net.Network;
import com.example.factory.net.RemoteService;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class UserHelper {
    // 更新用户信息的操作，异步的
    public static void update(UserUpdateModel model, Observer<RspModel<UserCard>> observer) {
        // 调用Retrofit对我们的网络请求接口做代理
        RemoteService service = Network.remote();
        // 得到一个Call
        Observable<RspModel<UserCard>> rspModelObservable = service.userUpdate(model);
        // 网络请求
        rspModelObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
