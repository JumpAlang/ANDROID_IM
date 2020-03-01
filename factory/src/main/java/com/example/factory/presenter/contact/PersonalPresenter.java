package com.example.factory.presenter.contact;

import android.util.Log;

import com.example.common.factory.presenter.BasePresenter;
import com.example.factory.BaseObserver;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;
import com.example.factory.persistence.Account;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public class PersonalPresenter extends BasePresenter<PersonalContract.View>
        implements PersonalContract.Presenter {
    public static final String TAG="PersonalPresenter";
    private User user;

    public PersonalPresenter(PersonalContract.View view) {
        super(view);
    }
    DeleteObserver mDeleteObserver;

    @Override
    public void start() {
        super.start();

//        // 个人界面用户数据优先从网络拉取
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                PersonalContract.View view = getView();
                if (view != null) {
                    String id = view.getUserId();
                    User user = UserHelper.searchFirstOfNet(id);
                    onLoaded(view, user);
                }
            }
        });

    }


    private void onLoaded(final PersonalContract.View view, final User user) {
        this.user = user;
        // 是否就是我自己
        final boolean isSelf = user.getId().equalsIgnoreCase(Account.getUserId());
        // 是否已经关注
        final boolean isFollow = isSelf || user.isFollow();
        // 已经关注同时不是自己才能聊天
        final boolean allowSayHello = isFollow && !isSelf;

        Scheduler scheduler = AndroidSchedulers.mainThread();
        scheduler.scheduleDirect(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: "+user.toString());
                view.onLoadDone(user);
            }
        });
    }

    @Override
    public User getUserPersonal() {
        return user;
    }

    @Override
    public void deleteUser() {
        Log.d(TAG, "deleteUser: ");
        mDeleteObserver=new DeleteObserver();
        UserHelper.delete(getView().getUserId(),mDeleteObserver);
    }
    class DeleteObserver extends BaseObserver<UserCard> {
        final PersonalContract.View view = getView();
        @Override
        public void onNext(RspModel<UserCard> rspModel) {
            super.onNext(rspModel);
            if (view == null)
                return;
            if (rspModel.success()) {
                Log.d(TAG, "onNext: success");
                view.setFollowStatus(false);
            } else {
                int id = Factory.decodeRspCode(rspModel);
                view.showError(id);
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            view.showError(R.string.data_network_error);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if(mDeleteObserver!=null)
            mDeleteObserver.getDisposable().dispose();
    }
}
