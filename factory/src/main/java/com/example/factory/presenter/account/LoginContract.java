package com.example.factory.presenter.account;

import com.example.common.factory.presenter.BaseContract;

/**
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public interface LoginContract {
    interface View extends BaseContract.View<Presenter> {
        // 登录成功
        void loginSuccess();
    }


    interface Presenter extends BaseContract.Presenter {
        // 发起一个登录
        void login(String phone, String password);
    }

}
