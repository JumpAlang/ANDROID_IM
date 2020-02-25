package com.example.factory.presenter.contact;

import com.example.common.factory.presenter.BaseContract;
import com.example.factory.model.db.User;

/**
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public interface PersonalContract {
    interface Presenter extends BaseContract.Presenter {
        // 获取用户信息
        User getUserPersonal();
    }

    interface View extends BaseContract.View<Presenter> {
        String getUserId();

        // 加载数据完成
        void onLoadDone(User user);

        // 设置关注状态
        void setFollowStatus(boolean isFollow);
    }
}
