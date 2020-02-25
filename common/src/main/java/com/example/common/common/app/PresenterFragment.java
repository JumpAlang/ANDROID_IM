package com.example.common.common.app;

import android.content.Context;
import android.util.Log;

import com.example.common.factory.presenter.BaseContract;

/**
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public abstract class PresenterFragment<Presenter extends BaseContract.Presenter> extends Fragment
        implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;

    @Override
    public void onAttach(Context context) {
        Log.d("TAG", "onAttach: ");
        super.onAttach(context);

        // 在界面onAttach之后就触发初始化Presenter
        initPresenter();
    }

    /**
     * 初始化Presenter
     * @return Presenter
     */
    protected abstract Presenter initPresenter();

    @Override
    public void showError(int strId) {
        // 显示错误
        if(mPlaceHolderView!=null){
            mPlaceHolderView.triggerError(strId);
        } else {
            String str = Application.getInstance().getResources().getString(strId);
            Application.showToast(str);
        }
    }

    @Override
    public void showLoading() {
        if(mPlaceHolderView!=null){
            mPlaceHolderView.triggerLoading();
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        // View中赋值Presenter
        mPresenter = presenter;
    }
}
