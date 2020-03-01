package com.example.factory.presenter.timetable;

import android.util.Log;

import com.example.common.factory.presenter.BaseContract;
import com.example.common.factory.presenter.BasePresenter;
import com.example.factory.BaseObserver;
import com.example.factory.model.api.RspModel;
import com.example.factory.net.Network;
import com.example.factory.net.RemoteService;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TimeTablePresent extends BasePresenter<TimeTableContract.View>
        implements TimeTableContract.Presenter {
    public static final String TAG="TimeTablePresent";
    Observe observer;
    public TimeTablePresent(TimeTableContract.View view) {
        super(view);
        observer = new Observe();
    }

    @Override
    public void pullData() {
        RemoteService service = Network.remote();
        service.pullTimeData()
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }
    class Observe extends BaseObserver<String>{
        @Override
        public void onNext(RspModel<String> stringRspModel) {
            super.onNext(stringRspModel);
            Log.d(TAG, "onNext: "+stringRspModel.getResult());
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if(observer!=null)
            observer.getDisposable().dispose();
    }
}
