package com.example.factory.presenter.timetable;

import com.example.common.factory.presenter.BaseContract;
import com.example.common.factory.presenter.BasePresenter;

public class TimeTablePresent extends BasePresenter<TimeTableContract.View>
        implements TimeTableContract.Presenter {

    public TimeTablePresent(TimeTableContract.View view) {
        super(view);
    }

}
