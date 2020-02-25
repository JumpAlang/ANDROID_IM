package com.example.factory.data.helper;


import android.util.Log;

import com.example.common.factory.data.DataSource;
import com.example.factory.BaseObserver;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.data.user.UserDispatcher;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.api.user.UserUpdateModel;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.AppDatabase;
import com.example.factory.model.db.User;
import com.example.factory.model.db.User_Table;
import com.example.factory.model.db.view.UserSampleModel;
import com.example.factory.net.Network;
import com.example.factory.net.RemoteService;
import com.example.factory.persistence.Account;
import com.example.factory.presenter.contact.ContactContract;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import java.util.ArrayList;
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
 * @author 1050483859@qq.com
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
                        Factory.getUserCenter().dispatch(userCard);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    // 刷新联系人的操作
    public static void refreshContacts() {
        RemoteService service = Network.remote();
        Observable<RspModel<List<UserCard>>> rspModelObservable = service.userContacts();

        // 网络请求
        rspModelObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<UserCard>>() {
                    @Override
                    public void onNext(RspModel<List<UserCard>> rspModel) {
                        super.onNext(rspModel);
                        if (rspModel.success()) {
                            List<UserCard> userCards = rspModel.getResult();
                            // 转换为User
                            final List<User> users = new ArrayList<>();
                            for (UserCard userCard : userCards) {
                                users.add(userCard.build());
                                Log.d(TAG, "onNext: "+userCard.toString());
                            }
                            UserDispatcher.instance().dispatch(userCards.toArray(new UserCard[0]));
                        }
                    }
                });
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
//                Factory.getUserCenter().dispatch();
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
    // 获取一个联系人列表，
    // 但是是一个简单的数据的
    public static List<UserSampleModel> getSampleContact() {
        //"select id = ??";
        //"select User_id = ??";
        return SQLite.select(User_Table.id.withTable().as("id"),
                User_Table.name.withTable().as("name"),
                User_Table.portrait.withTable().as("portrait"))
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .queryCustomList(UserSampleModel.class);
    }
}
