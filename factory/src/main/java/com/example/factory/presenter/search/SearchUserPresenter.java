package com.example.factory.presenter.search;

import android.util.Log;

import com.example.common.factory.data.DataSource;
import com.example.common.factory.presenter.BasePresenter;
import com.example.factory.BaseObserver;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.card.UserCard;
import java.util.List;

/**
 * 搜索人的实现
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class SearchUserPresenter extends BasePresenter<SearchContract.UserView>
        implements SearchContract.Presenter {
    public static final String TAG="SearchUserPresenter";

    public SearchUserPresenter(SearchContract.UserView view) {
        super(view);
    }

    @Override
    public void search(String content) {
        Log.d(TAG, "search: "+content);
        start();
        UserHelper.search(content, new searchObserver());
    }

    class searchObserver extends BaseObserver<List<UserCard>>{
        final SearchContract.UserView view = getView();
        @Override
        public void onNext(RspModel<List<UserCard>> rspModel) {
            if (view == null)
                return;
            if (rspModel.success()) {
                // 拿到实体
                List<UserCard> userCards = rspModel.getResult();
                view.onSearchDone(userCards);

            } else {
                int id = Factory.decodeRspCode(rspModel);
                view.showError(id);
            }
            super.onNext(rspModel);
        }

        @Override
        public void onError(Throwable e) {
            view.showError(R.string.data_network_error);
            super.onError(e);
        }
    }
}
