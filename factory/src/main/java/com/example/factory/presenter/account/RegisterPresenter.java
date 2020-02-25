package com.example.factory.presenter.account;

import android.text.TextUtils;
import android.util.Log;

import com.example.common.common.Common;
import com.example.common.factory.data.DataSource;
import com.example.common.factory.presenter.BasePresenter;
import com.example.factory.BaseObserver;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.data.helper.AccountHelper;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.api.account.RegisterModel;
import com.example.factory.model.db.User;
import com.example.factory.persistence.Account;


import java.util.regex.Pattern;

import static com.example.factory.data.helper.AccountHelper.bindPush;

/**
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public class RegisterPresenter extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter {
    public static final String TAG = "RegisterPresenter";

    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    public void register(String phone, String name, String password) {
        Log.d(TAG, "register phone:"+phone+",name:"+name+",password:"+password);
        // 调用开始方法，在start中默认启动了Loading
        start();

        // 得到View接口
        RegisterContract.View view = getView();

        // 校验
        if (!checkMobile(phone)) {
            // 提示
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        } else if (name.length() < 2) {
            // 姓名需要大于2位
            view.showError(R.string.data_account_register_invalid_parameter_name);
        } else if (password.length() < 6) {
            // 密码需要大于6位
            view.showError(R.string.data_account_register_invalid_parameter_password);
        } else {
            // 进行网络请求
            // 构造Model，进行请求调用
            RegisterModel model = new RegisterModel(phone, password, name, Account.getPushId());
            // 进行网络请求，并设置回送接口为自己
            AccountHelper.register(model, new RegisterObserver());
        }
    }
    class RegisterObserver extends BaseObserver<AccountRspModel>{
        final RegisterContract.View view = getView();
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
                    view.registerSuccess();
                } else {
                    // 进行绑定的唤起
                    bindPush(new BaseObserver<AccountRspModel>() {
                        @Override
                        public void onNext(RspModel<AccountRspModel> accountRspModelRspModel) {
                            super.onNext(accountRspModelRspModel);
                            Account.setBind(true);
                            view.registerSuccess();
                        }
                    });
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
     * 检查手机号是否合法
     *
     * @param phone 手机号码
     * @return 合法为True
     */
    @Override
    public boolean checkMobile(String phone) {
        // 手机号不为空，并且满足格式
        return !TextUtils.isEmpty(phone)
                && Pattern.matches(Common.Constance.REGEX_MOBILE, phone);
    }
}
