package com.example.factory.presenter.user;

import android.text.TextUtils;
import android.util.Log;

import com.example.common.factory.data.DataSource;
import com.example.common.factory.presenter.BasePresenter;
import com.example.factory.BaseObserver;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.api.user.UserUpdateModel;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;
import com.example.factory.net.UploadHelper;
import com.example.factory.persistence.Account;


/**
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public class UpdateInfoPresenter extends BasePresenter<UpdateInfoContract.View>
        implements UpdateInfoContract.Presenter{
    public static final String TAG="UpdateInfoPresenter";
    public UpdateInfoPresenter(UpdateInfoContract.View view) {
        super(view);
        updateObserver=new UpdateObserver();
    }
    private UpdateObserver updateObserver;
    @Override
    public void update(final String photoFilePath, final String desc, final boolean isMan) {
        start();
        final UpdateInfoContract.View view = getView();

        if (TextUtils.isEmpty(photoFilePath) || TextUtils.isEmpty(desc)) {
            view.showError(R.string.data_account_update_invalid_parameter);
        } else {
            // 上传头像
            Factory.runOnAsync(new Runnable() {
                @Override
                public void run() {
                    String url = UploadHelper.getInstance().uploadPortrait(photoFilePath);
                    if (TextUtils.isEmpty(url)) {
                        // 上传失败
                        view.showError(R.string.data_upload_error);
                    } else {
                        // 构建Model
                        UserUpdateModel model = new UserUpdateModel("", url, desc,
                                isMan ? User.SEX_MAN : User.SEX_WOMAN);
                        // 进行网络请求，上传
                        UserHelper.update(model, updateObserver);
                    }
                }
            });
        }
    }

    @Override
    public void update(String str, int type) {
        Log.d(TAG, "update text:"+str+" type:"+type);
//        if(type==InfoUpdateActivity.NAME_CHANGE)
    }


    class UpdateObserver extends BaseObserver<UserCard>{
        final UpdateInfoContract.View view = getView();
        @Override
        public void onNext(RspModel<UserCard> rspModel) {
            super.onNext(rspModel);
            if (view == null)
                return;
            if (rspModel.success()) {
                UserCard userCard = rspModel.getResult();
                // 数据库的存储操作，需要把UserCard转换为User
                // 保存用户信息
                User user = userCard.build();
                user.save();
                // 返回成功
                view.updateSucceed();
            } else {
                int id = Factory.decodeRspCode(rspModel);
                view.showError(id);
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            final UpdateInfoContract.View view = getView();
            if (view == null)
                return;
            view.showError(R.string.data_network_error);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if(updateObserver!=null){
            if(!updateObserver.getDisposable().isDisposed())
                updateObserver.getDisposable().dispose();
        }
    }
}
