package com.example.factory.data.helper;


import com.example.common.factory.data.DataSource;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.api.user.UserUpdateModel;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;
import com.example.factory.model.db.User_Table;
import com.example.factory.net.Network;
import com.example.factory.net.RemoteService;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
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
        Observable<RspModel<UserCard>> rspModelObservable = service.userUpdate(model);

        // 网络请求
        rspModelObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    // 搜索好友的方法
    public static void search(String name, Observer<RspModel<List<UserCard>>> observer) {
        // 调用Retrofit对我们的网络请求接口做代理
        RemoteService service = Network.remote();
        Observable<RspModel<List<UserCard>>> rspModelObservable = service.userSearch(name);

        // 网络请求
        rspModelObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    // 关注的网络请求
    public static void follow(String id,Observer<RspModel<UserCard>> observer) {
        RemoteService service = Network.remote();
        Observable<RspModel<UserCard>> rspModelObservable = service.userFollow(id);

        // 网络请求
        rspModelObservable.subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<RspModel<UserCard>>() {
                    @Override
                    public void accept(RspModel<UserCard> rspModel) throws Exception {
                        UserCard userCard = rspModel.getResult();
                        // 保存到本地数据库
                        User user = userCard.build();
                        user.save();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    // 刷新联系人的操作
    public static void refreshContacts(final Observer<RspModel<List<UserCard>>> observer) {
        RemoteService service = Network.remote();
        Observable<RspModel<List<UserCard>>> rspModelObservable = service.userContacts();

        // 网络请求
        rspModelObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    // 从本地查询一个用户的信息
    public static User findFromLocal(String id) {
        return SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(id))
                .querySingle();
    }

    public static User findFromNet(String id) {

        RemoteService remoteService = Network.remote();
        try {
            Response<RspModel<UserCard>> response = remoteService.userFind(id).execute();
            UserCard card = response.body().getResult();
            if (card != null) {

                // TODO 数据库的存储但是没有通知
                User user = card.build();
                user.save();

                return user;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 搜索一个用户，优先本地缓存，
     * 没有用然后再从网络拉取
     */
    public static User search(String id) {
        User user = findFromLocal(id);
        if (user == null) {
            return findFromNet(id);
        }
        return user;
    }

    /**
     * 搜索一个用户，优先网络查询
     * 没有用然后再从本地缓存拉取
     */
    public static User searchFirstOfNet(String id) {
        User user = findFromNet(id);
        if (user == null) {
            return findFromLocal(id);
        }
        return user;
    }

}
