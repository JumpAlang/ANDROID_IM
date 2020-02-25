package com.example.factory.presenter.message;


import com.example.common.factory.presenter.BaseContract;
import com.example.factory.model.db.Session;

/**
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public interface SessionContract {

    interface Presenter extends BaseContract.Presenter {
        void clearUnReadCount(Session session);
    }

    // 都在基类完成了
    interface View extends BaseContract.RecyclerView<Presenter, Session> {

    }
}
