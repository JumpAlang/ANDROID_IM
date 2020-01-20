package com.example.factory.presenter.account;

import android.text.TextUtils;
import android.util.Log;

import com.example.common.common.app.Application;
import com.example.common.factory.presenter.BasePresenter;
import com.example.factory.BaseObserver;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.data.helper.AccountHelper;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.api.account.LoginModel;
import com.example.factory.persistence.Account;

import static com.example.factory.data.helper.AccountHelper.bindPush;


/**
 * 登录的逻辑实现
 *
 * @author sunhaobin
 * @version 1.0.0
 */
public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter{
    
    public static final String TAG="LoginPresenter";
    
    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {
        start();

        final LoginContract.View view = getView();

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            view.showError(R.string.data_account_login_invalid_parameter);
        } else {
            // 尝试传递PushId
            LoginModel model = new LoginModel(phone, password, Account.getPushId());
            AccountHelper.login(model, new loginObserver());
        }
    }

    /**
     * 登陆状态观察者
     */
    class loginObserver extends BaseObserver<AccountRspModel> {
        final LoginContract.View view = getView();
        @Override
        public void onNext(RspModel<AccountRspModel> rspModel) {
            super.onNext(rspModel);
            if (view == null)
                return;
            if (rspModel.success()) {
                // 拿到实体
                AccountRspModel accountRspModel = rspModel.getResult();

                // 判断绑定状态，是否绑定设备
                if (accountRspModel.isBind()) {
                    // 设置绑定状态为True
                    Account.setBind(true);
                    view.loginSuccess();
                } else {
                    // 进行绑定的唤起
                    bindPush(new bindObserver());
                }
            } else {
                int id = Factory.decodeRspCode(rspModel);
                view.showError(id);
            }
        }
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            if (view == null)
                return;
            view.showError(R.string.data_network_error);
        }
    }

    /**
     * 绑定ID观察者
     */
    class bindObserver extends BaseObserver<AccountRspModel>{

        final LoginContract.View view = getView();

        @Override
        public void onNext(RspModel<AccountRspModel> rspModel) {
            super.onNext(rspModel);
            if (rspModel.success()){
                AccountRspModel result = rspModel.getResult();
                // 判断绑定状态，是否绑定设备
                if (result.isBind()) {
                    // 设置绑定状态为True
                    Account.setBind(true);
                    view.loginSuccess();
                }
            }else {
                int id = Factory.decodeRspCode(rspModel);
                view.showError(id);
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            if (view != null)
                view.showError(R.string.data_network_error);
        }
    }
}
