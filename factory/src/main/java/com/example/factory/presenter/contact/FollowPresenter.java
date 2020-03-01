package com.example.factory.presenter.contact;

import android.util.Log;

import com.example.common.factory.presenter.BasePresenter;
import com.example.factory.BaseObserver;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.card.UserCard;
import com.example.factory.presenter.search.SearchContract;

import io.reactivex.Observer;

public class FollowPresenter extends BasePresenter<FollowContact.View> implements FollowContact.Presenter {
    public static final String TAG="FollowPresenter";
    public FollowPresenter(FollowContact.View view) {
        super(view);
    }
    followObserver followObserver;
    @Override
    public void follow(String id) {
        Log.d(TAG, "follow: ");
        followObserver=new followObserver();
        UserHelper.follow(id,followObserver);
    }
    class followObserver extends BaseObserver<UserCard> {
        final FollowContact.View view = getView();
        @Override
        public void onNext(RspModel<UserCard> rspModel) {
            super.onNext(rspModel);
            if (view == null)
                return;
            if (rspModel.success()) {
                UserCard userCard = rspModel.getResult();
                view.onFollowSucceed(userCard);
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
        if(followObserver!=null&&followObserver.getDisposable()!=null)
            if(!followObserver.getDisposable().isDisposed())
                followObserver.getDisposable().dispose();
    }
}
