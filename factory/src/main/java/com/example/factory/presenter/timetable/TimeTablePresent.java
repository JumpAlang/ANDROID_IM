package com.example.factory.presenter.timetable;

import android.util.Log;

import com.example.common.factory.presenter.BaseContract;
import com.example.common.factory.presenter.BasePresenter;
import com.example.factory.BaseObserver;
import com.example.factory.data.helper.TimeTableHelper;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.card.SubjectCard;
import com.example.factory.net.Network;
import com.example.factory.net.RemoteService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TimeTablePresent extends BasePresenter<TimeTableContract.View>
        implements TimeTableContract.Presenter {
    public static final String TAG="TimeTablePresent";
    Observe observer;
    public TimeTablePresent(TimeTableContract.View view) {
        super(view);
    }

    @Override
    public void pullData() {
        observer = new Observe();
        RemoteService service = Network.remote();
        service.pullTimeData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    class Observe extends BaseObserver<String>{
        @Override
        public void onNext(RspModel<String> stringRspModel) {
            super.onNext(stringRspModel);
            List<SubjectCard> parse = TimeTableHelper.parse(stringRspModel.getResult());
            Log.d(TAG, "onNext: "+parse.size());
            getView().updateSucceed(parse);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if(observer!=null&&observer.getDisposable()!=null)
            if(!observer.getDisposable().isDisposed())
                observer.getDisposable().dispose();
    }
}
