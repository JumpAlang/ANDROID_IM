package com.example.factory.presenter.timetable;

import com.example.common.factory.presenter.BaseContract;
import com.example.factory.model.card.SubjectCard;

import java.util.List;

public interface TimeTableContract {
    interface Presenter extends BaseContract.Presenter{
        void pullData();
    }
    interface View extends BaseContract.View<Presenter>{
        void updateSucceed(List<SubjectCard> subjectCards);
    }
}
