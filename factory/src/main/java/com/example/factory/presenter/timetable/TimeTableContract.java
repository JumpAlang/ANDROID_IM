package com.example.factory.presenter.timetable;

import com.example.common.factory.presenter.BaseContract;

public interface TimeTableContract {
    interface Presenter extends BaseContract.Presenter{

    }
    interface View extends BaseContract.View<Presenter>{
        void updateSucceed();
    }
}
