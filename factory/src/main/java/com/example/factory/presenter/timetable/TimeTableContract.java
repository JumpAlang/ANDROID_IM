package com.example.factory.presenter.timetable;

import com.example.common.factory.presenter.BaseContract;

public interface TimeTableContract {
    interface Presenter extends BaseContract.Presenter{
        void pullData();
    }
    interface View extends BaseContract.View<Presenter>{
        void updateSucceed();
    }
}
