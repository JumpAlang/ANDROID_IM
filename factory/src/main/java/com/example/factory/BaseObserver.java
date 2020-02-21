package com.example.factory;

import android.util.Log;

import com.example.common.factory.presenter.BaseContract;
import com.example.factory.model.api.RspModel;
import com.example.factory.presenter.account.LoginContract;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<RspModel<T>>{

    public final String TAG=this.getClass().getName();
    Disposable d;

    public Disposable getDisposable() {
        return d;
    }

    @Override
    public void onSubscribe(Disposable d) {
        Log.d(TAG, "onSubscribe: ");
        this.d=d;
    }

    @Override
    public void onNext(RspModel<T> tRspModel) {
        Log.d(TAG, "onNext: ");
    }

    @Override
    public void onError(Throwable e) {
        Log.d(TAG, "onError: ");
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete: ");
    }
}
